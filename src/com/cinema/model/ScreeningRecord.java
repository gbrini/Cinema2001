package com.cinema.model;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningRecord(Screening screening, Movie movie, LocalDate date, LocalTime slot, Screen screen, int tickets_sold, int seats_remaining) {}
