package com.example.demo.book_price.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_price.dto.CreateImportPriceDTO;
import com.example.demo.book_price.entity.ImportPrice;
import com.example.demo.book_price.flow.CreateNewImportPriceFlow;
import com.example.demo.book_price.service.ImportPriceService;
import com.example.demo.util.dto.ApiResponseDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books/{bookId}/import-prices")
public class ImportPriceController {

	private CreateNewImportPriceFlow createNewImportPriceFlow;
	private ImportPriceService importPriceService;

	public ImportPriceController(CreateNewImportPriceFlow createNewImportPriceFlow,
			ImportPriceService importPriceService) {
		this.createNewImportPriceFlow = createNewImportPriceFlow;
		this.importPriceService = importPriceService;
	}

	@PostMapping
	public ResponseEntity<?> createNewImportPrice(@RequestBody @Valid CreateImportPriceDTO dto, BindingResult result,
			@PathVariable(name = "bookId", required = true) String bookId) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Thêm giá nhập thất bại");
		if (responseError != null)
			return responseError;

		ImportPrice importPrice = createNewImportPriceFlow.createNewImportPrice(bookId, dto.getAmount(), dto.getFrom());
		var response = new ApiResponseDTO<ImportPrice>("Thêm giá nhập thành công", true, importPrice);
		return new ResponseEntity<ApiResponseDTO<ImportPrice>>(response, HttpStatus.CREATED);
	}

}
