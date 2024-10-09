package com.skysavvy.traveleasy.connection;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.referencedata.Locations;
import com.amadeus.resources.*;
import com.amadeus.exceptions.ResponseException;
import com.google.gson.JsonObject;
import com.skysavvy.traveleasy.model.TravelClass;

public enum AmadeusConnect {
    INSTANCE;
    private Amadeus amadeus;
    private AmadeusConnect() {
        this.amadeus = Amadeus
                .builder("iVrnCAeGoPW0v7Fq0uRQitg2Ezfvw0tc", "lqEsDMURa1jbvFhC")
                .build();
    }
    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
                .with("keyword", keyword)
                .and("subType", Locations.ANY));
    }
    public FlightOfferSearch[] flights(String origin, String destination, String departDate, String returnDate, Integer adults, Integer children, Integer infants, TravelClass travelClass, boolean nonStop) throws ResponseException {
        Params params = Params.with("originLocationCode", origin)
                .and("destinationLocationCode", destination)
                .and("departureDate", departDate)
                .and("adults", adults)
                .and("children", children)
                .and("infants", infants)
                .and("nonStop", nonStop)
                .and("currencyCode", "EUR")
                .and("max", 32);
        if (travelClass != TravelClass.ANY) {
            params = params.and("travelClass", travelClass.toString());
        }
        if (returnDate != null) {
            params = params.and("returnDate", returnDate);
        }

        return amadeus.shopping.flightOffersSearch.get(params);
    }

    public FlightPrice confirm(FlightOfferSearch offer) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.pricing.post(offer);
    }
    public FlightOrder booking(FlightPrice price, FlightOrder.Traveler[] travels) throws ResponseException{
        return amadeus.booking.flightOrders.post(price, travels);
    }
    public Hotel[] hotels(String cityCode) throws ResponseException {
        return amadeus.referenceData.locations.hotels.byCity.get(
                Params.with("cityCode", cityCode));
    }
    public HotelOfferSearch[] hotelOffers(String hotelIds) throws ResponseException {
        return amadeus.shopping.hotelOffersSearch.get(Params.with("hotelIds", hotelIds));
    }
    public HotelBooking[] hotelBooking(JsonObject body) throws ResponseException {
        return amadeus.booking.hotelBookings.post(body);
    }
}

