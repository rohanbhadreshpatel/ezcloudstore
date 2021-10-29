package com.cloud.ezcloudstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

@Configuration
public class AWSConfig {
	
	 @Value("${aws.access.key.id}")
	    private String accessKey;

	    @Value("${aws.access.key.secret}")
	    private String secretKey;

	    @Value("${aws.region}")
	    private String awsRegion;

	    @Value("${aws.s3.bucket}")
	    private String awsS3Bucket;
	    
	    @Value("${aws.s3.endpointUrl}")
	    private String endpointUrl;

	    @Bean(name = "accessKey")
	    public String getAccessKey() {
	        return accessKey;
	    }

	    @Bean(name = "secretKey")
	    public String getAWSKeySecret() {
	        return secretKey;
	    }
	    
	    @Bean(name = "endpointUrl")
	    public String getEndPointURL() {
	        return endpointUrl;
	    }

	    @Bean(name = "awsRegion")
	    public Region getAWSPollyRegion() {
	        return Region.getRegion(Regions.fromName(awsRegion));
	    }

	    @Bean(name = "awsCredentialsProvider")
	    public AWSCredentialsProvider getAWSCredentials() {
	        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
	        return new AWSStaticCredentialsProvider(awsCredentials);
	    }

	    @Bean(name = "awsS3Bucket")
	    public String getAWSS3Bucket() {
	        return awsS3Bucket;
	    }

}
