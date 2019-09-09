package com.jlpay.rabbitmq.producer.mqtt;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt")
public class MqttController {

	@Resource
	private IMqttSender iMqttSender;

	@RequestMapping("sendMqtt")
	public String sendMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "topic") String topic){
		try {
			iMqttSender.sendToMqtt(msg, topic);
			iMqttSender.sendToMqtt(topic, 2, msg);
			return  "send successfully";
		}catch (Exception e) {
			// TODO: handle exception
			System.out.print(e);
			return "send failed";
		}
	}
}
