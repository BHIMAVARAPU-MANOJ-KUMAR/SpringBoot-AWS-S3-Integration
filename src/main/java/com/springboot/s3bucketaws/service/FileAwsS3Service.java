package com.springboot.s3bucketaws.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class FileAwsS3Service {
	
	@Value("${aws.s3.bucket.name}")
	private String s3_bucket_name;
	
	private S3Client s3Client;
	
	public FileAwsS3Service(@Value("${aws.s3.accessKey}") String access_key,
			@Value("${aws.s3.secretKey}") String access_secret_key,
			@Value("${aws.s3.region}") String aws_s3_region) {
		AwsBasicCredentials	basicCredentials = AwsBasicCredentials.create(access_key, access_secret_key);
		s3Client = S3Client
				.builder()
				.region(Region.of(aws_s3_region))
				.credentialsProvider(StaticCredentialsProvider.create(basicCredentials))
				.build();
	}
	
	public void uploadFileToS3(MultipartFile multipartFile, String fileName) throws Exception {
		PutObjectRequest putObjectRequest = PutObjectRequest
				.builder()
				.bucket(s3_bucket_name)
				.key(fileName)
				.build();
		s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
	}
	
	public byte[] getFile(String s3BucketName, String objectFileName) throws Exception {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(s3BucketName)
				.key(objectFileName)
				.build();
		return s3Client.getObject(getObjectRequest).readAllBytes();
	}
}