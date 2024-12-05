package com.skysavvy.traveleasy.controller;

import com.amadeus.exceptions.ResponseException;
import com.skysavvy.traveleasy.database.service.BookingService;
import com.skysavvy.traveleasy.model.bookingFlight.Booked;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/myBookings")
@Tag(name = "Booking API")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @DeleteMapping("/{id}")
    public void deleteBooking(@RequestHeader("Authorization") String tokenUser,
                              @PathVariable(value = "id") Long bookingId) throws ResponseException {
        bookingService.deleteBooking(bookingId);
    }

}
