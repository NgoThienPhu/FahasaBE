package com.example.demo.book.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBookRequestDTO {
		
		@NotBlank(message = "Tiêu đề sách không được để trống")
		String title;
		
		@NotBlank(message = "Mô tả sách không được để trống")
		String description;
		
		@NotBlank(message = "Tên tác giả không được để trống")
		String author;
		
		@NotBlank(message = "Tên nhà cung cấp không được để trống")
		String publisher;
		
		@NotBlank(message = "ISBN không được để trống")
		String isbn;
		
		@NotBlank(message = "Loại sản phẩm không được để trống")
		String categoryId;
		
		@NotBlank(message = "Ngày phát hành không được để trống")
		LocalDate publishDate;
		
		@Positive(message = "Giá sản phẩm phải lớn hơn 0")
		@Min(value = 1_000, message = "Giá sản phẩm tối thiểu là 1.000 VND")
		BigDecimal price;
		
		@NotNull(message = "Ảnh chính sản phẩm không được để trống")
		MultipartFile primaryImage;
		
	    @Size(max = 5, message = "Sách chỉ có tối đa 5 ảnh")
		List<MultipartFile> secondImages = new ArrayList<>();
		
}
