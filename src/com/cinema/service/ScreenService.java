package com.cinema.service;

import com.cinema.model.Screen;
import com.cinema.model.dao.ScreenDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.util.ArrayList;

public class ScreenService {
    public static int addScreen(Screen screen) {
        if (!PermissionService.hasPermission("screen:add"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return ScreenDAO.addScreen(screen);
    }

    public static int getLatestScreenId() {
        return ScreenDAO.getLastScreenId();
    }

    public static ArrayList<Screen> getAllScreen() {
        if (!PermissionService.hasPermission("screen:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return ScreenDAO.getAllScreen();
    }
}
