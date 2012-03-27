package com.hrycan.prime.exception;

public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException() {
	}
	
	public DataNotFoundException(String message) {
		super(message);
	}
	
	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);		
	}

}