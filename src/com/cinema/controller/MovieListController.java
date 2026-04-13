package com.cinema.controller;

import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.UnauthorizedAccessException;
import com.cinema.util.constants.TextConstants;
import com.cinema.view.ListMoviePanel;
import com.cinema.view.listener.PanelActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MovieListController extends BaseController implements PanelActionListener<Movie>, DialogCloseObserver {
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

        JDialog dialog = new JDialog(ownerFrame, (item == null ? TextConstants.ADD_TXT : TextConstants.EDIT_TXT) + " film", true);

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
        try {
            if(!MovieService.deleteMovie(item.getMovieId())) {
                throw new Exception("Impossibile rimuovere il film");
            }
            this.onRefreshRequested();
        } catch (Exception e) {
            handleException(e);
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
