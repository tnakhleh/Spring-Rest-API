package com.brightminds.assignment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.brightminds.assignment.dto.GeneralResultDTO;
import com.brightminds.assignment.exceptions.AccessValidationException;
import com.brightminds.assignment.exceptions.DataSourceException;
import com.brightminds.assignment.exceptions.RequestValidationException;
import com.brightminds.assignment.util.LoggerUtil;


@ControllerAdvice
public class APIExceptionHandler {

	@Autowired
	private LoggerUtil logger;
	
	@ExceptionHandler({ RequestValidationException.class })
	public  ResponseEntity<Object> handleRequestValidationExceptionn(RequestValidationException e) {
		logger.error(e.getMessage(), e);
		GeneralResultDTO result = new GeneralResultDTO("Error", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class, HttpMessageNotReadableException.class})
    public  ResponseEntity<Object> handleBadRequests(Exception e) {
    	logger.error(e.getMessage(), e);
    	GeneralResultDTO result = new GeneralResultDTO("Error", "Invalid Parameter");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }
    
    @ExceptionHandler({AuthenticationException.class, AccessValidationException.class})
    public  ResponseEntity<Object> handleFailedAuthRequests(Exception e) {
    	logger.error(e.getMessage(), e);
    	GeneralResultDTO result = new GeneralResultDTO("Error", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        
    }
    @ExceptionHandler({DataSourceException.class})
    public ResponseEntity<Object> handleDataSourceProblem(DataSourceException e) {
    	logger.error("Datasource Error: "+e.getMessage(),e);
    	GeneralResultDTO result = new GeneralResultDTO("Error", "Internal Server Error");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
    }
    
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(Exception e, WebRequest request) {
    	logger.error(e.getMessage(), e);
    	GeneralResultDTO result = new GeneralResultDTO("Error", "Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
    }
    
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleMethodException(Exception e, WebRequest request) {
    	logger.error(e.getMessage(), e);
    	GeneralResultDTO result = new GeneralResultDTO("Error", "Request Method Not allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }
    
    
    
}
