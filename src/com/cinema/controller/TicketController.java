package com.cinema.controller;

import com.cinema.model.*;
import com.cinema.service.TicketService;
import com.cinema.service.TicketTypeService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.Observable;
import com.cinema.util.TicketFactory;
import com.cinema.view.SeatComponent;
import com.cinema.view.SeatMapPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TicketController extends BaseController implements Observable<DialogCloseObserver> {

    private final User user;
    private final ScreeningRecord screeningRecord;
    private final SeatMapPanel view;
    private final List<DialogCloseObserver> observers = new ArrayList<>();
    private final ArrayList<Ticket> tickets = new ArrayList<>();

    private final JPanel cartItemsPanel = new JPanel();
    private final JLabel totalLabel     = new JLabel("Totale: 0.00 €");
    private final JButton buyButton     = new JButton("Acquista");
    private JPanel cachedView = null;
    private JPanel seatMapView;

    public TicketController(ScreeningRecord screeningRecord) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.screeningRecord = screeningRecord;
        this.view = new SeatMapPanel(
                screeningRecord.screening().getScreeningId(),
                this.user.getRole().getRoleId() == 3,
                this.user,
                screeningRecord
        );
    }

    public JPanel getView() {
        if (cachedView != null) return cachedView;

        seatMapView = view.getView();
        this.attachListeners();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                seatMapView,
                buildCartPanel()
        );
        splitPane.setResizeWeight(0.75);
        splitPane.setDividerSize(6);

        cachedView = new JPanel(new BorderLayout());
        cachedView.add(splitPane, BorderLayout.CENTER);
        return cachedView;
    }

    private JPanel buildCartPanel() {
        JPanel cart = new JPanel(new BorderLayout(0, 8));
        cart.setBorder(new EmptyBorder(12, 12, 12, 12));
        cart.setPreferredSize(new Dimension(260, 0));

        JLabel title = new JLabel("Carrello");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
        cart.add(title, BorderLayout.NORTH);

        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        cart.add(scrollPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout(0, 6));
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 13f));
        buyButton.setBackground(new Color(0, 150, 80));
        buyButton.setForeground(Color.WHITE);
        buyButton.setEnabled(false);
        buyButton.addActionListener(e -> buyTickets());

        footer.add(totalLabel, BorderLayout.NORTH);
        footer.add(buyButton, BorderLayout.SOUTH);
        cart.add(footer, BorderLayout.SOUTH);

        return cart;
    }

    private void addCartRow(Ticket ticket, SeatEditor seatEditor, TicketType ticketType) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setBorder(new EmptyBorder(4, 4, 4, 4));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        String label = String.format("%s%d · %s · %.2f €",
                seatEditor.getSeatRow(),
                seatEditor.getSeatNumber(),
                ticketType.getTypeName(),
                ticket.getFinalPrice());

        row.add(new JLabel(label), BorderLayout.CENTER);

        JButton removeBtn = new JButton("X");
        removeBtn.setPreferredSize(new Dimension(28, 28));
        removeBtn.setFocusPainted(false);
        removeBtn.setForeground(Color.RED);
        removeBtn.addActionListener(e -> removeTicket(ticket, seatEditor, row));
        row.add(removeBtn, BorderLayout.EAST);

        cartItemsPanel.add(row);
        cartItemsPanel.add(Box.createVerticalStrut(2));
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void removeTicket(Ticket ticket, SeatEditor seatEditor, JPanel row) {
        tickets.remove(ticket);

        seatEditor.setTaken(false);
        refreshSeatComponent(seatEditor);

        int index = cartItemsPanel.getComponentZOrder(row);
        cartItemsPanel.remove(row);

        if (index < cartItemsPanel.getComponentCount()) {
            cartItemsPanel.remove(index);
        }

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();

        updateTotal();
    }

    private void updateTotal() {
        float total = (float) tickets.stream()
                .mapToDouble(Ticket::getFinalPrice)
                .sum();
        totalLabel.setText(String.format("Totale: %.2f €", total));
        buyButton.setEnabled(!tickets.isEmpty());
    }

    private void refreshSeatComponent(SeatEditor seatEditor) {
//        System.out.println("Cerco seatEditor: " + seatEditor.getSeatRow() + seatEditor.getSeatNumber());
//        System.out.println("SeatComponents disponibili: " + view.getSeatComponents().size());
//
//        for (SeatComponent sc : view.getSeatComponents()) {
//            System.out.println("  → " + sc.getSeatEditor().getSeatRow() + sc.getSeatEditor().getSeatNumber()
//                    + " | stesso oggetto? " + (sc.getSeatEditor() == seatEditor));
//        }

        for (SeatComponent sc : view.getSeatComponents()) {
            if (sc.getSeatEditor() == seatEditor) {
                sc.getSeatEditor().setTaken(false);
                sc.setSelected(false);
                sc.updateAppearance();

                cachedView.revalidate();
                cachedView.repaint();

                break;
            }
        }
    }

    private void attachListeners() {
        this.view.addSeatSelectionListener(e -> {
            SeatComponent seatComponent = (SeatComponent) e.getSource();
            SeatEditor seatEditor = seatComponent.getSeatEditor();

            if (!seatEditor.isActive() || seatEditor.isTaken()) return;

            this.openTicketTypeDialog(seatEditor, seatComponent);
        });
    }

    private void openTicketTypeDialog(SeatEditor seatEditor, SeatComponent seatComponent) {
        List<TicketType> ticketTypes = new ArrayList<>();
        try {
            ticketTypes = TicketTypeService.getTicketTypes();
        } catch (Exception e) {
            handleException(e);
            return;
        }

        if (ticketTypes.isEmpty()) {
            JOptionPane.showMessageDialog(this.view.getView(),
                    "Nessun tipo di biglietto disponibile.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this.view.getView()),
                "Aggiungi posto " + seatEditor.getSeatRow() + seatEditor.getSeatNumber(),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        String postoInfo = String.format("Posto: %s%d%s%s",
                seatEditor.getSeatRow(), seatEditor.getSeatNumber(),
                seatEditor.isVip()      ? " · VIP"      : "",
                seatEditor.isHandicap() ? " · Handicap" : "");
        panel.add(new JLabel(postoInfo), gbc);

        gbc.gridy = 1;
        float basePrice = screeningRecord.screening().getTicketPrice();
        panel.add(new JLabel("Prezzo base: " + String.format("%.2f €", basePrice)), gbc);

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

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel priceLabel = new JLabel();
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(priceLabel, gbc);

        updatePriceLabel(priceLabel, (TicketType) typeComboBox.getSelectedItem(), basePrice);
        typeComboBox.addActionListener(e ->
                updatePriceLabel(priceLabel, (TicketType) typeComboBox.getSelectedItem(), basePrice));

        gbc.gridy = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton  = new JButton("Annulla");
        JButton confirmButton = new JButton("Aggiungi");
        confirmButton.setBackground(new Color(0, 150, 80));
        confirmButton.setForeground(Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        panel.add(buttonPanel, gbc);

        cancelButton.addActionListener(e -> dialog.dispose());
        confirmButton.addActionListener(e -> {
            TicketType selectedType = (TicketType) typeComboBox.getSelectedItem();
            if (selectedType != null) addTicket(seatEditor, seatComponent, selectedType, dialog);
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
        label.setText(String.format("Prezzo finale: %.2f €", finalPrice));
    }

    private void addTicket(SeatEditor seatEditor, SeatComponent seatComponent,
                           TicketType ticketType, JDialog dialog) {
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

        Ticket ticket = TicketFactory.createTicket(screening, ticketType, seat, user);
        tickets.add(ticket);

        seatEditor.setTaken(true);
        seatComponent.setSelected(false);
        seatComponent.updateAppearance();

        addCartRow(ticket, seatEditor, ticketType);
        updateTotal();

        dialog.dispose();
    }

    public void buyTickets() {
        if (tickets.isEmpty()) return;

        try {
            //aceuisto i tickets

            int n = tickets.size();
            JOptionPane.showMessageDialog(
                    this.view.getView(),
                    n + " bigliett" + (n == 1 ? "o acquistato" : "i acquistati") + " con successo!",
                    "Acquisto completato", JOptionPane.INFORMATION_MESSAGE);

            tickets.clear();
            cartItemsPanel.removeAll();
            cartItemsPanel.revalidate();
            cartItemsPanel.repaint();
            updateTotal();
            this.notifyObservers(true);

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this.view.getView(),
                    ex.getMessage(), "Acquisto non consentito", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            handleException(e);
        }
    }

    public ArrayList<Ticket> getTickets() { return tickets; }

    @Override
    public void addObserver(DialogCloseObserver observer) { this.observers.add(observer); }

    @Override
    public void deleteObserver(DialogCloseObserver observer) { this.observers.remove(observer); }

    @Override
    public void notifyObservers(boolean saved) {
        for (DialogCloseObserver observer : this.observers) observer.onDialogClosed(saved);
    }
}