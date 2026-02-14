package com.cinema.model;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class Screening {
    private int screeningId;
    private int movieId;
    private int screenId;
    private LocalDateTime startTime;
    private double ticketPrice;
    private boolean isDeleted;

    public Screening(int screeningId, int movieId, int screenId, LocalDateTime startTime, double ticketPrice, boolean isDeleted) {
        this.screeningId = screeningId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
        this.isDeleted = isDeleted;
    }

    public Screening(int movieId, int screenId, LocalDateTime startTime, double ticketPrice, boolean isDeleted) {
        this.movieId = movieId;
        this.screenId = screenId;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
        this.isDeleted = isDeleted;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public void setScreeningId(int screeningId) {
        this.screeningId = screeningId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ZonedDateTime getLocalStartTime() {
        ZoneId zoneId = ZoneId.systemDefault();
        return this.startTime.atZone(zoneId);
    }

    public LocalDate getStartTimeDate() {
        return startTime.toLocalDate();
    }

    public LocalTime getStartTimeT() {
        return startTime.toLocalTime();
    }
}
