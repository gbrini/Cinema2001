package com.cinema.model;

public record TicketRecord(
        Ticket ticket,
        Screening screening,
        Movie movie,
        Seat seat,
        TicketType ticketType,
        String screenName
) {}
