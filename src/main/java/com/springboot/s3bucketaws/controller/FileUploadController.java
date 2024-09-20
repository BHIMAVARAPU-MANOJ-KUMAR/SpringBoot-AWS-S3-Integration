package com.springboot.s3bucketaws.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.s3bucketaws.service.FileService;

@RestController
@RequestMapping(path = {"api/s3/upload"})
public class FileUploadController {
	
	private FileService fileService;
	
	public FileUploadController(FileService service) {
		this.fileService=service;
	}
	
	@PostMapping(value = {"/images"})
	public ResponseEntity<Object> uploadImages(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		 fileService.handleImageUpload(multipartFile);
		 return ResponseEntity.status(HttpStatus.OK).body("File Upload Successful");
	}
	
	@PostMapping(value = {"/pdfs"})
	public ResponseEntity<Object> uploadPdfs(@RequestParam("file") MultipartFile multipartFile) throws Exception {
		 fileService.handlePdfUpload(multipartFile);
		 return ResponseEntity.status(HttpStatus.OK).body("File Upload Successful");
	}
	
}