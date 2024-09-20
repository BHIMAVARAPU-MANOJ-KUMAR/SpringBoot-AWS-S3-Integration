package com.springboot.s3bucketaws.service;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class FileService {
	
	private Integer MAX_ALLOWED_IMAGE_SIZE = 5 * 1024 * 1024;
	
	private Integer MAX_ALLOWED_PDF_SIZE = 10 * 1024 * 1024;
	
	private FileAwsS3Service awsS3Service;
	
	public FileService(FileAwsS3Service fileAwsS3Service) {
		this.awsS3Service=fileAwsS3Service;
	}
	
	public void handleImageUpload(MultipartFile file) throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileType = StringUtils.getFilenameExtension(fileName);
		
		String[] allowedFileExtensionTypes = {"jpeg", "jpg", "png", "gif"};
		Boolean isFileTypeAllowed = Arrays.stream(allowedFileExtensionTypes).anyMatch(fileType::equalsIgnoreCase);
		
		if (!isFileTypeAllowed) {
			throw new Exception(fileType + " file is not Allowed.");
		}
		
		if (file.getSize() > MAX_ALLOWED_IMAGE_SIZE) {
			throw new Exception("Maximum 5MB Allowed.");
		}
		
		String uploadImageName = generateUUID();
		awsS3Service.uploadFileToS3(file, "images/" + uploadImageName + "." + fileType);
	}
	
	public void handlePdfUpload(MultipartFile file) throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String fileType = StringUtils.getFilenameExtension(fileName);
		
		String[] allowedFileExtensionTypes = {"pdf"};
		Boolean isFileTypeAllowed = Arrays.stream(allowedFileExtensionTypes).anyMatch(fileType::equalsIgnoreCase);
		
		if (!isFileTypeAllowed) {
			throw new Exception(fileType + " file is not Allowed.");
		}
		
		if (file.getSize() > MAX_ALLOWED_PDF_SIZE) {
			throw new Exception("Maximum 10MB Allowed.");
		}
		
		String uploadPdfName = generateUUID();
		awsS3Service.uploadFileToS3(file, "pdfs/" + uploadPdfName + "." + fileType);
	}
	
	public byte[] downloadFile(String bucketName, String fileName) throws Exception {
		try {
			return awsS3Service.getFile(bucketName, fileName);
		} catch (S3Exception s3Exception) {
			throw new Exception("File Download Failed: " + s3Exception.awsErrorDetails().errorMessage());
		}
	}
	
	private String generateUUID() {
		return UUID
				.randomUUID()
				.toString();
	}
}