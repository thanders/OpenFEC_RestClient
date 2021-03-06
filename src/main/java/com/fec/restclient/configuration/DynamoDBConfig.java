package com.fec.restclient.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.fec.restclient.bean.CandidateTest;
import com.fec.restclient.repository.CandidateRepository;
import com.fec.restclient.repository.CandidateTestRepository;
import com.fec.restclient.service.DataProcessService;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
@EnableDynamoDBRepositories(basePackageClasses = {CandidateRepository.class, CandidateTestRepository.class, DataProcessService.class})
public class DynamoDBConfig {

    private String amazonAWSAccessKey = "";

    private String amazonAWSSecretKey = "";

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        System.out.println(this.amazonAWSAccessKey);
        System.out.println(this.amazonAWSSecretKey);
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }


    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialsProvider())
                .withRegion(Regions.US_EAST_1).build();
    }

    public void setAmazonAWSAccessKey(String amazonAWSAccessKey) {
        this.amazonAWSAccessKey = amazonAWSAccessKey;
    }

    public void setAmazonAWSSecretKey(String amazonAWSSecretKey) {
        this.amazonAWSSecretKey = amazonAWSSecretKey;
    }

    public String getAmazonAWSAccessKey() {
        return amazonAWSAccessKey;
    }

    public String getAmazonAWSSecretKey() {
        return amazonAWSSecretKey;
    }
}
