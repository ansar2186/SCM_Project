package com.smart.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.model.Contact;
import com.smart.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	@Query("from Contact as c where c.user.id=:userID")
	//Pageable have two information
	//1 current page
	//2 no of record perpage
	public Page<Contact> findAllContactByUserName(@Param("userID")int userID, Pageable pageable);
	
	@Query("from Contact as c where c.contectId=:cId")
	public Contact getContactByContactID(@Param("cId")int cId);
	
	
	public List<Contact> findContactByFirstNameAndUser(String firstName, User user);
	
	
		
	

}
