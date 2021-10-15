package com.moviebooking.api.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookingResponseDTO {

	 private Double totalPrice;
	 private Long showId;
	 private List<Long> seatIds;
	 
}
