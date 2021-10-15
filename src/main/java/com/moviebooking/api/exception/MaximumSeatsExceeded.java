package com.moviebooking.api.exception;

public class MaximumSeatsExceeded extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1820373512601707880L;

	public MaximumSeatsExceeded(Long userId)
	{
		super("Max seats exceeded for "+userId);
	}
}
