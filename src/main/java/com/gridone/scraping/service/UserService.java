package com.gridone.scraping.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gridone.scraping.mapper.UserMapper;
import com.gridone.scraping.model.LoginUserDetails;
import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.type.EnumActive;
import com.gridone.scraping.type.EnumUserRole;

@Service
public class UserService implements UserDetailsService {

	List<UserDetails> uu = new ArrayList<UserDetails>();
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserModel user = userMapper.selectByLogin(email);
		System.err.println("username : "+email);
		if(user == null) {
			System.out.println("null");
			throw new UsernameNotFoundException("login fail");
		}else if(!user.getActive().equals(EnumActive.ACTIVE)) {
			System.out.println("InActive");
			throw new UsernameNotFoundException("not ativated user");
		}
		return new LoginUserDetails(user);
	}


	public Map<String, Object> join(UserModel um) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			um.setPassword(passwordEncoder.encode(um.getPassword()));
			um.setRole(EnumUserRole.USER);
			um.setActive(EnumActive.ACTIVE);
			um.setPasswordFailCnt(0);
			userMapper.insertUser(um);
			state_ok = 0;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "register falsed");
		}
		result.put("state_ok", state_ok);
		
		return result;
	}
	
	public UserModel selectUser(String email) {
		return userMapper.selectByLogin(email);
	}
	
	
}
