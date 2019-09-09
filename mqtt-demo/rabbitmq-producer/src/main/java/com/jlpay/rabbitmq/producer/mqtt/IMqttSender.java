package com.jlpay.rabbitmq.producer.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface IMqttSender {
	void sendToMqtt(byte[] data, @Header(MqttHeaders.TOPIC) String topic);
	void sendToMqtt(String data);
	void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
	void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}
