package com.example.demo.book_price.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.book_price.service.ImportPriceService;

@RestController
@RequestMapping("/api/admin/books/{bookId}/import-prices")
public class ImportPriceController {

	private ImportPriceService importPriceService;

}
