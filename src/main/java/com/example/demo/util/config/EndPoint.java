package com.example.demo.util.config;

public class EndPoint {
	public static String[] PUBLIC_ENDPOINT_GET = {
			"/api/auth/login",
			"/api/auth/register",
	};
	
	public static String[] PUBLIC_ENDPOINT_POST = {
			"/api/auth/refresh",
			"/api/auth/send-otp"
	};
}
