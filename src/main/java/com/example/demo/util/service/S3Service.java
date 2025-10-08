package com.example.demo.util.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
	
	String uploadFile(MultipartFile file) throws IOException;
	
	void deleteFile(String fileName);
	
	public static String convertFileURlToFileName(String fileURl) {
		int index = fileURl.lastIndexOf("/");
		if(index == -1) return fileURl;
		return fileURl.substring(index + 1);
	}
	
}
