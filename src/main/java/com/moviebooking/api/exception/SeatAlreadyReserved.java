package com.moviebooking.api.exception;

public class SeatAlreadyReserved extends Exception {

	private static final long serialVersionUID = -9096917899666967291L;
	
	
	public SeatAlreadyReserved(Long seatId)
	{
		super("SeatId " +seatId+" is Locked");
	}
}
