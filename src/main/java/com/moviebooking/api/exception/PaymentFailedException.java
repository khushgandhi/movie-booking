package com.moviebooking.api.exception;

public class PaymentFailedException extends Exception {

	private static final long serialVersionUID = -6110000040626018702L;

	public PaymentFailedException(Long userId)
	{
		super("Payment failed for userId "+userId);
	}
}
