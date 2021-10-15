package com.moviebooking.api.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="movie_show")
public class Show {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long showId;
	
	private String showName;
	
	private Integer startTime;
	
	private Integer endTime;
	
	@ManyToOne
	@JoinColumn(name = "cinema_id",nullable=false)
	private Cinema cinema;
	
	@ManyToOne
	@JoinColumn(name="movie_id",nullable=false)
	private Movie movie;
	
	@OneToMany(mappedBy = "show")
	private List<Seat> seats;
	
	private Long version;
	
	private Instant lockUntil;
}
