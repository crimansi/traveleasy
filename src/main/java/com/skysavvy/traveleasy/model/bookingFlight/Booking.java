package com.skysavvy.traveleasy.model.bookingFlight;

import com.skysavvy.traveleasy.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Booking {
    @Id
    private String id;
    private String bookingId;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_traveler",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "traveler_id")
    )

    private List<TravelerEntity> travelers;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Flight flight;
}
