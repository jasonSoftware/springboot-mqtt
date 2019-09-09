package com.jlpay.rebbitmq.consumer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component("messageHandler")
public class MQTTMessageHandler implements MessageHandler {

	private final Logger logger = LoggerFactory.getLogger(MQTTMessageHandler.class);

	public void handleMessage(Message<?> message) throws MessagingException {
		logger.info("rece topic message: {}", message);

		String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
		logger.info("rece topic ：{}", topic);
		Object payLoad = message.getPayload();

		// 如果不设置转换器这里强转byte[]会报错
		byte[] data = (byte[]) payLoad;

		logger.info(new String(data));

		System.out.println(message.getPayload());
	}
}
