package com.smart.controller;

import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.model.User;
import com.smart.service.EmailService;

@Controller
public class ForgotController {
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Logger logger = LoggerFactory.getLogger(ForgotController.class);

	Random random = new Random();
	//forgot password handler open page
	@RequestMapping("/forgot-pwd")
	public String OpenForgotPasswordPage(Model model) {

		model.addAttribute("title", "Forgot-Password-Page");

		logger.info("-------------Entring the OpenForgotPasswordPage Method---------------");

		return "forgot_Pwd";

	}

	// Otp Handler

	@PostMapping("/sendOtp")
	public String sendOtp(@RequestParam("email") String email, Model model,HttpSession session) {
		logger.info("---------Entring the SendOtp Method with the email------------" +email);


		int otp = random.nextInt(1000000);

		logger.info("----------Genrated OTP is ---------" +otp);
		String message="Your 6 Digits OTP is --" +otp;

		try {

			boolean sendEmail = this.emailService.sendEmail("OTP", message, email);
			model.addAttribute("title", "Verify-OTP Page");


			if(sendEmail) {
				session.setAttribute("otp", otp);
				session.setAttribute("email", email);
				return "verify_Otp";
			}else {
				session.setAttribute("message", "please Check your email");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "forgot_Pwd";
	}
	
	// verify OTP handler
	
	@PostMapping("/verifyOtp")
	public String verifyOtp(@RequestParam("otp") String otp , Model model, HttpSession session) {
		logger.info("-----------Entering the VerifyOtp merhod with the OTP -------------"+otp);
		Integer myotp =(Integer)session.getAttribute("otp");
		String email = (String)session.getAttribute("email");
		
		if(myotp==Integer.parseInt(otp)) {
			
			
			User user = this.userRepository.getUserByUserName(email);
			
			
			return "newPassword";
			
		}else {
			session.setAttribute("message", "Please enter the right OTP");
			return "verify_Otp";
		}	
	}
	// change password handler
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session) {
		
		logger.info("----------Entering the chnagePassword Method with newPassword--------" +newPassword);
		String userName =(String) session.getAttribute("email");
		
		User userByUserName = this.userRepository.getUserByUserName(userName);
		
		if(userByUserName!=null) {
			
			userByUserName.setPassword(bCryptPasswordEncoder.encode(newPassword));
			userByUserName.setUpdateDateTime(new Date());
			User save = this.userRepository.save(userByUserName);
			session.setAttribute("message", "Password Successfully changed");
			logger.info("------------Password Successfully changed with user---" +userByUserName.getEmail());
			return "login"; 
			
		}else {
			session.setAttribute("message", "Password not chnaged please try again !!");
			logger.info("---------Password not changed becouse user not found with the given email id--"+userName);
			return "newPassword";
		}
		
		
	}

}
