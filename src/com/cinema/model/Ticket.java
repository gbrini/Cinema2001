package com.cinema.model;

import java.time.LocalDate;

public class Ticket {
    private int ticketId;
    private int screeningId;
    private int typeId;
    private int seatId;
    private int userId;
    private LocalDate purchaseTime;
    private float finalPrice;

    private Ticket(Builder builder) {

    }

    public static class Builder {


        public Ticket build() { return new Ticket(this); }
    }
}
