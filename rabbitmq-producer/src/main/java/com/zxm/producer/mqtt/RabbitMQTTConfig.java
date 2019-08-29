package com.zxm.producer.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class RabbitMQTTConfig {

	@Value("${spring.mqtt.url}")
	private String hostUrl;

	@Value("${spring.mqtt.client.id}")
	private String clientId;

	@Value("${spring.mqtt.default.topic}")
	private String defaultTopic;

	@Value("${spring.mqtt.completionTimeout}")
	private int completionTimeout;

	@Value("${spring.mqtt.Qos}")
	private int defaultQos;

	@Value("${spring.mqtt.userName}")
	private String userName;

	@Value("${spring.mqtt.password}")
	private String password;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() throws MqttException {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setUserName(userName);
		factory.setPassword(password);
		factory.setServerURIs(hostUrl);
		factory.setKeepAliveInterval(2);
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public StockMessageHandler mqttOutbound() throws MqttException {
		StockMessageHandler stockMessageHandler = new StockMessageHandler(clientId + "_outbound", mqttClientFactory());
		stockMessageHandler.setAsync(true);
		stockMessageHandler.setDefaultTopic(defaultTopic);
		stockMessageHandler.setDefaultQos(defaultQos);
		DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
		converter.setPayloadAsBytes(true);
		stockMessageHandler.setConverter(converter);
		return stockMessageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

}
