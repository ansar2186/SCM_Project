package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.model.EmailRequest;
import com.smart.service.EmailService;

@RestController
public class EmailController {
	@Autowired
	private EmailService emailService;

	@GetMapping("/email")
	public String welcome() {
		return "Welcome to Email Rest API";
	}
	@PostMapping("/sendEmail")
	public ResponseEntity<?>sendEmail(@RequestBody EmailRequest emailRequest){

		System.out.println("From---" + emailRequest.getTo()+"--subject----" +emailRequest.getSubject()+"--- Message--"+emailRequest.getMessage());
		boolean sendEmail = this.emailService.sendEmail(emailRequest.getSubject(), emailRequest.getMessage(), emailRequest.getTo());

		if(sendEmail) {
			
			return ResponseEntity.ok("done");
		}

		return ResponseEntity.ok("not send");

	}

}
