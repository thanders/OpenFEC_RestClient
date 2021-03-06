package com.fec.restclient.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.fec.restclient.bean.Candidate;
import com.fec.restclient.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.lang.Thread.sleep;

@Service
public class DataProcessService {

    @Autowired
    AmazonDynamoDB amazonDynamoDB;

    @Autowired
    CandidateRepository repository;

    DynamoDBMapper dynamoDBMapper;

    CreateTableRequest tableRequest;

    Class<Candidate> tableName;

    String awsAccessKey;

    String awsSecretKey;

    public void connectToDB(){

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
        System.out.println("Connection details");
        System.out.println(this.amazonDynamoDB.listTables());
    }

    public void instantiateMapper(){
;
        // Create instance of DynamoDBmapper
        System.out.println("TABLES:  "+ amazonDynamoDB.listTables());

        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);



    }

    public void createTableRequest(Object tableClass){

        System.out.println(tableClass.getClass());

        // Create a request for a new table based on the candidate class
        this.tableRequest = dynamoDBMapper.generateCreateTableRequest(tableClass.getClass());
        // Add provisioned throughput - required
        this.tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

    }

    public boolean createTable() throws InterruptedException {

        // Check if it already exists
        Boolean exists = this.tableExists();

        // If it exists
        if (exists == true){

            return false;
        }
        // If table doesn't already exist
        else {
            System.out.println("Creating table...");
            System.out.println(this.tableRequest.getTableName());
            System.out.println(this.tableRequest.getAttributeDefinitions());
            amazonDynamoDB.createTable(this.tableRequest); // Create it
            sleep(20000);
            return true;
        }
    }

    public boolean tableExists(){

        try{ DescribeTableResult json = this.amazonDynamoDB.describeTable("Candidate");
            System.out.println(json.getTable().getAttributeDefinitions());}

        catch (ResourceNotFoundException e) {
            System.out.println("Exception -- Could not find table");
            //e.printStackTrace();
            return false;
        }

        catch(Exception e){
            System.out.println("Exception -- Other");
            e.printStackTrace();
        }
        System.out.println("Table exists");

        return true;
    }


    public void setTableName(Class<Candidate> tableName) {
        this.tableName = tableName;
    }


    public DynamoDBMapper getDynamoDBMapper() {
        return dynamoDBMapper;
    }

    public AmazonDynamoDB getAmazonDynamoDB() {
        return amazonDynamoDB;
    }

    public void setAwsAccessKey(String awsAccessKey) {
        this.awsAccessKey = awsAccessKey;
    }

    public void setAwsSecretKey(String awsSecretKey) {
        this.awsSecretKey = awsSecretKey;
    }
}



