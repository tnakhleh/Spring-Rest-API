package com.brightminds.assignment.services.impl;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import com.brightminds.assignment.dto.GeneralResultDTO;
import com.brightminds.assignment.dto.LoginRequestDTO;
import com.brightminds.assignment.dto.LoginResponseDTO;
import com.brightminds.assignment.dto.StatementDTO;
import com.brightminds.assignment.exceptions.AccessValidationException;
import com.brightminds.assignment.services.ISecurityService;
import com.brightminds.assignment.util.LoggerUtil;
import com.brightminds.assignment.util.TokenUtil;

@Service
public class SecurityService implements ISecurityService {
	
	@Autowired
	private LoggerUtil logger;
	@Autowired
	private UserDetailsService customUserDetailsService;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public boolean checkValidAccess(StatementDTO statementParams) {
		logger.debug("Check security access role");
		// Apply the policy: When the test user tries to specify any parameter, then
		// HTTP unauthorized (401) access error will be sent
		if (this.isCurrentUserInRole("ROLE_USER") && statementParams != null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isCurrentUserInRole(String role) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
	}
	
	@Override
	public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) throws Exception {
		logger.debug(loginRequest.getUsername());
	    try{
	    	logger.debug("Logged in Password "+ loginRequest.getPassword());
	    	
	    	if(!this.checkUserToken(loginRequest.getUsername())) {
	    		tokenUtil.deleteToken(loginRequest.getUsername());	
		    	UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		    	logger.debug("Logged in Password token  "+authenticationToken.getCredentials()); 
		        authenticationManager.authenticate(authenticationToken);
	    	}else {
	    		throw new AccessValidationException("User already logged in");
	    	}
	    }catch (BadCredentialsException e) {
	    	logger.error("Error Occured: BadCredentialsException", e);
	        throw new AccessValidationException("Invalid Credentials");
	    }catch (AccessValidationException e) {
	        throw new AccessValidationException(e.getMessage());
	    }catch (Exception e) {
	    	logger.error("Error Occured when trying to authenticat user", e);
	    	throw new AccessValidationException("Error Occured when trying to authenticat user");
	    }

	    UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getUsername());

	    String token = tokenUtil.generateToken(userDetails);
	    return new LoginResponseDTO(token);
	}
	
	@Override
	public GeneralResultDTO logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			if(tokenUtil.isUserLoggedIn(auth.getName())) {
				tokenUtil.deleteToken(auth.getName());	
			}
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}else {
			throw new AccessValidationException("Error Occured when trying to logout user");
		}
		
		return new GeneralResultDTO("Success","Logged Out Successfully");
	}
	
	private boolean checkUserToken(String userName) {
		return tokenUtil.isUserLoggedIn(userName) && !tokenUtil.isTokenExpired(tokenUtil.getUserSavedToken(userName));
    }

}
