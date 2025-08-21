package com.example.demo.category.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.category.entity.Category;

public record CategoryTreeResponseDTO(

        String id,
        
        String name,
        
        Integer sortOrder,
        
        Boolean visible,
        
        List<CategoryTreeResponseDTO> children

) {
    public static CategoryTreeResponseDTO fromEntity(Category category) {
        return new CategoryTreeResponseDTO(
                category.getId(),
                category.getName(),
                category.getSortOrder(),
                category.getVisible(),
                category.getChildren() != null 
                    ? category.getChildren()
                        .stream()
                        .map(CategoryTreeResponseDTO::fromEntity)
                        .collect(Collectors.toList())
                    : List.of()
        );
    }
}
