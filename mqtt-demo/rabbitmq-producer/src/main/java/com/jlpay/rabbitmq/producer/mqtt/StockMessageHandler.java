package com.jlpay.rabbitmq.producer.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

public class StockMessageHandler extends MqttPahoMessageHandler {

	private final Logger logger = LoggerFactory.getLogger(StockMessageHandler.class);

	@Value("${spring.mqtt.client.id}")
	private String clientId;

	public StockMessageHandler(String clientId, MqttPahoClientFactory clientFactory) {
		super(clientId, clientFactory);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		logger.info("topic:" + topic);
		logger.info("" + message.getId() + "," + message.getQos() + "," + message.isDuplicate() + ","
				+ message.isRetained());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		try {
			logger.info("topic:" + token.getMessage().getQos() + "," + token.getTopics());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}
