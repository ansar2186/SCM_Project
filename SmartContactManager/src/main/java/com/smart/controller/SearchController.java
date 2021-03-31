package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.model.Contact;
import com.smart.model.User;

@RestController
public class SearchController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ContactRepository contactRepo;


	Logger logger = LoggerFactory.getLogger(SearchController.class);


	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query,Principal principal){
		
		logger.info("------Entering search Method -------------");
		logger.info("------Query -------------" +query);
		
		String name = principal.getName();
		logger.info("-----------get Login user name---------"  +name);
		User user = userRepo.getUserByUserName(name);
		
		
		List<Contact> contactList =contactRepo.findContactByFirstNameAndUser(query, user);
		logger.info("-----------Getting Contact List Size-----------" +contactList.size());
		
		logger.info("------Ending search Method -------------");
		return ResponseEntity.ok(contactList);

	}

}
