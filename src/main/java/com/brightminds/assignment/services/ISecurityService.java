package com.brightminds.assignment.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.brightminds.assignment.dto.GeneralResultDTO;
import com.brightminds.assignment.dto.LoginRequestDTO;
import com.brightminds.assignment.dto.LoginResponseDTO;
import com.brightminds.assignment.dto.StatementDTO;

public interface ISecurityService {

	boolean checkValidAccess(StatementDTO statementParams);

	boolean isCurrentUserInRole(String role);
	
	LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) throws Exception;
	
	public GeneralResultDTO logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception;

}