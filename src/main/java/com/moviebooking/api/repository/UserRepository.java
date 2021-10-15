package com.moviebooking.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moviebooking.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
