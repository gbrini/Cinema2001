package com.cinema.service;

import com.cinema.model.Seat;
import com.cinema.model.SeatEditor;
import com.cinema.model.dao.SeatDAO;

import java.util.ArrayList;

public class SeatService {
    public static boolean addSeats(ArrayList<Seat> seats) {
        if (PermissionService.hasPermission("seat:add"))
            return SeatDAO.addSeats(seats);
        return false;
    }

    public static boolean upsertSeats(ArrayList<Seat> seats) {
        if (PermissionService.hasPermission("seat:add") && PermissionService.hasPermission("seat:edit"))
            return SeatDAO.upsertSeats(seats);
        return false;
    }

    public static boolean removeSeatsByScreenId(int screenId) {
        if (PermissionService.hasPermission("seat:remove"))
            return SeatDAO.removeSeatsByScreenId(screenId);
        return false;
    }

    public static ArrayList<Seat> getSeatsByScreenId(int screenId) {
        if (PermissionService.hasPermission("seat:view"))
            return SeatDAO.getSeatsByScreenId(screenId);
        return new ArrayList<>();
    }

    public static ArrayList<SeatEditor> getSeatsStatusByScreeningId(int screeningId) {
        return SeatDAO.getSeatsStatusByScreeningId(screeningId);
    }
}
