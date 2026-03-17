package com.cinema.controller;

import com.cinema.util.UnauthorizedAccessException;

import javax.swing.*;
import java.sql.SQLException;

public abstract class BaseController {

    protected void handleException(Exception e) {
        if (e instanceof UnauthorizedAccessException) {
            JOptionPane.showMessageDialog(null,
                    "Non hai i permessi per eseguire questa operazione.",
                    "Accesso negato",
                    JOptionPane.WARNING_MESSAGE);
        } else if (e instanceof SQLException) {
            JOptionPane.showMessageDialog(null,
                    "Errore di connessione al database.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Si è verificato un errore imprevisto.",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}