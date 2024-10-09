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


   /*@Operation(summary = "Get a list of flight bookings", description = "Return a list of Booked")
    @GetMapping
    public List<Booked> getBookings(@RequestHeader("Authorization") String tokenUser,
                                    @Parameter(description = "Order of bookings", example = "ASC", required = true) @RequestParam String sort) throws ResponseException {
        return bookingService. getAllPublicBookings(tokenUser, sort);
    }*/

    @DeleteMapping("/{id}")
    public void deleteBooking(@RequestHeader("Authorization") String tokenUser,
                              @PathVariable(value = "id") Long bookingId) throws ResponseException {
        bookingService.deleteBooking(bookingId);
    }

    /*@GetMapping("/range")
    public List <Booked> getBookingsInRange(@RequestHeader("Authorization") String tokenUser,
                                            @RequestParam String range,
                                            @RequestParam String sort) throws ResponseException {
        return bookingService.getBookingsFromRange(tokenUser, range, sort);
    }*/


}
