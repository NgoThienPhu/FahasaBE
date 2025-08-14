package com.example.demo.attribute.dto;

import com.example.demo.attributevalue.entity.AttributeValue;

public record AttributeResponseDTO(

		String attributeName,

		String attributeValue

) {

	public static AttributeResponseDTO fromEntity(AttributeValue attributeValue) {
		return new AttributeResponseDTO(attributeValue.getAttribute().getName(), attributeValue.getValue());
	}

}
