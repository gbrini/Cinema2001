package test.com.cinema.util;

import com.cinema.model.*;
import com.cinema.util.TicketFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TicketFactory — White Box")
class TicketFactoryTest {
    private Screening screening;
    private Seat seat;
    private User user;

    @BeforeEach
    void setUp() {
        screening = new Screening(1, 1, 1,
                LocalDateTime.now().plusHours(2), 12.0f, false);
        seat = new Seat(1, 1, "A", 1, false, false, true);
        Role role = new Role(3, "client");
        user = new User(1, "Mario", "Rossi", LocalDate.of(199, 11, 10), "mario@test.it", role);
    }

    @Test
    @DisplayName("Biglietto Intero: nessuno sconto, nessun addendum → prezzo base invariato")
    void testPrezzoIntero() {
        TicketType intero = new TicketType(1, "Intero", 0f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, intero, seat, user);
        assertEquals(12.0f, ticket.getFinalPrice(), 0.01f);
    }

    @Test
    @DisplayName("Biglietto Ridotto: sconto 20% → 9.60€")
    void testPrezzoRidotto() {
        TicketType ridotto = new TicketType(2, "Ridotto", 20f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, ridotto, seat, user);
        // 12 - (12 * 20 / 100) = 9.6
        assertEquals(9.6f, ticket.getFinalPrice(), 0.01f);
    }

    @Test
    @DisplayName("Biglietto Anziani: sconto 30% → 8.40€")
    void testPrezzoAnziani() {
        TicketType anziani = new TicketType(3, "Anziani", 30f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, anziani, seat, user);
        // 12 - (12 * 30 / 100) = 8.4
        assertEquals(8.4f, ticket.getFinalPrice(), 0.01f);
    }

    @Test
    @DisplayName("Biglietto VIP: nessuno sconto + sovrapprezzo 5€ → 17.00€")
    void testPrezzoVip() {
        TicketType vip = new TicketType(4, "VIP", 0f, 5f);
        Ticket ticket = TicketFactory.createTicket(screening, vip, seat, user);
        // 12 + 5 = 17
        assertEquals(17.0f, ticket.getFinalPrice(), 0.01f);
    }

    @Test
    @DisplayName("Sconto e addendum combinati → prezzo corretto")
    void testPrezzoScontoEAddendum() {
        TicketType tipo = new TicketType(5, "Special", 20f, 2f);
        Ticket ticket = TicketFactory.createTicket(screening, tipo, seat, user);
        // 12 - (12 * 20 / 100) + 2 = 11.6
        assertEquals(11.6f, ticket.getFinalPrice(), 0.01f);
    }

    @ParameterizedTest
    @DisplayName("Prezzo base variabile con sconto fisso 20%")
    @CsvSource({"10.0, 8.0", "15.0, 12.0", "20.0, 16.0"})
    void testPrezzoBaseVariabile(float basePrice, float expectedPrice) {
        Screening s = new Screening(1, 1, 1,
                LocalDateTime.now().plusHours(2), basePrice, false);
        TicketType ridotto = new TicketType(2, "Ridotto", 20f, 0f);
        Ticket ticket = TicketFactory.createTicket(s, ridotto, seat, user);
        assertEquals(expectedPrice, ticket.getFinalPrice(), 0.01f);
    }

    @Test
    @DisplayName("Il ticket ha il corretto screeningId")
    void testTicketScreeningId() {
        TicketType intero = new TicketType(1, "Intero", 0f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, intero, seat, user);
        assertEquals(screening.getScreeningId(), ticket.getScreeningId());
    }

    @Test
    @DisplayName("Il ticket ha il corretto seatId")
    void testTicketSeatId() {
        TicketType intero = new TicketType(1, "Intero", 0f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, intero, seat, user);
        assertEquals(seat.getSeatId(), ticket.getSeatId());
    }

    @Test
    @DisplayName("Il ticket ha il corretto userId")
    void testTicketUserId() {
        TicketType intero = new TicketType(1, "Intero", 0f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, intero, seat, user);
        assertEquals(user.getUserId(), ticket.getUserId());
    }

    @Test
    @DisplayName("purchaseTime non è null")
    void testPurchaseTimeNonNull() {
        TicketType intero = new TicketType(1, "Intero", 0f, 0f);
        Ticket ticket = TicketFactory.createTicket(screening, intero, seat, user);
        assertNotNull(ticket.getPurchaseTime());
    }
}