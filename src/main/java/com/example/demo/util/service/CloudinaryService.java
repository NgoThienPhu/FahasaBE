package com.example.demo.util.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.util.exception.CustomException;
import com.example.demo.util.service.dto.UploadResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {
	
	private Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public UploadResponse uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String publicId = uploadResult.get("public_id").toString();
            String url = uploadResult.get("secure_url").toString();
            
            return new UploadResponse(publicId, url);

        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể tải lên tệp tin lên Cloudinary");
        }
    }
    
    public List<UploadResponse> uploadFiles(List<MultipartFile> files) {

        List<UploadResponse> uploadResponses = new ArrayList<>();

        try {

            for (MultipartFile file : files) {

                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.emptyMap()
                );

                String url = uploadResult.get("secure_url").toString();
                String publicId = uploadResult.get("public_id").toString();

                uploadResponses.add(new UploadResponse(publicId, url));
            }

        } catch (Exception e) {
        	for (UploadResponse uploadResponse : uploadResponses) {
				deleteFile(uploadResponse.getPublicId());
			}
        }
        
        return uploadResponses;
    }
    
    public void deleteFile(String publicId) {
		try {
			cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
		} catch (Exception e) {
			throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Không thể xóa tệp tin khỏi Cloudinary");
		}
	}

}
