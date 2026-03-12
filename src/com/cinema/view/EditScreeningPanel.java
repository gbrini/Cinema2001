package com.cinema.view;

import com.cinema.model.*;
import com.cinema.service.MovieService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class EditScreeningPanel extends JPanel {
    private final Screening screening;
    private final User user;
    private ArrayList<Movie> movies;

    private final JFormattedTextField dateField;
    private final JComboBox movieSelect;
    private final JComboBox screenSelect;
    private final JComboBox timeSlotSelect;
    private final JButton saveButton;

    public EditScreeningPanel(Screening screening, User user) {
        this.screening = screening;
        this.user = user;
        this.movies = MovieService.getAllMovies();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Date (yyyy-MM-dd): "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');

            dateField = new JFormattedTextField(dateMask);
            dateField.setColumns(10);

            dateField.setValue(LocalDate.now().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        
        add(this.dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Movie: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.movieSelect = setupComboBox();
        add(this.movieSelect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Screen: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.screenSelect = setupComboBox();
        add(this.screenSelect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Time slot: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.timeSlotSelect = setupComboBox();
        add(this.timeSlotSelect, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        this.saveButton = new JButton((this.screening == null ? "Add" : "Edit")  + " Screening");
        add(this.saveButton, gbc);
    }

    private JComboBox setupComboBox() {
        JComboBox comboBox = new JComboBox<>();
        comboBox.setRenderer(new ComboBoxRenderer());
        return comboBox;
    }

    public void setAvailableMovies(ArrayList<Movie> movies) {
        for (Movie movie: movies) {
            this.movieSelect.addItem(movie);
        }
    }

    public void setAvailableScreens(ArrayList<Screen> screens) {
        for (Screen screen: screens) {
            this.screenSelect.addItem(screen);
        }
    }

    public void setAvailableTimeSlots(LocalTime[] timeSlots) {
        for (LocalTime slot: timeSlots) {
            this.timeSlotSelect.addItem(slot);
        }
    }

    public ScreeningRecord getScreeningData() {
        LocalDate date = LocalDate.parse(dateField.getText());
        Movie movie = (Movie) this.movieSelect.getSelectedItem();
        Screen screen = (Screen) this.screenSelect.getSelectedItem();
        LocalTime slot = (LocalTime) this.timeSlotSelect.getSelectedItem();

        if (movie == null || screen == null || slot == null) {
            return null;
        }

        LocalDateTime localDateTime = LocalDateTime.of(date, slot);

        Screening screening = new Screening(
            movie.getMovieId(),
            screen.getScreenId(),
            localDateTime,
            12,
            false
        );

        return new ScreeningRecord(screening, movie, date, slot, null, 0, 0);
    }

    public void addSaveListener(ActionListener listener) { this.saveButton.addActionListener(listener); }
}
