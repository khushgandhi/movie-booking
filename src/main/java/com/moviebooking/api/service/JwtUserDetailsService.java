package com.moviebooking.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moviebooking.api.dto.UserDTO;
import com.moviebooking.api.model.User;
import com.moviebooking.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

	public final UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepo.findByUserName(username);
		
		if(user == null)
			throw new UsernameNotFoundException("Invalid username");
		
		return new UserDTO(user);
	}

}
