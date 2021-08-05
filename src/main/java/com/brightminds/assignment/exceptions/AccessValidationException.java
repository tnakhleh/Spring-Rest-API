package com.brightminds.assignment.exceptions;

public class AccessValidationException extends RuntimeException  {

	private static final long serialVersionUID = -4730379620759802204L;

	public AccessValidationException(String message) {
        super(message);
    }
}