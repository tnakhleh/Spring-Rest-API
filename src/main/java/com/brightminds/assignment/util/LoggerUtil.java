package com.brightminds.assignment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.brightminds.assignment.services.impl.SecurityService;

@Component
public class LoggerUtil {
	private Logger logger = LoggerFactory.getLogger(SecurityService.class);
	
	public void trace(String message) {
		logger.trace(message);
	}

	public void info(String message) {
		logger.info(message);
	}

	public void debug(String message) {
		logger.debug(message);
	}

	public void warn(String message) {
		logger.warn(message);
	}

	public void error(String message) {
		logger.error(message);
	}
	
	public void error(String message, Exception e) {
		logger.error(message,e);
	}

}
