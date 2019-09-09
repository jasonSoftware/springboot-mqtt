package com.jlpay.rabbitmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SpringBootConfiguration
public class RabbitMQProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RabbitMQProducerApplication.class, args);
	}
}
