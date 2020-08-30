package com.joyce.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {
	private Logger logger = LoggerFactory.getLogger(KafkaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}

}
