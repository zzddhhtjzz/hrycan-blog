package com.hrycan.prime.exception;

public class StaleDataException extends RuntimeException {
	public StaleDataException() {
	}
	
	public StaleDataException(String message) {
		super(message);
	}
	
	public StaleDataException(String message, Throwable cause) {
		super(message, cause);		
	}

}
