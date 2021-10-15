package com.moviebooking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.api.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

	
}
