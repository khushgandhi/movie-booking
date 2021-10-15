package com.moviebooking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.api.model.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {

}
