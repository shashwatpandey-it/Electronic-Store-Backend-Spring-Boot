package com.shashwat.electronicstorebackend.exceptions;

public class BadApiRequestException extends RuntimeException{

	public BadApiRequestException() {
		super();
	}
	
	public BadApiRequestException(String message) {
		super(message);
	}
}
