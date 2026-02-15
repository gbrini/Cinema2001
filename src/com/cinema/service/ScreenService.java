package com.cinema.service;

import com.cinema.model.Screen;
import com.cinema.model.User;
import com.cinema.model.dao.ScreenDAO;

import java.util.ArrayList;

public class ScreenService {
    public static int addScreen(Screen screen, User user) {
        if (PermissionService.hasPermission("screen:add"))
            return ScreenDAO.addScreen(screen);
        return -1;
    }

    public static int getLatestScreenId() {
        return ScreenDAO.getLastScreenId();
    }

    public static ArrayList<Screen> getAllScreen(User user) {
        if (PermissionService.hasPermission("screen:view"))
            return ScreenDAO.getAllScreen();
        return new ArrayList<>();
    }
}
