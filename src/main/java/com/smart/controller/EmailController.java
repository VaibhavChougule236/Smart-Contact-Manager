package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.Service.EmailService;
@RestController
public class EmailController {
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/sendEmail")
	public String sendEmail() {
		emailService.sendEmail("vaibhavchougule236@gmail.com", "Just testing", "Email sent successfully");
	return "sent successfully";
	}
}
