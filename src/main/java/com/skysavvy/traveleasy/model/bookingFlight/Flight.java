package com.skysavvy.traveleasy.model.bookingFlight;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long flightId;
    private String id;
    @Column(name="city-departure", nullable=false)
    private String cityDeparture;
    @Column(name="city-arrival", nullable=false)
    private String cityArrival;
    @Column(name="departure-date_time", nullable=false)
    private String departureTime;
    @Column(name="arrival-date_time", nullable=false)
    private String arrivalTime;
    @Column(name="city-departure-return")
    private String cityDepartureReturn;
    @Column(name="city-arrival-return")
    private String cityArrivalReturn;
    @Column(name="departure-return-date_time")
    private String departureTimeReturn;
    @Column(name="arrival-return-date_time")
    private String arrivalTimeReturn;
    private String duration;
    private String durationReturn;
    private String airline;
    private String airlineReturn;
    private String travelClass;
}
