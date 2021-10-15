package com.moviebooking.api.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seatId;
	private String seatNumber;
	
	@ManyToOne
	@JoinColumn(name="show_id",nullable = false)
	private Show show;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
    private Long version;
	
	private Instant lockUntil;
	
	private Boolean paymentStatus;
	
	private Double price;
}
