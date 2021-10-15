package com.moviebooking.api.dto;

import java.util.List;

import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequestDTO {
	
	@NotNull
	private Long showId;
	@NotNull
	private List<SeatDTO> seats;

}
