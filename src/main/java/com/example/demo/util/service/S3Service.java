package com.example.demo.util.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.S3Service;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private final S3Client s3Client;
	private final String bucketName;

	public S3Service(S3Client s3Client, @Value("${aws.s3.bucketName}") String bucketName) {
		this.s3Client = s3Client;
		this.bucketName = bucketName;
	}

	public String uploadFile(MultipartFile file) {
		validateFile(file);

		String objectKey = generateObjectKey(file.getOriginalFilename());

		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(objectKey)
				.contentType(file.getContentType()).build();

		try (InputStream is = file.getInputStream()) {
			s3Client.putObject(request, RequestBody.fromInputStream(is, file.getSize()));
		} catch (IOException e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Không đọc được file upload");
		} catch (AwsServiceException | SdkClientException e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi upload file lên S3");
		}

		return getFileUrl(objectKey);
	}

	public void deleteFileByUrl(String fileUrl) {
		deleteFile(extractObjectKey(fileUrl));
	}

	public void deleteFile(String objectKey) {
		s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectKey).build());
	}

	private void validateFile(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "File upload không hợp lệ");
		}
		if (file.getOriginalFilename() == null) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Tên file không hợp lệ");
		}
	}

	private String generateObjectKey(String originalFilename) {
		return UUID.randomUUID() + "_" + originalFilename.trim();
	}

	private String getFileUrl(String objectKey) {
		return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(objectKey)).toString();
	}

	private String extractObjectKey(String fileUrl) {
		int index = fileUrl.lastIndexOf('/');
		return index == -1 ? fileUrl : fileUrl.substring(index + 1);
	}
}
