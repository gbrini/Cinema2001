package com.cinema.controller;

import com.cinema.model.*;
import com.cinema.service.TicketService;
import com.cinema.service.TicketTypeService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.Observable;
import com.cinema.view.SeatComponent;
import com.cinema.view.SeatMapPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TicketController extends BaseController implements Observable<DialogCloseObserver> {

    private final User user;
    private final ScreeningRecord screeningRecord;
    private final SeatMapPanel view;
    private final List<DialogCloseObserver> observers = new ArrayList<>();

    public TicketController(ScreeningRecord screeningRecord) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.screeningRecord = screeningRecord;
        this.view = new SeatMapPanel(
                screeningRecord.screening().getScreeningId(),
                true,
                this.user,
                screeningRecord
        );
        this.attachListeners();
    }

    private void attachListeners() {
        this.view.addSeatSelectionListener(e -> {
            SeatComponent seatComponent = (SeatComponent) e.getSource();
            SeatEditor seatEditor = seatComponent.getSeatEditor();

            // Ignora posti inattivi o già occupati
            if (!seatEditor.isActive() || seatEditor.isTaken()) return;

            this.openTicketTypeDialog(seatEditor);
        });
    }

    private void openTicketTypeDialog(SeatEditor seatEditor) {
        List<TicketType> ticketTypes = new ArrayList<>();
        try {
            ticketTypes = TicketTypeService.getTicketTypes();
        } catch (Exception e) {
            handleException(e);
            return;
        }

        if (ticketTypes.isEmpty()) {
            JOptionPane.showMessageDialog(this.view.getView(),
                    "Nessun tipo di biglietto disponibile.",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ── Dialog ───────────────────────────────────────────────────────────
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this.view.getView()),
                "Acquisto — Posto " + seatEditor.getSeatRow() + seatEditor.getSeatNumber(),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Info posto
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        String postoInfo = String.format("Posto: %s%d%s%s",
                seatEditor.getSeatRow(),
                seatEditor.getSeatNumber(),
                seatEditor.isVip()      ? " · VIP"      : "",
                seatEditor.isHandicap() ? " · Handicap" : "");
        panel.add(new JLabel(postoInfo), gbc);

        // Prezzo base proiezione
        gbc.gridy = 1;
        float basePrice = screeningRecord.screening().getTicketPrice();
        panel.add(new JLabel("Prezzo base: " + String.format("%.2f $", basePrice)), gbc);

        // ComboBox tipo biglietto
        gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Tipo biglietto:"), gbc);

        JComboBox<TicketType> typeComboBox = new JComboBox<>(ticketTypes.toArray(new TicketType[0]));
        typeComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel lbl = new JLabel(value != null ? value.getTypeName() : "");
            if (isSelected) { lbl.setBackground(list.getSelectionBackground()); lbl.setOpaque(true); }
            return lbl;
        });
        gbc.gridx = 1;
        panel.add(typeComboBox, gbc);

        // Prezzo finale (aggiornato dinamicamente)
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel priceLabel = new JLabel();
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(priceLabel, gbc);

        updatePriceLabel(priceLabel, (TicketType) typeComboBox.getSelectedItem(), basePrice);
        typeComboBox.addActionListener(e ->
                updatePriceLabel(priceLabel, (TicketType) typeComboBox.getSelectedItem(), basePrice));

        // Bottoni
        gbc.gridy = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton  = new JButton("Annulla");
        JButton confirmButton = new JButton("Acquista");
        confirmButton.setBackground(new Color(0, 150, 80));
        confirmButton.setForeground(Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        panel.add(buttonPanel, gbc);

        cancelButton.addActionListener(e -> dialog.dispose());
        confirmButton.addActionListener(e -> {
            TicketType selectedType = (TicketType) typeComboBox.getSelectedItem();
            if (selectedType != null) this.buyTicket(seatEditor, selectedType, dialog);
        });

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this.view.getView()));
        dialog.setVisible(true);
    }

    private void updatePriceLabel(JLabel label, TicketType ticketType, float basePrice) {
        if (ticketType == null) return;
        float discount   = basePrice * ticketType.getBaseDiscountPercent() / 100f;
        float finalPrice = basePrice - discount + ticketType.getBasePriceAddendum();
        label.setText(String.format("Prezzo finale: %.2f $", finalPrice));
    }

    private void buyTicket(SeatEditor seatEditor, TicketType ticketType, JDialog dialog) {
        Screening screening = screeningRecord.screening();

        Seat seat = new Seat(
                seatEditor.getSeatId(),
                screening.getScreenId(),
                seatEditor.getSeatRow(),
                seatEditor.getSeatNumber(),
                seatEditor.isVip(),
                seatEditor.isHandicap(),
                seatEditor.isActive()
        );

        try {
            TicketService.buyTicket(screening, ticketType, seat, this.user);

            seatEditor.setTaken(true);
            this.view.refreshSeatMap();
            dialog.dispose();

            JOptionPane.showMessageDialog(
                    SwingUtilities.getWindowAncestor(this.view.getView()),
                    "Biglietto acquistato con successo!",
                    "Acquisto completato", JOptionPane.INFORMATION_MESSAGE);

            this.notifyObservers(true);

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(dialog,
                    ex.getMessage(), "Acquisto non consentito", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public SeatMapPanel getView() { return view; }

    @Override
    public void addObserver(DialogCloseObserver observer) { this.observers.add(observer); }

    @Override
    public void deleteObserver(DialogCloseObserver observer) { this.observers.remove(observer); }

    @Override
    public void notifyObservers(boolean saved) {
        for (DialogCloseObserver observer : this.observers) observer.onDialogClosed(saved);
    }
}