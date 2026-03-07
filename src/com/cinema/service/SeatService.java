package com.cinema.service;

import com.cinema.model.Seat;
import com.cinema.model.SeatEditor;
import com.cinema.model.dao.SeatDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.util.ArrayList;

public class SeatService {
    public static boolean addSeats(ArrayList<Seat> seats) {
        if (!PermissionService.hasPermission("seat:add"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return SeatDAO.addSeats(seats);
    }

    public static boolean upsertSeats(ArrayList<Seat> seats) {
        if (!PermissionService.hasPermission("seat:add") && PermissionService.hasPermission("seat:edit"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return SeatDAO.upsertSeats(seats);
    }

    public static boolean removeSeatsByScreenId(int screenId) {
        if (!PermissionService.hasPermission("seat:remove"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return SeatDAO.removeSeatsByScreenId(screenId);
    }

    public static ArrayList<Seat> getSeatsByScreenId(int screenId) {
        if (!PermissionService.hasPermission("seat:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return SeatDAO.getSeatsByScreenId(screenId);
    }

    public static ArrayList<SeatEditor> getSeatsStatusByScreeningId(int screeningId) {
        if (!PermissionService.hasPermission("seat:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return SeatDAO.getSeatsStatusByScreeningId(screeningId);
    }
}
