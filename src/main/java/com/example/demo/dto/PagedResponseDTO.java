package com.example.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public record PagedResponseDTO<T>(
	    List<T> content,
	    int pageNumber,
	    int pageSize,
	    long totalElements,
	    int totalPages,
	    boolean last
) {
	
	public static <T> PagedResponseDTO<T> convertPageToPagedResponseDTO(Page<T> page) {
        return new PagedResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
	
}
