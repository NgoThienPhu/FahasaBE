package com.example.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.services.implement.CustomUserDetailServiceImpl;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	private CustomUserDetailServiceImpl customUserDetailServiceImpl;
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(CustomUserDetailServiceImpl customUserDetailServiceImpl,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.customUserDetailServiceImpl = customUserDetailServiceImpl;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setPasswordEncoder(encoder());
		dao.setUserDetailsService(customUserDetailServiceImpl);
		return dao;
	}
	
	@Bean
	AuthenticationManager authenticationManager() {
		return new AuthenticationManager() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				return daoAuthenticationProvider().authenticate(authentication);
			}
		};
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(cors -> cors.disable());
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
		http.formLogin(formLogin -> formLogin.disable());
		http.logout(logout -> logout.disable());
		http.csrf(csrs -> csrs.disable());
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
}
