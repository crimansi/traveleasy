package com.skysavvy.traveleasy.database.mapper;

import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.FlightOrder.Traveler;
import com.skysavvy.traveleasy.model.bookingFlight.Booking;
import com.skysavvy.traveleasy.model.bookingFlight.Flight;
import com.skysavvy.traveleasy.model.bookingFlight.TravelerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {TravelerMapper.class, FlightMapper.class}, componentModel = "spring")
public interface BookingMapper {
    BookingMapper MAPPER = Mappers.getMapper(BookingMapper.class);

    @Mapping(target="bookingId", source = "booking.queuingOfficeId")
    @Mapping(target="flight", expression = "java(flightOfferSearch(booking))")
    @Mapping(target="travelers", expression = "java(traveler(booking))")
    Booking mapBooking(FlightOrder booking);

    default Flight flightOfferSearch(FlightOrder booking) {
        return FlightMapper.MAPPER.fromFlightOrder(booking.getFlightOffers()[0]);
    }
    default List<TravelerEntity> traveler(FlightOrder booking) {
        List<TravelerEntity> travelers = new ArrayList<>();
        for(Traveler trav: booking.getTravelers()) {
            travelers.add(TravelerMapper.MAPPER.fromFlightOrderTravel(trav));
        }
        return travelers;
    }
}
