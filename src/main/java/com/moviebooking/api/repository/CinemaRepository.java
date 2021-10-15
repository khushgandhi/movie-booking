package com.moviebooking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.api.model.Cinema;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {

}
