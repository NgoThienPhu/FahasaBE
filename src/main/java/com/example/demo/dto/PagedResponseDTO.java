package com.example.demo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.demo.util.view.View;
import com.fasterxml.jackson.annotation.JsonView;

public record PagedResponseDTO<T>(
		
		@JsonView({View.Public.class, View.Internal.class, View.Admin.class, View.Self.class})
	    List<T> content,
	    
	    @JsonView(View.Public.class)
	    int pageNumber,
	    
	    @JsonView(View.Public.class)
	    int pageSize,
	    
	    @JsonView(View.Public.class)
	    long totalElements,
	    
	    @JsonView(View.Public.class)
	    int totalPages,
	    
	    @JsonView(View.Public.class)
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
