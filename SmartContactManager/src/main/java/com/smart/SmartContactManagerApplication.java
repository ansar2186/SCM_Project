package com.smart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartContactManagerApplication {
	
    private static final Logger LOGGER = LogManager.getLogger(SmartContactManagerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SmartContactManagerApplication.class, args);
		
		
		  LOGGER.info("-----------In-side SmartContactManagerApplication-----------------");
		  LOGGER.debug("Debug level log message");
		  LOGGER.error("Error level log message");
		 
	}

}
