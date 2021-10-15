package com.moviebooking.api.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moviebooking.api.model.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {

	@Query(value = "update seat set user_id= :userId , version = :version + 1 , lock_until = :lockUntil where seat_id=:seatId and version = :version ",nativeQuery = true)
	@Modifying()
	int bookSeat(@Param("seatId") Long seatId,@Param("version") Long version,@Param("userId") Long userId,@Param("lockUntil")Instant lockUntil);
	
	@Query(value = "select s from Seat s where s.show.showId = :showId and (s.paymentStatus<>true) and (s.lockUntil is null or s.lockUntil < :currentTime)")
	Optional<List<Seat>> getAvailbleSeats(@Param("showId") Long showId,@Param("currentTime") Instant currentTime); 
	
	@Query(value = "select s from Seat s where s.user.userId = :userId and s.paymentStatus<>1 and s.lockUntil is not null and s.lockUntil > :currentTime")
	Optional<List<Seat>> getBookedSeats(@Param("userId") Long userId,@Param("currentTime") Instant currentTime);
}
