package com.moviebooking.api.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SeatDTO {

	private Long seatId;
	private Long version;
	private Double price;
}
