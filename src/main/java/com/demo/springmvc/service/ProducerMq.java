package com.demo.springmvc.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerMq {

	@Autowired
	private AmqpTemplate amqpTemplate;

	public void sendDataToCrQueue(Object obj) {
		double r = Math.random() * 10;
		if (r < 4) {
			amqpTemplate.convertAndSend("queue1_key", obj);
		} else if (r < 7) {
			amqpTemplate.convertAndSend("queue2_key", obj);
		} else {
			amqpTemplate.convertAndSend("queue3_key", obj);
		}
	}
}
