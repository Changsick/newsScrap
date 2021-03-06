package com.gridone.scraping.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping(value = "/join")
	public @ResponseBody Map<String, Object> join(@Valid @RequestBody UserModel um, final BindingResult bindingResult) {
		System.out.println("UserModel : "+um);
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
			System.out.println("bindingResult : "+bindingResult.getAllErrors());
		}
		return userService.join(um);
	}
	
}
