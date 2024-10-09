package com.skysavvy.traveleasy.payload.request;

import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.FlightPrice;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingRequest {
    private FlightPrice flightPrice;
    private FlightOrder.Traveler [] travelers;
}
