package com.example.demo.util.response;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Pagination {

	private int page;
	private int limit;
	private long totalItems;
	private int totalPages;

	public Pagination convertFromPage(Page<?> page) {
		return new Pagination(
				page.getNumber(), 
				page.getSize(), 
				page.getTotalElements(), 
				page.getTotalPages());
	}

}
