package com.example.demo.book.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBookRequestDTO {

    @NotBlank(message = "Tiêu đề sách không được để trống")
    private String title;

    @NotBlank(message = "Mô tả sách không được để trống")
    private String description;

    @NotBlank(message = "Tên tác giả không được để trống")
    private String author;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    private String publisher;

    @NotBlank(message = "ISBN không được để trống")
    private String isbn;

    @NotBlank(message = "Loại sản phẩm không được để trống")
    private String categoryId;

    @NotNull(message = "Ngày phát hành không được để trống")
    @PastOrPresent(message = "Ngày phát hành phải bằng thời gian hiện tại hoặc trong quá khứ")
    private LocalDate publishDate;

}