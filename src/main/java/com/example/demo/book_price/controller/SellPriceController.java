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
import com.example.demo.book_price.entity.SellPrice;
import com.example.demo.book_price.flow.CreateNewSellPriceFlow;
import com.example.demo.book_price.service.ImportPriceService;
import com.example.demo.util.dto.api_response.ApiResponseDTO;
import com.example.demo.util.dto.api_response.ApiResponseSuccessDTO;
import com.example.demo.util.validation.BindingResultUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/books/{bookId}/sell-prices")
public class SellPriceController {

	private CreateNewSellPriceFlow createNewSellPriceFlow;
	private ImportPriceService importPriceService;

	public SellPriceController(CreateNewSellPriceFlow createNewSellPriceFlow, ImportPriceService importPriceService) {
		this.createNewSellPriceFlow = createNewSellPriceFlow;
		this.importPriceService = importPriceService;
	}

	@PostMapping
	public ResponseEntity<?> createNewSellPrice(@RequestBody @Valid CreateImportPriceDTO dto,
			HttpServletRequest request, BindingResult result,
			@PathVariable(name = "bookId", required = true) String bookId) {
		ResponseEntity<?> responseError = BindingResultUtil.handleValidationErrors(result, "Thêm giá bán thất bại",
				request.getRequestURI());
		if (responseError != null)
			return responseError;

		SellPrice sellPrice = createNewSellPriceFlow.createNewSellPrice(bookId, dto.getAmount(), dto.getFrom());
		var response = new ApiResponseSuccessDTO<SellPrice>(201, "Tạo giá bán thành công", sellPrice);
		return new ResponseEntity<ApiResponseDTO>(response, HttpStatus.CREATED);
	}

}
