package com.gridone.scraping.configuration;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
//		String username = request.getParameter("email");
//		HttpSession session = request.getSession();
		redirectStrategy.sendRedirect(request, response, "/main");
//		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//		
//		if (session != null && (roles.contains("USER") || roles.contains("ADMIN"))){
//			session.setAttribute("role",roles.contains("USER") ? "USER" : "ADMIN");
//			session.setAttribute("username",username);
//			
//			redirectStrategy.sendRedirect(request, response, "/dailyNews");
//		}else {
//			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//			redirectStrategy.sendRedirect(request, response, "/login");
//		}

	}

}
