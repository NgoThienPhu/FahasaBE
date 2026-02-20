package com.example.demo.util.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.util.config.EndPoint;
import com.example.demo.util.entity.CustomUserDetails;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.CustomUserDetailService;
import com.example.demo.util.service.JwtService;
import com.example.demo.util.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	private RedisService redisService;
	private CustomUserDetailService customUserDetailService;

	public JwtAuthenticationFilter(JwtService jwtService, RedisService redisService,
			CustomUserDetailService customUserDetailService) {
		this.jwtService = jwtService;
		this.redisService = redisService;
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
				throw new CustomException(HttpStatus.NOT_FOUND, "Access Token không tồn tại");

			String jwt = authorizationHeader.substring(7);

			handleAccessToken(jwt, response, request, filterChain);
		} catch (ExpiredJwtException | MalformedJwtException | CustomException ex) {
			handleException(ex, response);
		}
	}

	private Boolean shouldBypassFilter(String requestURI, String requestMethod) {
		AntPathMatcher antPathMatcher = new AntPathMatcher();

		for (String endpoint : EndPoint.PUBLIC_ENDPOINT_GET) {
			if (antPathMatcher.match(endpoint, requestURI) && requestMethod.equalsIgnoreCase("GET"))
				return true;
		}

		for (String endpoint : EndPoint.PUBLIC_ENDPOINT_POST) {
			if (antPathMatcher.match(endpoint, requestURI) && requestMethod.equalsIgnoreCase("POST"))
				return true;
		}

		return false;
	}

	private void handleException(Exception ex, HttpServletResponse response)
			throws JsonProcessingException, IOException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		Map<String, String> errors = new HashMap<String, String>();
		errors.put("status", HttpStatus.UNAUTHORIZED.value() + "");
		if (ex instanceof CustomException) {
			CustomException ce = (CustomException) ex;
			errors.put("error", ce.getMessage());
		}
		if (ex instanceof MalformedJwtException) {
			errors.put("error", "INVALID_TOKEN_FORMAT");
		}
		if (ex instanceof ExpiredJwtException) {
			errors.put("error", "ACCESS_TOKEN_EXPIRED");
		}
		errors.put("message", ex.getMessage());
		errors.put("timestamp", LocalDateTime.now().toString());

		response.getWriter().write(new ObjectMapper().writeValueAsString(errors));

	}

	private void handleAccessToken(String accessToken, HttpServletResponse response, HttpServletRequest request,
			FilterChain filterChain) throws IOException, ServletException {
		String username = jwtService.extractUsername(accessToken);
		CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(username);

		if (!redisService.hasKey(String.format("ACCESS_TOKEN:%s", customUserDetails.getId()))) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED",
					"Access token không tồn tại hoặc đã hết hạn, vui lòng thử lại sau");
		}

		String actoken = redisService.getValue(String.format("ACCESS_TOKEN:%s", customUserDetails.getId()));
		if (!accessToken.equals(actoken)) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN_EXPIRED",
					"Access token không tồn tại hoặc đã hết hạn, vui lòng thử lại sau");
		}

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		filterChain.doFilter(request, response);

	}

}
