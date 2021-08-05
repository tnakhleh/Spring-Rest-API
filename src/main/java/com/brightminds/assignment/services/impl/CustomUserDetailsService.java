package com.brightminds.assignment.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brightminds.assignment.util.LoggerUtil;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private LoggerUtil logger;

	private List<User> usersList;
	
	@Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		logger.debug("In loadUserByUsername "+ s);
		Optional<User> user = this.usersList.stream().filter(usr -> usr.getUsername().equals(s)).findFirst();
		return user.get();
    }
	
	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		logger.debug("------- Set user list "+usersList.size());
		this.usersList = usersList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((usersList == null) ? 0 : usersList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomUserDetailsService other = (CustomUserDetailsService) obj;
		if (usersList == null) {
			if (other.usersList != null)
				return false;
		} else if (!usersList.equals(other.usersList))
			return false;
		return true;
	}
	
	
	
}