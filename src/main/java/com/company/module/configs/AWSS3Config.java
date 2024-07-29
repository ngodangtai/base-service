package com.company.module.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSS3Config {

    @Value("${aws.region}")
    private String awsRegion;
//    @Value("${aws.credentials.access-key}")
    private String awsAcceskey;
//    @Value("${aws.credentials.secret-key}")
    private String awsSecretkey;

    @Bean
    public AmazonS3 getAWSS3Service() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAcceskey, awsSecretkey)))
                .withRegion(awsRegion)
                .build();
    }
}
