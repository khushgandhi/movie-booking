package com.moviebooking.api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moviebooking.api.dto.AvailableSeatResponseDTO;
import com.moviebooking.api.dto.BookingRequestDTO;
import com.moviebooking.api.dto.BookingResponseDTO;
import com.moviebooking.api.dto.SeatDTO;
import com.moviebooking.api.exception.CompletePaymentForBookedSeats;
import com.moviebooking.api.exception.MaximumSeatsExceeded;
import com.moviebooking.api.exception.PaymentFailedException;
import com.moviebooking.api.exception.SeatAlreadyReserved;
import com.moviebooking.api.model.Seat;
import com.moviebooking.api.model.Show;
import com.moviebooking.api.repository.SeatRepository;
import com.moviebooking.api.repository.ShowRepository;
import com.moviebooking.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j

public class MovieBookingService {

	private final SeatRepository seatRepo;
	private final ShowRepository showRepo;
	private final UserRepository userRepo;

	// To Maintain Atomicity .so if any seat is locked rollback the whole
	// transaction and cancel all bookings.

	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
	public BookingResponseDTO bookSeats(BookingRequestDTO bookingRequest, Long userId)
			throws SeatAlreadyReserved, CompletePaymentForBookedSeats, MaximumSeatsExceeded {
		
		Long showId = bookingRequest.getShowId();
		List<SeatDTO> seats = bookingRequest.getSeats();
		List<Long> seatIds = new ArrayList<>();

		BookingResponseDTO bookingResponse = BookingResponseDTO.builder().showId(showId).build();

		if(seats.size()>6)
		{
			throw new MaximumSeatsExceeded(userId);
		}
		// check if user exist
		userRepo.findById(userId).orElseThrow(NoSuchElementException::new);

		// check if no pending booking for payment
		if (!CollectionUtils.isEmpty(this.getBookedSeats(userId))) {
			throw new CompletePaymentForBookedSeats(userId);
		}

		Instant now = Instant.now();
		Double total = 0.0;
		Instant lockPeriod = now.plus(2, ChronoUnit.MINUTES);
		for (SeatDTO requestedSeat : seats) {
			Optional<Seat> seatOptional = seatRepo.findById(requestedSeat.getSeatId());
			// check if seat is available
			if (seatOptional.isPresent() && !seatOptional.get().getPaymentStatus()
					&& (seatOptional.get().getLockUntil() == null || now.isAfter(seatOptional.get().getLockUntil()))) {
				Seat seat = seatOptional.get();
				// check if it is updated with read version.
				int bookStatus = seatRepo.bookSeat(requestedSeat.getSeatId(), requestedSeat.getVersion(), userId,
						lockPeriod);

				if (bookStatus == 0) {
					throw new SeatAlreadyReserved(requestedSeat.getSeatId());
				}

				total += seat.getPrice();
				seatIds.add(seat.getSeatId());
			} else {
				throw new SeatAlreadyReserved(requestedSeat.getSeatId());
			}

		}

		bookingResponse.setTotalPrice(total);
		bookingResponse.setSeatIds(seatIds);

		return bookingResponse;
	}

	public AvailableSeatResponseDTO getShowInfo(Long showId) {
		AvailableSeatResponseDTO availbleSeatResp = AvailableSeatResponseDTO.builder().build();
		Show show = showRepo.findById(showId).orElseThrow(NoSuchElementException::new);

		availbleSeatResp.setCinemaName(show.getCinema().getCinemaName());
		availbleSeatResp.setMovieName(show.getMovie().getMovieName());
		availbleSeatResp.setShowName(show.getShowName());

		availbleSeatResp.setStartTime(show.getStartTime());
		availbleSeatResp.setEndTime(show.getEndTime());
		Optional<List<Seat>> availbleSeats = this.seatRepo.getAvailbleSeats(showId, Instant.now());
		if (availbleSeats.isPresent()) {
			List<SeatDTO> seatDtos = new ArrayList<>();

			List<Seat> seats = availbleSeats.get();
			seats.forEach(seat -> {
				SeatDTO seatDto = SeatDTO.builder().build();
				seatDto.setPrice(seat.getPrice());
				seatDto.setSeatId(seat.getSeatId());
				seatDto.setVersion(seat.getVersion());

				seatDtos.add(seatDto);
			});

			availbleSeatResp.setSeats(seatDtos);
		}

		return availbleSeatResp;
	}

	public List<Long> getBookedSeats(Long userId) {
		Optional<List<Seat>> bookedSeatsOptional = this.seatRepo.getBookedSeats(userId, Instant.now());

		if (bookedSeatsOptional.isPresent()) {
			return bookedSeatsOptional.get().stream().map(seat -> seat.getSeatId()).collect(Collectors.toList());
		}

		return new ArrayList<>();

	}

	public void doPayment(Long userId, boolean status) throws PaymentFailedException {
		Optional<List<Seat>> bookedSeatsOptional = seatRepo.getBookedSeats(userId, Instant.now());

		if (status && bookedSeatsOptional.isPresent() && !CollectionUtils.isEmpty(bookedSeatsOptional.get())) {
			List<Seat> bookedSeats = bookedSeatsOptional.get();
			bookedSeats.forEach(bookedSeat -> {
				bookedSeat.setPaymentStatus(true);
				bookedSeat.setLockUntil(null);
			});

			seatRepo.saveAll(bookedSeats);
		} else {
			List<Seat> bookedSeats = bookedSeatsOptional.get();

			bookedSeats.forEach(bookedSeat -> {
				bookedSeat.setPaymentStatus(false);
				bookedSeat.setLockUntil(null);
				bookedSeat.setUser(null);
			});

			seatRepo.saveAll(bookedSeats);

			throw new PaymentFailedException(userId);
		}

	}
}
