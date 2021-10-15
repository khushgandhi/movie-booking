package com.moviebooking.api.exception;

public class CompletePaymentForBookedSeats extends Exception {

	private static final long serialVersionUID = 986682625986811460L;

	public CompletePaymentForBookedSeats(Long userId)
	{
		super("Complete payment for already booked seats for user Id "+userId);
	}
}
