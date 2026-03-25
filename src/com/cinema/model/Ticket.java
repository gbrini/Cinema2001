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
        this.ticketId = builder.ticketId;
        this.screeningId = builder.screeningId;
        this.typeId = builder.typeId;
        this.seatId = builder.seatId;
        this.userId = builder.userId;
        this.purchaseTime = builder.purchaseTime;
        this.finalPrice = builder.finalPrice;
    }

    public static class Builder {
        private int ticketId;
        private int screeningId;
        private int typeId;
        private int seatId;
        private int userId;
        private LocalDate purchaseTime;
        private float finalPrice;

        public Builder setTicketId(int ticketId) { this.ticketId = ticketId; return this; }
        public Builder setScreeningId(int screeningId) { this.screeningId = screeningId; return this; }
        public Builder setTypeId(int typeId) { this.typeId = typeId; return this; }
        public Builder setSeatId(int seatId) { this.seatId = seatId; return this; }
        public Builder setUserId(int userId) { this.userId = userId; return this; }
        public Builder setPurchaseTime(LocalDate purchaseTime) { this.purchaseTime = purchaseTime; return this; }
        public Builder setFinalPrice(float finalPrice) { this.finalPrice = finalPrice; return this; }

        public Ticket build() { return new Ticket(this); }
    }
}
