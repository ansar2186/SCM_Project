package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.UserRepository;
import com.smart.helper.Message;
import com.smart.model.Contact;
import com.smart.model.User;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@RequestMapping("/")
	public String home(Model model) {
		
		System.out.println("---------------Inside Home Page------------");
		/*
		 * User user= new User(); user.setName("Ansar");
		 * user.setEmail("ahmad.ansar74@gmail.com"); //user.setEnabled(true);
		 * user.setImageUrl("com.org"); user.setPassword("ansar#123");
		 * user.setRole("Admin"); user.setAbout("I am enginer"); Contact con= new
		 * Contact(); user.getList().add(con); userRepo.save(user);
		 */
		model.addAttribute("title", "Home-Page");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		
		model.addAttribute("title", "About-Page");
		
		return "about";
	}
	
	@RequestMapping("/sign-up")
	public String signUp(Model model) {
		
		model.addAttribute("title", "Sign-Up");
		model.addAttribute("user", new User());
		
		return "signUp";
	}
	
	@RequestMapping("/register")
	public String register(Model model,@ModelAttribute("user") User user,/*@RequestParam("name")String name*/
			@RequestParam(value = "agreement",defaultValue = "false") boolean agreement,HttpSession session
			/*@RequestParam(value = "email") String email,
			@RequestParam(value = "password")String password*/) {
		
		System.out.println("---------Inside Register Method---------------" );
		
		
		/*
		 * if(result.hasErrors()) {
		 * 
		 * System.out.println("----------Error-------" +result.toString());
		 * model.addAttribute("user", user); return "signUp"; }
		 */
		 
		
		try {
			
			if(!agreement) {
				System.out.println("You don't have accept the Term and condition");
				model.addAttribute("user",  user);
				throw new Exception("You don't have accept the Term and condition");
				
			}else {
				user.setRole("ROLE_USER");
				user.setEnabled(true);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				this.userRepo.save(user);
				System.out.println("---------USER---------------" +user);
				model.addAttribute("user", new User());
				session.setAttribute("message", new Message("User Register hase been successfully Submit!", "alert-success"));
				return "signUp";
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went wrong!!"+e.getMessage(), "alert-danger"));
			
			return "signUp";			
		}
		
		
	}
	
	
	@RequestMapping("/sign-in")
	public String CustomeLogin(Model model) {
		
		model.addAttribute("title", "Login-Page");
		
		return "login";
	}
	
	
}
