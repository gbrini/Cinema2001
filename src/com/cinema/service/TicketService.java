package com.cinema.service;

import com.cinema.model.Screening;
import com.cinema.model.Seat;
import com.cinema.model.Ticket;
import com.cinema.model.TicketType;
import com.cinema.model.User;
import com.cinema.model.dao.TicketDAO;
import com.cinema.util.TicketFactory;
import com.cinema.util.UnauthorizedAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TicketService {
    public static Ticket buyTicket(Screening screening, TicketType ticketType, Seat seat, User user) {
        if (!PermissionService.hasPermission("ticket:buy"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        if (TicketDAO.isSeatTaken(screening.getScreeningId(), seat.getSeatId()))
            throw new IllegalStateException("Il posto è già occupato");

        if (screening.getStartTime().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("La proiezione è già iniziata");

        Ticket ticket = TicketFactory.createTicket(screening, ticketType, seat, user);
        boolean saved = TicketDAO.saveTicket(ticket);

        if (!saved)
            throw new IllegalStateException("Errore durante il salvataggio del biglietto");

        return ticket;
    }

    public static ArrayList<Ticket> getTicketsByUser(int userId) {
        if (!PermissionService.hasPermission("ticket:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        return TicketDAO.getTicketsByUserId(userId);
    }

    public static boolean cancelTicket(int ticketId, Screening screening) {
        if (!PermissionService.hasPermission("ticket:cancel"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        if (screening.getStartTime().minusHours(1).isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Annullamento non consentito: manca meno di 1 ora all'inizio dello spettacolo");

        return TicketDAO.deleteTicket(ticketId);
    }
}