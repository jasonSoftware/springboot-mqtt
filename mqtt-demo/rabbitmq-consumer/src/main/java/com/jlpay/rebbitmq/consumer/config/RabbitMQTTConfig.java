package com.jlpay.rebbitmq.consumer.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

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

	// 消息处理器
	@Resource
	private MessageHandler messageHandler;

	// mqtt客户端工厂类
	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setUserName(userName);
		factory.setPassword(password);
		factory.setServerURIs(hostUrl);
		factory.setKeepAliveInterval(2);
		return factory;
	}

	// 接收通道（消息消费者）
	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	// 配置消息适配器，配置订阅客户端
	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(getClientId("inbound"), mqttClientFactory(), defaultTopic);
		adapter.setCompletionTimeout(completionTimeout);
		// 设置转换器，接收bytes
		DefaultPahoMessageConverter converter = new DefaultPahoMessageConverter();
		converter.setPayloadAsBytes(true);
		adapter.setConverter(converter);
		adapter.setQos(defaultQos);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	// 通过通道获取数据
	// 接收消息处理器（订阅）
	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
//		return messageHandler;
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
				System.out.println("topic:" + topic);

				String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
				System.out.println("topic_type:" + type);

				String msg = new String((byte[]) message.getPayload());
				System.out.println("msg:" + msg);
				System.out.println("payload:" + message.getPayload());
			}
		};
	}

	private String getClientId(String method) {
		return this.clientId + "-" + method + "-" + System.currentTimeMillis();
	}

}
