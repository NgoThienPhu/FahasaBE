package com.example.demo.util.dto.api_response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponsePaginationSuccess<T> extends ApiResponseSuccessDTO<T> {

	private int page;
	private int limit;
	private long totalItems;
	private int totalPages;

	public ApiResponsePaginationSuccess(int status, String message, T data, int page, int limit, long totalItems,
			int totalPages) {
		super(status, message, data);
		this.page = page;
		this.limit = limit;
		this.totalItems = totalItems;
		this.totalPages = totalPages;
	}

	public static <T> ApiResponsePaginationSuccess<List<T>> fromPage(Page<T> page, String message) {
		return new ApiResponsePaginationSuccess<>(HttpStatus.OK.value(), 
				message, 
				page.getContent(), 
				page.getNumber(),
				page.getSize(), 
				page.getTotalElements(), 
				page.getTotalPages());
	}

}
