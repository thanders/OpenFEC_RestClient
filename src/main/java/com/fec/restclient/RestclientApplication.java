package com.fec.restclient;

import com.fec.restclient.bean.command.CreateCommand;
import com.fec.restclient.bean.command.DeleteCommand;
import com.fec.restclient.bean.menu.Menu;
import com.fec.restclient.service.DataProcessService;
import com.fec.restclient.service.RestClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@SpringBootApplication
public class RestclientApplication {

    @Autowired
    RestClientService restClientService;

    @Autowired
    DataProcessService dataProcessService;

    @Value("${openfec}")
    String openFECkey;

    @Autowired
    Menu menu;

    @Autowired
    CreateCommand createCommand;

    @PostConstruct
    public void startApplication() throws IOException {

        System.out.println("\n"+ "--- OpenFEC RestClient application ---"+ "\n" + "Note: This application requires API keys for OpenFEC and AWS Dynamodb (Access and Secret)"+ "\n");

        int result = menu.printOptions();


        menu.chooseOption(result);

    }

    public static void main(String[] args) throws InterruptedException {

        // Start the spring application (application context)
        SpringApplication.run(RestclientApplication.class, args);

    }

}
