package com.moviebooking.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.api.common.CommonConstant;
import com.moviebooking.api.dto.AvailableSeatResponseDTO;
import com.moviebooking.api.dto.BookingRequestDTO;
import com.moviebooking.api.dto.BookingResponseDTO;
import com.moviebooking.api.exception.CompletePaymentForBookedSeats;
import com.moviebooking.api.exception.MaximumSeatsExceeded;
import com.moviebooking.api.exception.PaymentFailedException;
import com.moviebooking.api.exception.SeatAlreadyReserved;
import com.moviebooking.api.service.MovieBookingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-booking")
@Slf4j
@Validated
public class MovieBookingController {

	private final MovieBookingService movieBookingService;

	@RequestMapping(path = "/shows", method = RequestMethod.GET)
	public AvailableSeatResponseDTO getShowInfo(@RequestParam("showId") Long showId) {
		return movieBookingService.getShowInfo(showId);
	}

	@RequestMapping(path = "/book", method = RequestMethod.POST)
	public ResponseEntity<BookingResponseDTO> book(@Valid @RequestBody BookingRequestDTO bookingRequest,
			HttpServletRequest req) {

		Long userId = (Long) req.getAttribute(CommonConstant.USER_ID_ATTRIBUTE);

		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		try {
			BookingResponseDTO resp = movieBookingService.bookSeats(bookingRequest, userId);

			return ResponseEntity.ok(resp);
		} catch (SeatAlreadyReserved e) {
			log.error(e.getMessage());

			BookingResponseDTO resp = BookingResponseDTO.builder().showId(bookingRequest.getShowId()).build();

			return ResponseEntity.status(HttpStatus.LOCKED).body(resp);
		} catch (CompletePaymentForBookedSeats e) {

			log.error(e.getMessage());

			BookingResponseDTO resp = BookingResponseDTO.builder().showId(bookingRequest.getShowId()).build();

			return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
		} catch (MaximumSeatsExceeded e) {
			log.error(e.getMessage());

			BookingResponseDTO resp = BookingResponseDTO.builder().showId(bookingRequest.getShowId()).build();

			return ResponseEntity.status(HttpStatus.CONFLICT).body(resp);
		}
	}

	@RequestMapping(path = "/bookedSeats", method = RequestMethod.GET)
	public List<Long> getBookedSeats(HttpServletRequest req) {
		Long userId = (Long) req.getAttribute(CommonConstant.USER_ID_ATTRIBUTE);

		if(userId==null)
		{
			return null;
		}
		return movieBookingService.getBookedSeats(userId);
	}

	@RequestMapping(path = "/payment", method = RequestMethod.GET)
	public ResponseEntity<String> doPayment(@RequestParam("userId") Long userId,
			@RequestParam("status") boolean status) {
		try {
			movieBookingService.doPayment(userId, status);
		} catch (PaymentFailedException e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed");
		}

		return ResponseEntity.ok("Payment Successful!!");
	}
}
