package com.brightminds.assignment.security.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.brightminds.assignment.dto.GeneralResultDTO;
import com.brightminds.assignment.services.impl.CustomUserDetailsService;
import com.brightminds.assignment.util.LoggerUtil;
import com.brightminds.assignment.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ValidateAccessTokenFilter extends OncePerRequestFilter{
	@Autowired
	private LoggerUtil logger;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
		    String authorizationHeader = request.getHeader("Authorization");
		    String username = null;
		    String token = null;

		    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		    	token = authorizationHeader.substring(7);
		        username = tokenUtil.getUsernameFromToken(token);
		    }

		    if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
		        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		        if(tokenUtil.validateToken(token, userDetails)){
		            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
		                    userDetails, null, userDetails.getAuthorities()
		            );
		            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		        }
		    }
		    
		    logger.info("token "+token);
		    logger.info("Username "+username);
		    
		    filterChain.doFilter(request, response);
		    
		}catch (Exception e) {
			GeneralResultDTO result = new GeneralResultDTO("Error", "Authentication Failed");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.getWriter().write(convertObjectToJson(result));
			
			//throw new AccessValidationException("Error Occured when check token");
		}
	    
	}
	
	
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
