package com.cinema.util;

import java.time.LocalTime;

public class TimeSlot {
    public static final LocalTime[] SLOTS = {
        LocalTime.of(10, 0),
        LocalTime.of(12, 10),
        LocalTime.of(15, 0),
        LocalTime.of(16, 30),
        LocalTime.of(20, 20),
        LocalTime.of(22, 0),
    };

    public static String format(LocalTime time) {
        return time.toString();
    }
}
