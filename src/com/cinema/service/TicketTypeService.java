package com.cinema.service;

import com.cinema.model.TicketType;
import com.cinema.model.dao.TicketTypeDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.util.ArrayList;

public class TicketTypeService {
    public static ArrayList<TicketType> getTicketTypes() {
        if (!PermissionService.hasPermission("ticket:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        return TicketTypeDAO.getTicketTypes();
    }

    public static TicketType getTicketTypeById(int typeId) {
        if (!PermissionService.hasPermission("ticket:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");

        return TicketTypeDAO.getTicketTypeById(typeId);
    }
}
