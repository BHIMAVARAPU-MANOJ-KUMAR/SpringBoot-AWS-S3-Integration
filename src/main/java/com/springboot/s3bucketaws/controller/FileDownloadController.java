package com.springboot.s3bucketaws.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.s3bucketaws.service.FileService;

@RestController
@RequestMapping(value = {"api/s3/download"})
public class FileDownloadController {
	
	private FileService fileService;
	
	public FileDownloadController(FileService service) {
		this.fileService=service;
	}
	
	@GetMapping(value = {"/{bucketName}/{fileName}"})
	public ResponseEntity<byte[]> findByFileName(@PathVariable String bucketName,
			@PathVariable String fileName) {
		try {
			byte[] fileData = fileService.downloadFile(bucketName, fileName);
			return ResponseEntity.ok(fileData);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}
}