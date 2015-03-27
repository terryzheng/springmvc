package com.demo.springmvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.springmvc.repository.UserRepository;

@Controller
public class HelloWorldController {
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/hello")
	public String hello(
			@RequestParam(value = "corpId", required = false, defaultValue = "79941768") String corpId,
			Model model) {
		String corpName = userRepository.getUserStatusByCorpId(corpId);
		model.addAttribute("name", corpName);
		return "helloworld";
	}

}
