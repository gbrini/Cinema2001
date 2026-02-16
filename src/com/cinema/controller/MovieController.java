package com.cinema.controller;

import com.cinema.model.Movie;
import com.cinema.model.User;
import com.cinema.service.MovieService;
import com.cinema.service.auth.UserSession;
import com.cinema.util.DialogCloseObserver;
import com.cinema.util.Observable;
import com.cinema.view.EditMoviePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MovieController implements Observable<DialogCloseObserver> {
    private final User user;
    private final EditMoviePanel view;
    private final List<DialogCloseObserver> observers = new ArrayList<>();

    public MovieController(Movie movie) {
        this.user = UserSession.getInstance().getCurrentUser();
        this.view = new EditMoviePanel(movie, this.user);
        this.attachListener();
    }

    public EditMoviePanel getView() { return this.view; }

    private void attachListener() {
        this.view.addSaveListener(_ -> this.save());
    }

    private void save() {
        Movie newMovie = this.view.getFormData();

        if (newMovie.getTitle() == null || newMovie.getTitle().trim().isEmpty()) {
            this.view.displayError("The title is needed");
            return;
        }

        if (newMovie.getDurationMinutes() <= 0) {
            this.view.displayError("The duration must be greater then 0");
            return;
        }

        if (newMovie.getGenre() == null || newMovie.getGenre().trim().isEmpty()) {
            this.view.displayError("The genre is needed");
            return;
        }

        if (newMovie.getRating() == null || newMovie.getRating().trim().isEmpty()) {
            this.view.displayError("The rating is needed");
            return;
        }

        if (newMovie.getDirector() == null || newMovie.getDirector().trim().isEmpty()) {
            this.view.displayError("The director is needed");
            return;
        }

        if (newMovie.getDescription() == null || newMovie.getDescription().trim().isEmpty()) {
            this.view.displayError("The description is needed");
            return;
        }

        if (newMovie.getReleaseDate() == null) {
            this.view.displayError("The release date is needed");
            return;
        }

        boolean isOk;

        if (newMovie.getMovieId() == 0) {
            isOk = MovieService.addMovie(newMovie) != 0;

        } else {
            isOk = MovieService.updateMovie(newMovie);
        }

        this.notifyObservers(isOk);
        this.closeDialog();
    }

    private void closeDialog() {
        Window window = SwingUtilities.getWindowAncestor(this.view);

        if (window instanceof JDialog dialog) {
            dialog.dispose();
        }
    }

    @Override
    public void addObserver(DialogCloseObserver observer) { this.observers.add(observer); }

    @Override
    public void deleteObserver(DialogCloseObserver observer) { this.observers.remove(observer); }

    @Override
    public void notifyObservers(boolean changedSaved) {
        for (DialogCloseObserver observer: this.observers) {
            observer.onDialogClosed(changedSaved);
        }
    }
}
