package com.cinema.service;

import com.cinema.model.Movie;
import com.cinema.model.Screening;
import com.cinema.model.ScreeningRecord;
import com.cinema.model.dao.MovieDAO;
import com.cinema.model.dao.ScreenDAO;
import com.cinema.model.dao.ScreeningDAO;
import com.cinema.util.UnauthorizedAccessException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScreeningService {
    public static boolean validateAndSchedule(ScreeningRecord screeningRecord) {
        if (!PermissionService.hasPermission("screening:add")) {
            throw new UnauthorizedAccessException("Accesso non consentito");
        }

        if (MovieDAO.getMovieById(screeningRecord.movie().getMovieId()) == null)
            return false;

        ArrayList<Screening> screenings = ScreeningDAO.getScreeningByDateAndScreen(screeningRecord.screening().getStartTimeDate(), screeningRecord.screening().getScreenId());
        ZonedDateTime zoned = screeningRecord.screening().getLocalStartTime();
        LocalDateTime proposedStart = LocalDateTime.of(zoned.toLocalDate(), zoned.toLocalTime());
        LocalDateTime proposedEnd = proposedStart.plusMinutes(screeningRecord.movie().getDurationMinutes() + 15);

        for (Screening scheduled: screenings) {
            Movie scheduledMovie = MovieService.getMovieById(scheduled.getMovieId());

            if (scheduledMovie == null)
                continue;

            int scheduledMinutes = scheduledMovie.getDurationMinutes() + 15;
            ZonedDateTime zonedDateTime = scheduled.getLocalStartTime();

            LocalDateTime existingStart = LocalDateTime.of(zonedDateTime.toLocalDate(), zonedDateTime.toLocalTime());
            LocalDateTime existingEnd = existingStart.plusMinutes(scheduledMinutes);

            boolean conflict = proposedStart.isBefore(existingEnd) && proposedEnd.isAfter(existingStart);

            if (conflict) {
                return false;
            }
        }

        return ScreeningDAO.addScreening(screeningRecord.screening());
    }

    public static ArrayList<Screening> getScreeningByDateAndScreen(LocalDate date, int screenId) {
        if (!PermissionService.hasPermission("screening:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return ScreeningDAO.getScreeningByDateAndScreen(date, screenId);
    }

    public static HashMap<LocalDate, ArrayList<ScreeningRecord>> getScreeningByDateRange(Date from, Date to) {
        if (!PermissionService.hasPermission("screening:view"))
            throw new UnauthorizedAccessException("Accesso non consentito");
        return ScreeningDAO.getScreeningByDateRange(from, to);
    }
}
