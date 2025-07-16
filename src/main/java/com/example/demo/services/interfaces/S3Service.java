package com.example.demo.services.interfaces;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	
	String uploadFile(MultipartFile file) throws IOException;
	
	List<String> uploadFiles(List<MultipartFile> files) throws IOException;
	
	void deleteFile(String fileName);
	
}
