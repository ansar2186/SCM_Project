package com.smart.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.dao.UserRepository;
import com.smart.model.User;

public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByUserName = userRepo.getUserByUserName(username);
		
		
		if(userByUserName==null) {
			throw new UsernameNotFoundException("User Not Fount!!!");
		}
		//userByUserName.setLoginDateTime(new Date());
		
		CustomUserDetails customUserDetails= new CustomUserDetails(userByUserName);
		
		return customUserDetails;
	}

}
