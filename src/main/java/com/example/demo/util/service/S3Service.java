package com.example.demo.util.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.util.service.S3Service;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3Service {

	private S3Client s3Client;

	@Value("${aws.s3.bucketName}")
	private String bucketName;

	@Value("${aws.region}")
	private String region;

	public S3Service(S3Client s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadFile(MultipartFile file) {
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename().trim();

		PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(fileName)
				.contentType(file.getContentType()).build();

		try (InputStream inputStream = file.getInputStream()) {
			s3Client.putObject(request, RequestBody.fromInputStream(inputStream, file.getSize()));
		} catch (AwsServiceException e) {
			throw new RuntimeException("Lỗi dịch vụ AWS: " + e.awsErrorDetails().errorMessage(), e);
		} catch (SdkClientException e) {
			throw new RuntimeException("Lỗi AWS SDK", e);
		} catch (IOException e) {
			throw new RuntimeException("Không đọc được dữ liệu file upload", e);
		}
		return convertFileNameToFileURL(fileName, bucketName, region);
	}

	public void deleteFile(String fileName) {
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(fileName)
				.build();
		s3Client.deleteObject(deleteObjectRequest);
	}

	public static String convertFileURlToFileName(String fileURl) {
		int index = fileURl.lastIndexOf("/");
		if (index == -1)
			return fileURl;
		return fileURl.substring(index + 1);
	}

	public static String convertFileNameToFileURL(String fileName, String awsBucketName, String awsRegion) {
		return String.format("https://%s.s3.%s.amazonaws.com/%s", awsBucketName, awsRegion, fileName);
	}

}
