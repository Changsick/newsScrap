package com.gridone.scraping.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		String destUri = request.getRequestURI();
		if(authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
			if(destUri.contains("login")) { // 인증된 사용자가 login 페이지 url을 요청할 경우 redirect
				try {
					response.sendRedirect(request.getContextPath()+"/main");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "login";
	}
	
	@GetMapping("/textmining")
	public String textminingPage() {
		return "textmining";
	}
}
