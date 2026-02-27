package com.cinema.model;

import com.cinema.util.ComboBoxMethods;

import java.time.LocalDate;

public class Movie implements ComboBoxMethods {
    private final int movieId;
    private final String title;
    private final int durationMinutes;
    private final LocalDate releaseDate;
    private final String genre;
    private final String rating;
    private final String description;
    private final String director;
    private final boolean isDeleted;

    public Movie(Builder builder) {
        this.movieId = builder.movieId;
        this.title = builder.title;
        this.durationMinutes = builder.durationMinutes;
        this.releaseDate = builder.releaseDate;
        this.genre = builder.genre;
        this.rating = builder.rating;
        this.description = builder.description;
        this.director = builder.director;
        this.isDeleted = builder.isDeleted;
    }

    public static class Builder {
        private int movieId;
        private String title;
        private int durationMinutes;
        private LocalDate releaseDate;
        private String genre;
        private String rating;
        private String description;
        private String director;
        private boolean isDeleted;

        public Builder setMovieId(int movieId) { this.movieId = movieId; return this; }
        public Builder setTitle(String title) { this.title = title; return this; }
        public Builder setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public Builder setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; return this; }
        public Builder setGenre(String genre) { this.genre = genre; return this; }
        public Builder setRating(String rating) { this.rating = rating; return this; }
        public Builder setDescription(String description) { this.description = description; return this; }
        public Builder setDirector(String director) { this.director = director; return this; }
        public Builder setIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; return this; }

        public Movie build() { return new Movie(this); }
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

    public boolean isDeleted() {
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
