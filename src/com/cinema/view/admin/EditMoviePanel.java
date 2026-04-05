package com.cinema.view.admin;

import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.util.constants.TextConstants;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Objects;

public class EditMoviePanel extends JPanel {
    private final Movie movie;
    private final User user;
    private JTextField titleField;
    private JFormattedTextField durationField;
    private JFormattedTextField dateField;
    private JTextField genreField;
    private JComboBox ratingField;
    private JTextArea descriptionField;
    private JTextField directorField;

    private JButton addButton;

    private final String[] S_COMBO = {"T", "6+ / 10+", "VM14", "VM18"};
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public EditMoviePanel(Movie movie, User user) {
        this.movie = movie;
        this.user = user;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Titolo: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        titleField = new JTextField(20);
        titleField.setText(this.movie == null ? "" : movie.getTitle());
        add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Durata (Minuti): "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setMinimum(1);
        formatter.setMaximum(999);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);

        durationField = new JFormattedTextField(formatter);
        durationField.setColumns(10);
        durationField.setText(this.movie == null ? "" : String.valueOf(movie.getDurationMinutes()));

        add(durationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Data d'uscita (yyyy-MM-dd): "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        try {
            MaskFormatter dateMask = new MaskFormatter("####-##-##");
            dateMask.setPlaceholderCharacter('_');

            dateField = new JFormattedTextField(dateMask);
            dateField.setColumns(10);

            if(this.movie != null && this.movie.getReleaseDate() != null) {
                dateField.setValue(this.movie.getReleaseDate());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Generi: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        genreField = new JTextField(20);
        genreField.setText(this.movie == null ? "" : movie.getGenre());
        add(genreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Rating: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        ratingField = new JComboBox<>(this.S_COMBO);
        if (this.movie != null) {
            int selectedIndex = 0;
            String rating = this.movie.getRating();
            for (int i = 0; i < this.S_COMBO.length; i++) {
                if (Objects.equals(this.S_COMBO[i], rating)) {
                    selectedIndex = i;
                    break;
                }
            }
            ratingField.setSelectedIndex(selectedIndex);
        }
        add(ratingField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Descrizione: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        descriptionField = new JTextArea(this.movie == null ? "" : movie.getDescription(),5, 8);
        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(descriptionField);
        add(scrollDesc, gbc);
        gbc.weighty = 0;

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        add(new JLabel("Regista: "), gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        directorField = new JTextField(20);
        directorField.setText(this.movie == null ? "" : movie.getDirector());
        add(directorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        addButton = new JButton((this.movie == null ? TextConstants.ADD_TXT : TextConstants.EDIT_TXT)  + " Film");
        add(addButton, gbc);
        //addButton.addActionListener(e -> addMovie());
    }

    public Movie getFormData() {
        int durationMinute;
        String parsedDate;
        String selectedRating;

        try {
            durationMinute = Integer.parseInt(durationField.getText());
        }
        catch (NumberFormatException e) {
            durationMinute = 0;
        }

        try {
            parsedDate = dateField.getFormatter().valueToString(dateField.getValue());
        } catch (ParseException ex) {
            parsedDate = null;
        }

        if (this.ratingField.getSelectedItem() == null) {
            selectedRating = this.S_COMBO[0];
        } else {
            selectedRating = this.ratingField.getSelectedItem().toString();
        }

        return new Movie.Builder()
                .setMovieId(this.movie == null ? 0 : this.movie.getMovieId())
                .setTitle(this.titleField.getText().trim())
                .setDurationMinutes(durationMinute)
                .setReleaseDate(LocalDate.parse(parsedDate))
                .setGenre(this.genreField.getText().trim())
                .setRating(selectedRating)
                .setDescription(this.descriptionField.getText().trim())
                .setDirector(this.directorField.getText().trim())
                .setIsDeleted(false)
                .build();
    }

    public void displayError(String errorMsg) {
        JOptionPane.showMessageDialog(this, errorMsg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public void addSaveListener(ActionListener listener) { this.addButton.addActionListener(listener); }

    public JButton getAddButton() { return this.addButton; }
}
