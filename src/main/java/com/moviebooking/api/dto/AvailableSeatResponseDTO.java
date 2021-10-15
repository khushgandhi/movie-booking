package com.moviebooking.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvailableSeatResponseDTO {

	private List<SeatDTO> seats;
	private String movieName;
	private String cinemaName;
	private String showName;
	private Integer startTime;
	private Integer endTime;
}
