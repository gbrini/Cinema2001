package com.cinema.controller;

import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.UnauthorizedAccessException;
import com.cinema.view.ListMoviePanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MovieListController implements PanelActionListener<Movie>, DialogCloseObserver {
    private final User user;
    private final ListMoviePanel view;

    public MovieListController() {
        this.user = UserSession.getInstance().getCurrentUser();
        this.view = new ListMoviePanel(this, this.user.getRole().getRoleId());
        this.onRefreshRequested();
    }

    public ListMoviePanel getView() { return this.view; }

    @Override
    public void onRefreshRequested() {
        ArrayList<Movie> movies = MovieService.getAllMovies();
        this.view.setContentList(movies);
    }

    @Override
    public void onEditRequested(Movie item) {
        Window ownerWindow = SwingUtilities.getWindowAncestor(this.view);
        Frame ownerFrame = (ownerWindow instanceof Frame) ? (Frame) ownerWindow : JOptionPane.getRootFrame();

        JDialog dialog = new JDialog(ownerFrame, (item == null ? "Add" : "Edit") + " movie", true);

        MovieController movieController = new MovieController(item);
        movieController.addObserver(this);

        dialog.setContentPane(movieController.getView());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(ownerFrame);
        dialog.setVisible(true);
    }

    @Override
    public void onDeleteRequested(Movie item) {
        boolean ok;

        try {
            ok = MovieService.deleteMovie(item.getMovieId());
        } catch (UnauthorizedAccessException exc) {
            ok = false;
        }

        if (ok) {
            this.onRefreshRequested();
        } else {
            JOptionPane.showMessageDialog(this.view, "Errore!");
        }
    }

    @Override
    public void onDialogClosed(boolean changedSaved) {
        String message = changedSaved ? "Movie saved successfully" : "Editing cancelled or failed";
        JOptionPane.showMessageDialog(this.view, message);

        if (changedSaved) {
            this.onRefreshRequested();
        }
    }
}
