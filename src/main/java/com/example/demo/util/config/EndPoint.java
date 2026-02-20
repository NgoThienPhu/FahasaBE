package com.example.demo.util.config;

public class EndPoint {
	public static String[] PUBLIC_ENDPOINT_GET = {
			
	};
	
	public static String[] PUBLIC_ENDPOINT_POST = {
			"/api/auth/login",
			"/api/auth/refresh",
			
			"/api/admin/auth/login",
			"/api/admin/auth/refresh",
			
			"/api/auth/register",
			"/api/auth/send-otp"
	};
}
