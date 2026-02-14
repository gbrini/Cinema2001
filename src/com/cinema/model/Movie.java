package com.cinema.model;

import com.cinema.util.ComboBoxMethods;

import java.time.LocalDate;
import java.util.Date;

public class Movie implements ComboBoxMethods {
    private int movieId;
    private String title;
    private int durationMinutes;
    private LocalDate releaseDate;
    private String genre;
    private String rating;
    private String description;
    private String director;
    private boolean isDeleted;

    public Movie(int movieId, String title, int durationMinutes, LocalDate releaseDate, String genre, String rating, String description, String director, boolean isDeleted) {
        this.movieId = movieId;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.director = director;
        this.isDeleted = isDeleted;
    }

    public Movie(String title, int durationMinutes, LocalDate releaseDate, String genre, String rating, String description, String director, boolean isDeleted) {
        this.movieId = 0;
        this.title = title;
        this.durationMinutes = durationMinutes;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.rating = rating;
        this.description = description;
        this.director = director;
        this.isDeleted = isDeleted;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    public String getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getDirector() {
        return director;
    }

    public boolean is_Deleted() {
        return isDeleted;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return String.format("%s - %d min", this.title, this.durationMinutes);
    }

    @Override
    public String toStringComboBox() {
        return String.format("%s (%s)", this.title, this.rating);
    }
}
