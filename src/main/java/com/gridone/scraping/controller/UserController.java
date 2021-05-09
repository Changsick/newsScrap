package com.gridone.scraping.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gridone.scraping.model.SearchBase;
import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.service.UserService;
import com.gridone.scraping.type.EnumActive;
import com.gridone.scraping.type.EnumUserRole;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping(value = "/join")
	public @ResponseBody Map<String, Object> join(@Valid @RequestBody UserModel um, final BindingResult bindingResult) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		UserModel user = userService.selectUser(um.getEmail());
		if(user != null) {
			result.put("msg", "register duplicated");
			result.put("state_ok", state_ok);
			FieldError error = new FieldError("user", "login", "register duplicated");
			return result;
		}
		if (bindingResult.hasErrors()) {
//			System.out.println("bindingResult : "+bindingResult.getAllErrors());
		}
		return userService.join(um);
	}
	
	@PostMapping(value = "/myInfo")
	public @ResponseBody Map<String, Object> myInfo() {
		return userService.myInfo();
	}
	
	@PostMapping(value = "/saveMyInfo")
	public @ResponseBody Map<String, Object> saveMyInfo(UserModel um) {
		return userService.saveMyInfo(um);
	}
	
	@PreAuthorize("isAdmin()")
	@PostMapping(value = "/getUsersInfo")
	public @ResponseBody Map<String, Object> getUsersInfo(SearchBase search) {
		return userService.getUsersInfo(search);
	}
	
	@PreAuthorize("isAdmin()")
	@PostMapping(value = "/getUser")
	public @ResponseBody Map<String, Object> getUser(Integer id) {
		Map<String, Object> result = new HashMap<String, Object>();
		int state_ok = -1;
		try {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", userService.selectUserById(id));
			data.put("roleList", EnumUserRole.values());
			data.put("activeList", EnumActive.values());
			result.put("data", data);
			state_ok = 200;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", e.getMessage());
		}
		result.put("state_ok", state_ok);
		return result;
	}
	
	@PreAuthorize("isAdmin()")
	@PostMapping(value = "/editUser")
	public @ResponseBody Map<String, Object> editUser(UserModel um) {
		return userService.editUser(um);
	}
	
	
	
}
