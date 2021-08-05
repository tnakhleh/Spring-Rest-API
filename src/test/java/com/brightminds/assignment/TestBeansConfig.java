package com.brightminds.assignment;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.brightminds.assignment.repositories.dbaccess.DBAccessAdapter;
import com.brightminds.assignment.services.IStatementsService;
import com.brightminds.assignment.services.impl.CustomUserDetailsService;
import com.brightminds.assignment.services.impl.SecurityService;
import com.brightminds.assignment.services.impl.StatementsService;
import com.brightminds.assignment.util.LoggerUtil;
import com.brightminds.assignment.util.TokenUtil;

@TestConfiguration
public class TestBeansConfig {
	@Bean
	public SecurityService securityService() {
		return new SecurityService();
	}
	@Bean
	public IStatementsService statementsService() {
		return new StatementsService();
	}
	
	@Bean
	public CustomUserDetailsService customUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public TokenUtil tokenUtil() {
		return new TokenUtil();
	}
	
	@Bean
	public DBAccessAdapter dBAccessAdapter() {
		return new DBAccessAdapter();
	}
	
	@Bean
	public LoggerUtil loggerUtil() {
		return new LoggerUtil();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
