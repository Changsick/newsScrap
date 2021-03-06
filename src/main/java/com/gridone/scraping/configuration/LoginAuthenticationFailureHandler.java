package com.gridone.scraping.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.gridone.scraping.model.UserModel;
import com.gridone.scraping.service.UserService;
import com.gridone.scraping.type.EnumActive;

@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final int maxFailCnt = 5;
	
	@Autowired
	UserService userService;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		UserModel user = userService.selectUser(request.getParameter("email"));
		
		if(user != null) {
			if(user.getActive().equals(EnumActive.ACTIVE) && user.getPasswordFailCnt() < maxFailCnt) {	
				int failCnt = user.getPasswordFailCnt() + 1;
				response.sendRedirect(request.getContextPath()+"/login?usererror="+failCnt+"&failCheckCount="+maxFailCnt);
			}else if(user.getActive().equals(EnumActive.ACTIVE) && user.getPasswordFailCnt() >= maxFailCnt) {
				response.sendRedirect(request.getContextPath()+"/login?lock&failCheckCount="+maxFailCnt);
			}else if(user.getActive().equals(EnumActive.INACTIVE)) {
				response.sendRedirect(request.getContextPath()+"/login?locked");
			}else if(user.getActive().equals(EnumActive.DELETE)) {				
				response.sendRedirect(request.getContextPath()+"/login?deleted");
			}
		}else {			
			response.sendRedirect(request.getContextPath()+"/login?error");
		}

	}

}
