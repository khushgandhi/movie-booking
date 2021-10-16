package com.moviebooking.api.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.moviebooking.api.common.CommonConstant;
import com.moviebooking.api.model.User;
import com.moviebooking.api.repository.UserRepository;
import com.moviebooking.api.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepo;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Cookie[] cookies = request.getCookies();

		String jwt = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (CommonConstant.USER_COOKIE.equalsIgnoreCase(cookie.getName())) {
					jwt = cookie.getValue();
				}
			}
		}

		if (jwt != null && jwtUtil.validateToken(jwt)) {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					jwtUtil.extractUsername(jwt), null, null);
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			User user = userRepo.findByUserName(jwtUtil.extractUsername(jwt));
			
			request.setAttribute("userId", user.getUserId());
		}
		
		filterChain.doFilter(request, response);
	}

}
