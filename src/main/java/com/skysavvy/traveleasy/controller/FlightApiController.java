package com.skysavvy.traveleasy.controller;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.*;
import com.google.gson.JsonArray;
import com.skysavvy.traveleasy.connection.AmadeusConnect;
import com.skysavvy.traveleasy.connection.DatabaseConnect;
import com.skysavvy.traveleasy.database.mapper.BookingMapper;
import com.skysavvy.traveleasy.database.service.BookingService;
import com.skysavvy.traveleasy.model.bookingFlight.Booking;
import com.skysavvy.traveleasy.payload.request.BookingRequest;
import com.skysavvy.traveleasy.model.TravelClass;
import com.skysavvy.traveleasy.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.servlet.function.ServerResponse.badRequest;

@RestController
@RequestMapping(value="/api")
public class FlightApiController {
    @Autowired
    BookingMapper bookingMapper;
    @Autowired
    BookingService bookingService;

    @GetMapping("/locations")
    public Location[] locations(@RequestParam(required=true) String keyword) throws ResponseException {
        return AmadeusConnect.INSTANCE.location(keyword);
    }

    @GetMapping("/flights")
    public FlightOfferSearch[] flights(@RequestParam String origin,
                                       @RequestParam String destination,
                                       @RequestParam String departDate,
                                       @RequestParam(required = false) String returnDate,
                                       @RequestParam Integer adults,
                                       @RequestParam(required = false, defaultValue = "0") Integer children,
                                       @RequestParam(required = false, defaultValue = "0") Integer infants,
                                       @RequestParam(required = false, defaultValue = "ANY") TravelClass travelClass,
                                       @RequestParam(required = false, defaultValue = "false") boolean nonStop)
            throws ResponseException {
        return AmadeusConnect.INSTANCE.flights(origin, destination, departDate, returnDate, adults,children, infants, travelClass, nonStop);
    }
    @PostMapping("/confirm")
    public FlightPrice confirm(@RequestBody(required=true) FlightOfferSearch search) throws ResponseException {
        return AmadeusConnect.INSTANCE.confirm(search);
    }

    @PostMapping("/traveler")
    public FlightOrder.Traveler[] traveler(@RequestBody
    JsonArray travelerInfo) {
        return DatabaseConnect.traveler(travelerInfo);
    }
    @PostMapping("/booking")
    public ResponseEntity<?> booking(@RequestHeader("Authorization") String tokenUser,
                                     @RequestBody BookingRequest request) {
        FlightPrice flightPrice = request.getFlightPrice();
        FlightOrder.Traveler[] travelers = request.getTravelers();
        try {
            FlightOrder flightOrder = AmadeusConnect.INSTANCE.booking(flightPrice, travelers);
            Booking booking = bookingMapper.mapBooking(flightOrder);
            bookingService.saveBooking(booking, tokenUser);
            return ResponseEntity.ok(new MessageResponse("Booking saved"));

        } catch (ResponseException e) {
            return ResponseEntity.badRequest().body("Sorry! We couldn't book flight");
        }
    }

}
