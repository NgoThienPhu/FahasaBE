package com.example.demo.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.config.EndPoint;
import com.example.demo.services.interfaces.CustomUserDetailService;
import com.example.demo.services.interfaces.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private CustomUserDetailService customUserDetailService;

	public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailService customUserDetailService) {
		this.jwtService = jwtService;
		this.customUserDetailService = customUserDetailService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String requestURI = request.getRequestURI();
			String requestMethod = request.getMethod();

			if (shouldBypassFilter(requestURI, requestMethod)) {
				filterChain.doFilter(request, response);
				return;
			}

			String authorizationHeader = request.getHeader("Authorization");
			if (authorizationHeader == null || authorizationHeader.isBlank()
					|| !authorizationHeader.startsWith("Bearer "))
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
						"Mã JWT không tồn tại, vui lòng thử lại sau");

			String jwt = authorizationHeader.substring(7);

			handleAccessToken(jwt, response, request, filterChain);
			
		} catch (ResponseStatusException e) {
			handleException(e.getStatusCode(), "error", e.getMessage(), response);
		} catch (ExpiredJwtException e) {
			handleException(HttpStatus.UNAUTHORIZED, "error", "Access Token đã hết hạn, vui lòng thử lại sau",
					response);
		}
	}

	private Boolean shouldBypassFilter(String requestURI, String requestMethod) {
		AntPathMatcher antPathMatcher = new AntPathMatcher();

		for (String endpoint : EndPoint.PUBLIC_ENDPOINT_GET) {
			if (antPathMatcher.match(endpoint, requestURI))
				return true;
		}

		return false;
	}

	private void handleException(HttpStatusCode httpStatusCode, String status, String message,
			HttpServletResponse response) throws JsonProcessingException, IOException {

		response.setStatus(httpStatusCode.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, String> errors = new HashMap<String, String>();
		errors.put("status", status);
		errors.put("message", message);
		errors.put("timestamp", LocalDateTime.now().toString());

		response.getWriter().write(new ObjectMapper().writeValueAsString(errors));

	}

	private void handleAccessToken(String accessToken, HttpServletResponse response, HttpServletRequest request,
			FilterChain filterChain) throws IOException, ServletException {

		if (!jwtService.isTokenExpired(accessToken)) {
			String username = jwtService.extractUsername(accessToken);
			UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			filterChain.doFilter(request, response);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Mã JWT không hợp lệ hoặc đã hết hạn, vui lòng thử lại sau");
		}

	}

}
