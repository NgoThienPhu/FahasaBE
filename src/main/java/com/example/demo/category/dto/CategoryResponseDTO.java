package com.example.demo.category.dto;

import com.example.demo.category.entity.Category;

public record CategoryResponseDTO(

        String id,
        
        String name,
        
        String parentId,
        
        Integer sortOrder,
        
        Boolean visible

) {
    public static CategoryResponseDTO fromEntity(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getSortOrder(),
                category.getVisible()
        );
    }
}
