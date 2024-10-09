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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveleasyApplication.class)
@AutoConfigureMockMvc
public class BookedRepositoryUnitTests {

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
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inizializzazione dell'utente
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
        booked.setUser(user);
        booked.setDate(new Date());
        booked.setBooking(booking);

        booked1 = new Booked();
        booked1.setUser(user);
        booked1.setDate(new Date(System.currentTimeMillis() - 1000000));
        booked1.setBooking(booking1);
    }

    @Test
    public void testFindAllByUser() throws Exception {
        Sort sort = Sort.by(Sort.Direction.ASC, "date");
        List<Booked> bookeds = new ArrayList<>();
        bookeds.add(booked);
        bookeds.add(booked1);

        when(bookedRepository.findAllByUser(user.getId(), sort)).thenReturn(Arrays.asList(booked, booked1));

        List<Booked> response = bookedRepository.findAllByUser(user.getId(), sort);
        System.out.println("Response from mock: " + response);
        System.out.println("Booked 1: " + booked);
        System.out.println("Booked 2: " + booked1);


        mockMvc.perform(get("/bookings/search/findAllByUser?user="+user.getId()+"&sort=date,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.bookings", hasSize(2))) // Verifica che ci siano 2 prenotazioni
                .andExpect(jsonPath("$._embedded.bookings[0].date").value(booked.getDate().toString())) // Verifica la data della prima prenotazione
                .andExpect(jsonPath("$._embedded.bookings[1].date").value(booked1.getDate().toString())) // Verifica la data della seconda prenotazione
                .andExpect(jsonPath("$._embedded.bookings[0].booking.bookingId").value("BKG123")) // Assicurati di avere l'ID della prenotazione
                .andExpect(jsonPath("$._embedded.bookings[0].booking.travelers[0].firstName").value("John")) // Verifica il nome del primo viaggiatore
                .andExpect(jsonPath("$._embedded.bookings[0].booking.flight.cityDeparture").value("Milan")); // Verifica la città di partenza
    }

    }

