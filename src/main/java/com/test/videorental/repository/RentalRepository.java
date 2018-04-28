package com.test.videorental.repository;

import com.test.videorental.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("SELECT r FROM Rental r where r.movie.id = :movieId and r.returnDate=null")
    Rental findActiveRental(@Param("movieId") Long movieId);

    @Query("SELECT r FROM Rental r where r.customer.id=:customerId and r.movie.id = :movieId and r.returnDate=null")
    Rental findActiveRental(@Param("customerId") Long customerId, @Param("movieId") Long movieId);
}
