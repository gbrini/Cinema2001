package com.cinema.service;

import com.cinema.model.*;
import com.cinema.model.dao.SeatDAO;
import com.cinema.model.dao.TicketDAO;
import com.cinema.util.TicketFactory;
import com.cinema.util.UnauthorizedAccessException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TicketService {
    public static Ticket buyTicket(Screening screening, TicketType ticketType, Seat seat, User user) {
        if (!PermissionService.hasPermission("ticket:buy"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        if (SeatDAO.isSeatTaken(screening.getScreeningId(), seat.getSeatId()))
            throw new IllegalStateException("Il posto è già occupato");

        if (screening.getStartTime().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("La proiezione è già iniziata");

        Ticket ticket = TicketFactory.createTicket(screening, ticketType, seat, user);
        boolean saved = TicketDAO.saveTicket(ticket);

        if (!saved)
            throw new IllegalStateException("Errore durante il salvataggio del biglietto");

        return ticket;
    }

    public static boolean buyTickets(ArrayList<Ticket> tickets, Screening screening) {
        if (!PermissionService.hasPermission("ticket:buy"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        if (screening.getStartTime().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("La proiezione è già iniziata");

        for (Ticket t: tickets) {
            if (SeatDAO.isSeatTaken(screening.getScreeningId(), t.getSeatId()))
                throw new IllegalStateException("Il posto è già occupato");
        }

        boolean saved = TicketDAO.saveBatchTickets(tickets);

        if (!saved)
            throw new IllegalStateException("Errore durante il salvataggio dei biglietti");

        return true;
    }

    public static ArrayList<TicketRecord> getTicketsByUser(int userId) {
        if (!PermissionService.hasPermission("ticket:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        return TicketDAO.getTicketRecordsByUserId(userId);
    }

    public static boolean cancelTicket(int ticketId, Screening screening) {
        if (!PermissionService.hasPermission("ticket:cancel"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        if (screening.getStartTime().minusHours(1).isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Annullamento non consentito: manca meno di 1 ora all'inizio dello spettacolo");

        return TicketDAO.deleteTicket(ticketId);
    }
}