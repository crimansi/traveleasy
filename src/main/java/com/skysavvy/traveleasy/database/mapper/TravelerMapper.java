package com.skysavvy.traveleasy.database.mapper;

import com.skysavvy.traveleasy.model.bookingFlight.TravelerEntity;
import com.amadeus.resources.FlightOrder.Traveler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TravelerMapper {
    TravelerMapper MAPPER = Mappers.getMapper(TravelerMapper.class);

    @Mapping(target = "firstName", source="name.firstName")
    @Mapping(target="lastName", source="name.lastName")
    TravelerEntity fromFlightOrderTravel(Traveler flightOrderTravel);

}
