package com.example.demo.dto;

import java.util.List;

public record FilterConditionDTO(
		String key,
		List<String> values
) {}
