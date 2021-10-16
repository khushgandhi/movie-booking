package com.moviebooking.api.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.api.common.CommonConstant;
import com.moviebooking.api.dto.AuthenticationRequestDTO;
import com.moviebooking.api.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-booking")
@Slf4j
public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtUtil jwtTokenUtil;
	
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Validated @RequestBody AuthenticationRequestDTO authenticationRequest,HttpServletResponse resp)
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));
		}
		catch(Exception ex)
		{
			log.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed!!");
		}
		
		String jwt = jwtTokenUtil.generateToken(authenticationRequest.getUserName());
		
		Cookie cookie = new Cookie(CommonConstant.USER_COOKIE, jwt);
		
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		
		resp.addCookie(cookie);
		
		return ResponseEntity.ok("Authenticated Successfuly!!");
	
	}
	
}
