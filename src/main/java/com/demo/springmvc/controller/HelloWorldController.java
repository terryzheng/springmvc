package com.demo.springmvc.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.springmvc.repository.UserRepository;
import com.zhaopin.rabbitMQ.producer.ProducerMq;

@Controller
public class HelloWorldController {
	@Autowired
	private UserRepository userRepository;

	@Value("#{config['jdbc.driverClass']}")
	private String driverClass;

	@Autowired
	private ProducerMq producer;

	@RequestMapping("/hello")
	public String hello(
			@RequestParam(value = "corpId", required = false, defaultValue = "79941768") String corpId,
			Model model) {
		String corpName = userRepository.getUserStatusByCorpId(corpId);
		for (int i = 0; i < 100; i++) {
			producer.convertAndSend("99msg", "data" + i);
		}
		model.addAttribute("name", driverClass + corpName);
		return "helloworld";
	}

}
