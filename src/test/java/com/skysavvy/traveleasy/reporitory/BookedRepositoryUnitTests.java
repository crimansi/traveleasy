package com.skysavvy.traveleasy.reporitory;

import com.skysavvy.traveleasy.TraveleasyApplication;
import com.skysavvy.traveleasy.database.repository.BookedRepository;
import com.skysavvy.traveleasy.model.bookingFlight.Booked;
import com.skysavvy.traveleasy.model.bookingFlight.Booking;
import com.skysavvy.traveleasy.model.bookingFlight.Flight;
import com.skysavvy.traveleasy.model.bookingFlight.TravelerEntity;
import com.skysavvy.traveleasy.model.user.Role;
import com.skysavvy.traveleasy.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveleasyApplication.class)
@AutoConfigureMockMvc
public class BookedRepositoryUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BookedRepository bookedRepository;

    private User user;
    private Booked booked;
    private Booked booked1;
    private Booking booking;
    private Booking booking1;
    private Flight flight;
    private Flight flight1;
    private TravelerEntity traveler;
    private TravelerEntity traveler1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("firstname", "lastname", "username", "user@example.com", "password123", Role.USER);
        user.setId(10L);

        // Inizializzazione del primo Booking e collegamento a una lista di travelers e flight
        booking = new Booking();
        booking.setId("BKG123");
        booking.setBookingId("BKG123ID");

        flight = new Flight();
        flight.setFlightId(20L);
        flight.setId("FL123");
        flight.setCityDeparture("Milan");
        flight.setCityArrival("Rome");
        booking.setFlight(flight);  // Collega il volo alla prenotazione

        traveler = new TravelerEntity();
        traveler.setTravelerId(50L);
        traveler.setFirstName("John");
        traveler.setLastName("Doe");
        List<TravelerEntity> travelers = new ArrayList<>();
        travelers.add(traveler);
        booking.setTravelers(travelers);

        booking1 = new Booking();
        booking1.setId("BKG124");
        booking1.setBookingId("BKG124ID");

        flight1 = new Flight();
        flight1.setFlightId(70L);
        flight1.setId("FL124");
        flight1.setCityDeparture("Rome");
        flight1.setCityArrival("Milan");
        booking1.setFlight(flight1);

        traveler1 = new TravelerEntity();
        traveler1.setTravelerId(100L);
        traveler1.setFirstName("Jane");
        traveler1.setLastName("Smith");
        List<TravelerEntity> travelers1 = new ArrayList<>();
        travelers1.add(traveler1);
        booking1.setTravelers(travelers1);

        booked = new Booked();
        booked.setId(60L);
        booked.setUser(user);
        booked.setDate(new Date());
        booked.setBooking(booking);

        booked1 = new Booked();
        booked.setId(90L);
        booked1.setUser(user);
        booked1.setDate(new Date(System.currentTimeMillis() - 1000000));
        booked1.setBooking(booking1);
    }

    @Test
    public void testFindAllByUser() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");



        // Simula il comportamento del repository
        when(bookedRepository.findAllByUser(user.getId(), sort)).thenReturn(Arrays.asList(booked, booked1));
        when(bookedRepository.findById(60L)).thenReturn(Optional.of(booked));
        when(bookedRepository.findById(90L)).thenReturn(Optional.of(booked1));

        List<Booked> bookeds = bookedRepository.findAllByUser(user.getId(), sort);

        Optional<Booked> result1 = bookedRepository.findById(60L);
        Optional<Booked> result2 = bookedRepository.findById(90L);

        assertFalse(bookeds.isEmpty());
        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());

        verify(bookedRepository).findAllByUser(user.getId(), sort);
        verify(bookedRepository).findById(60L);
        verify(bookedRepository).findById(90L);

        // Verifica l'endpoint
        mockMvc.perform(get("/bookings/search/findAllByUser?user=" + user.getId() + "&sort=date,asc"))
                .andExpect(status().isOk());
    }
}

