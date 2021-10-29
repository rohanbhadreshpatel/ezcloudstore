package com.cloud.ezcloudstore.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.util.IOUtils;
import javax.servlet.http.Part;

@Service
public class AWSService {
	
	private AmazonS3 s3client;
	private String awsS3Bucket;
	private String endpointUrl;
	
	@Autowired
	public AWSService(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3Bucket,
			String endpointUrl) {
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName()).build();
		this.awsS3Bucket = awsS3Bucket;
		this.endpointUrl = endpointUrl;
	}
	
	public String uploadFileToS3(String filename, Part part, InputStream inputStream,  String emailid) {

		try {
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(part.getSize());
			metadata.setContentLength(Long.valueOf(part.getInputStream().available()));
			String keyPath = emailid + "/" + filename;
			System.out.println("key path" +keyPath);
			s3client.putObject(new PutObjectRequest(awsS3Bucket, keyPath, part.getInputStream(), metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			System.out.println("put");
			return endpointUrl + "/" + awsS3Bucket + "/" + keyPath;

		} catch (Exception e) {

			throw new RuntimeException("FAIL!");
		}
	}
	
	public byte[] downloadFile(String fileName, String emailid) {

		try {
			String keyPath = emailid + "/" + fileName;
			S3Object s3Object = s3client.getObject(new GetObjectRequest(awsS3Bucket, keyPath));
			S3ObjectInputStream inputStream = s3Object.getObjectContent();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			return bytes;
			} catch (IOException e) {
			throw new RuntimeException("FAIL!");
		}

	}
	
	public boolean deleteFile(String fileName, String emailid) {
		try {
			String keyPath = emailid + "/" + fileName;
			s3client.deleteObject(new DeleteObjectRequest(awsS3Bucket, keyPath));
			return true;
		}
		catch (Exception e) {
			throw new RuntimeException("FAIL!");
		}
	}

}
