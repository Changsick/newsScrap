package com.gridone.scraping.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		System.out.println("##########access Deny == 403###########");
		System.out.println("deny msg : "+accessDeniedException.getMessage());
		
		Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
		System.out.println("authentication : "+authentication.getAuthorities());
		
		response.sendRedirect(request.getContextPath() + "/deny");
	}

}
