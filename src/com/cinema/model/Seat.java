package com.cinema.model;

public class Seat {
    private int seatId;
    private int screenId;
    private String seatRow;
    private int seatNumber;
    private boolean isVip;
    private boolean isHandicap;
    private boolean isActive;

    public Seat(int seatId, int screenId, String seatRow, int seatNumber, boolean isVip, boolean isHandicap, boolean isActive) {
        this.seatId = seatId;
        this.screenId = screenId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
        this.isHandicap = isHandicap;
        this.isActive = isActive;
    }

    public Seat(int screenId, String seatRow, int seatNumber, boolean isVip, boolean isHandicap, boolean isActive) {
        this.screenId = screenId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
        this.isHandicap = isHandicap;
        this.isActive = isActive;
    }

    public Seat(String seatRow, int seatNumber, boolean isVip, boolean isHandicap, boolean isActive) {
        this.screenId = 0;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
        this.isHandicap = isHandicap;
        this.isActive = isActive;
    }

    public int getSeatId() {
        return seatId;
    }

    public int getScreenId() {
        return screenId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public boolean isVip() {
        return isVip;
    }

    public boolean isHandicap() {
        return isHandicap;
    }

    public boolean isActive() {
        return isActive;
    }
}
