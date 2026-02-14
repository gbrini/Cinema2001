package com.cinema.service;

import com.cinema.model.Seat;
import com.cinema.model.SeatEditor;
import com.cinema.model.User;
import com.cinema.model.dao.SeatDAO;

import java.util.ArrayList;

public class SeatService {
    public static boolean addSeats(ArrayList<Seat> seats, User user) {
        if (PermissionService.hasPermission(user, "seat:add"))
            return SeatDAO.addSeats(seats);
        return false;
    }

    public static boolean upsertSeats(ArrayList<Seat> seats, User user) {
        if (PermissionService.hasPermission(user, "seat:add") && PermissionService.hasPermission(user, "seat:edit"))
            return SeatDAO.upsertSeats(seats);
        return false;
    }

    public static boolean removeSeatsByScreenId(int screenId, User user) {
        if (PermissionService.hasPermission(user, "seat:remove"))
            return SeatDAO.removeSeatsByScreenId(screenId);
        return false;
    }

    public static ArrayList<Seat> getSeatsByScreenId(int screenId, User user) {
        if (PermissionService.hasPermission(user, "seat:view"))
            return SeatDAO.getSeatsByScreenId(screenId);
        return new ArrayList<>();
    }

    public static ArrayList<SeatEditor> getSeatsStatusByScreeningId(int screeningId, User user) {
        return SeatDAO.getSeatsStatusByScreeningId(screeningId);
    }
}
