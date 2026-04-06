package com.cinema.util;

import com.cinema.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketFactory {
    public static Ticket createTicket(Screening screening, TicketType ticketType, Seat seat, User user) {
        float basePrice = screening.getTicketPrice();
        float discount = ticketType.getBaseDiscountPercent();
        float addendum = ticketType.getBasePriceAddendum();

        float finalPrice = basePrice - ( basePrice * discount / 100 ) + addendum;

        return new Ticket.Builder()
                .setFinalPrice(finalPrice)
                .setPurchaseTime(LocalDateTime.now())
                .setScreeningId(screening.getScreeningId())
                .setSeatId(seat.getSeatId())
                .setUserId(user.getUserId())
                .setTypeId(ticketType.getTypeId())
                .build();
    }
}
