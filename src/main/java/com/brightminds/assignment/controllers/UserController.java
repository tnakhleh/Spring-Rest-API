package com.brightminds.assignment.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.brightminds.assignment.dto.GeneralResultDTO;
import com.brightminds.assignment.dto.LoginRequestDTO;
import com.brightminds.assignment.dto.LoginResponseDTO;
import com.brightminds.assignment.services.ISecurityService;
import com.brightminds.assignment.util.LoggerUtil;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private LoggerUtil logger;
	@Autowired
	private ISecurityService securityService;
	
	@PostMapping("/login")
	public LoginResponseDTO loginUser(@RequestBody LoginRequestDTO loginRequest) throws Exception {
		return securityService.authenticateUser(loginRequest);
	}
	
	@GetMapping("/logout")
	public GeneralResultDTO logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return securityService.logoutUser(request, response);
	}
}
