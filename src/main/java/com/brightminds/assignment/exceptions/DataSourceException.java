package com.brightminds.assignment.exceptions;

public class DataSourceException extends RuntimeException{
	private static final long serialVersionUID = -3710037160452591687L;

	public DataSourceException(String message) {
        super("Data source problem: "+message);
    }
}
