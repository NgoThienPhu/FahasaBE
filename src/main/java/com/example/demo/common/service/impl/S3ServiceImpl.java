package com.example.demo.common.service.impl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.service.S3Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3ServiceImpl implements S3Service {

	private S3Client s3Client;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("${aws.region}")
	private String region;

	public S3ServiceImpl(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	@Override
	public String uploadFile(MultipartFile file) throws IOException {

		String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

		PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(fileName)
				.contentType(file.getContentType()).build();

		s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

		return convertFileNameToFileURL(fileName);
	}

	@Override
	public void deleteFile(String fileName) {

		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(fileName)
				.build();

		s3Client.deleteObject(deleteObjectRequest);
	}
	
	private String convertFileNameToFileURL(String fileName) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
	}

}
