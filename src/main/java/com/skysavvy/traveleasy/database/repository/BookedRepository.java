package com.skysavvy.traveleasy.database.repository;

import com.skysavvy.traveleasy.model.bookingFlight.Booked;
import com.skysavvy.traveleasy.model.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Tag(name = "Booked API")
@RepositoryRestResource(path = "bookings", collectionResourceRel = "bookings")
public interface BookedRepository extends JpaRepository<Booked, Long> {

    // Questo sarà accessibile via: GET /bookings/search/findAllByUser?user=userId
    @RestResource(path = "findAllByUser", rel = "findAllByUser")
    @Query(value = "SELECT b FROM Booked b WHERE b.user.id = :user")
    List<Booked> findAllByUser(@Param("user") Long user, Sort sort);

    @RestResource(exported = false)
    void saveBooked(Booked booked);


    @RestResource(path= "findAllFromRangeByUser", rel ="findAllFromRangeByUser")
    @Query(value = "SELECT b FROM Booked b WHERE b.date >= :range AND b.user.id = :user")
    List<Booked> findAllFromRangeByUserId(@Param("range")
                                        @DateTimeFormat(pattern= "yyyy-MM-dd")Date range, @Param("user") Long user, Sort sort);
}
