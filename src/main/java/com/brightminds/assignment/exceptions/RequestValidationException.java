package com.brightminds.assignment.exceptions;

public class RequestValidationException extends RuntimeException  {

	private static final long serialVersionUID = 9128755487240654333L;

	public RequestValidationException(String message) {
        super("Invalid Request parameter: "+message);
    }
}