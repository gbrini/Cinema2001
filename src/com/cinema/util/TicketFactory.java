package com.cinema.util;

import com.cinema.model.*;

public class TicketFactory {
    public static Ticket createTicket(Screening screening, TicketType ticketType, Seat seat, User user) {
        float basePrice = screening.getTicketPrice();
        float discount = ticketType.getBaseDiscountPercent();
        float addendum = ticketType.getBasePriceAddendum();

        float finalPrice = basePrice - ( basePrice * discount / 100 ) + addendum;

        return null;
    }
}
