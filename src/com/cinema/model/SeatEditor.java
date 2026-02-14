package com.cinema.model;

public class SeatEditor {
    private int seatId;
    private String seatRow;
    private int seatNumber;
    private boolean isVip;
    private boolean isHandicap;
    private boolean isActive = true;
    private boolean isSelected = false;
    private boolean isTaken = false;

    public SeatEditor(String seatRow, int seatNumber) {
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
    }

    public SeatEditor(int seatId, String seatRow, int seatNumber, boolean isVip, boolean isHandicap, boolean isActive, boolean isSelected, boolean isTaken) {
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
        this.isVip = isVip;
        this.isHandicap = isHandicap;
        this.isActive = isActive;
        this.isSelected = isSelected;
        this.isTaken = isTaken;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
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

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isHandicap() {
        return isHandicap;
    }

    public void setHandicap(boolean handicap) {
        isHandicap = handicap;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isTaken() {
        return this.isTaken;
    }

    public void setTaken(boolean taken) {
        this.isTaken = taken;
    }

    @Override
    public String toString() {
        return seatRow + seatNumber;
    }
}
