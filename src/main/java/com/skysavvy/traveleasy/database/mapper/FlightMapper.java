package com.skysavvy.traveleasy.database.mapper;

import com.amadeus.resources.FlightOfferSearch;
import com.skysavvy.traveleasy.model.bookingFlight.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    FlightMapper MAPPER = Mappers.getMapper(FlightMapper.class);

    @Mapping(target="cityDeparture", expression="java(cityDeparture(order))")
    @Mapping(target="cityArrival", expression="java(cityArrival(order))")
    @Mapping(target="departureTime", expression = "java(departureTime(order))")
    @Mapping(target="arrivalTime", expression = "java(arrivalTime(order))")
    @Mapping(target="cityDepartureReturn", expression = "java(cityDepartureReturn(order))")
    @Mapping(target="cityArrivalReturn", expression = "java(cityArrivalReturn(order))")
    @Mapping(target="departureTimeReturn", expression = "java(timeDepartureReturn(order))")
    @Mapping(target="arrivalTimeReturn", expression = "java(timeArrivalReturn(order))")
    @Mapping(target="duration", expression = "java(duration(order))")
    @Mapping(target="durationReturn", expression = "java(durationReturn(order))")
    @Mapping(target="airline", expression = "java(airline(order))")
    @Mapping(target="airlineReturn", expression = "java(airlineReturn(order))")
    @Mapping(target="travelClass", expression = "java(travelClass(order))")
    Flight fromFlightOrder(FlightOfferSearch order);


    default String cityDeparture(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[0].getDeparture().getIataCode();
    }

    default String cityArrival(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[flight.getItineraries()[0].getSegments().length - 1].getArrival().getIataCode();
    }
    default String departureTime(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[0].getDeparture().getAt();
    }
    default String arrivalTime(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[flight.getItineraries()[0].getSegments().length - 1].getArrival().getAt();
    }
    default String cityDepartureReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1)
            return flight.getItineraries()[1].getSegments()[0].getDeparture().getIataCode();
        return null;
    }
    default String cityArrivalReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1)
            return flight.getItineraries()[1].getSegments()[flight.getItineraries()[0].getSegments().length - 1].getArrival().getIataCode();
         return null;
    }
    default String timeDepartureReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1)
            return flight.getItineraries()[1].getSegments()[0].getDeparture().getAt();
        return null;
    }
    default String timeArrivalReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1)
            return flight.getItineraries()[1].getSegments()[flight.getItineraries()[0].getSegments().length - 1].getArrival().getAt();
        return null;
    }
    default String duration(FlightOfferSearch flight) {
        if(flight.getItineraries()[0].getDuration() != null)
            return flight.getItineraries()[0].getDuration();
        else
            return flight.getItineraries()[0].getSegments()[0].getDuration();
    }
    default String durationReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1){
            if(flight.getItineraries()[1].getDuration() != null)
                return flight.getItineraries()[1].getDuration();
            else
                return flight.getItineraries()[1].getSegments()[0].getDuration();
        }
        return null;
    }
    default String airline(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[0].getCarrierCode();
    }
    default String airlineReturn(FlightOfferSearch flight) {
        if(flight.getItineraries().length > 1)
            return flight.getItineraries()[1].getSegments()[0].getCarrierCode();
        return null;
    }
    default String travelClass(FlightOfferSearch flight) {
        return flight.getItineraries()[0].getSegments()[0].getCo2Emissions()[0].getCabin();
    }

}
