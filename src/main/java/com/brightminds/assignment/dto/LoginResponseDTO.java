package com.brightminds.assignment.dto;

public class LoginResponseDTO {
	private String accessToken;
	
	public LoginResponseDTO(String accessToken) {
		this.accessToken = accessToken;
	}
	
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


}
