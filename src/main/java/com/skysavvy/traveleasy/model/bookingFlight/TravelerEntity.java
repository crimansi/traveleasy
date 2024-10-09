package com.skysavvy.traveleasy.model.bookingFlight;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="travelers")
public class TravelerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long travelerId;
    @Column(name="first-name", nullable=false)
    private String firstName;
    @Column(name="last-name", nullable=false)
    private String lastName;
    @Column(name="date-of-birth", nullable=false)
    private String dateOfBirth;
    @Column(nullable=false)
    private String gender;
}
