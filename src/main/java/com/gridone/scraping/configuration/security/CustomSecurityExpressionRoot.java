package com.gridone.scraping.configuration.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.WebSecurityExpressionRoot;

import com.gridone.scraping.mapper.UserMapper;
import com.gridone.scraping.model.LoginUserDetails;

public class CustomSecurityExpressionRoot extends WebSecurityExpressionRoot {

	public HttpServletRequest request;
	
	UserMapper userMapper;
	
	public CustomSecurityExpressionRoot(Authentication a, FilterInvocation fi) {
		super(a, fi);
		this.request = fi.getRequest();
	}
	
	public boolean isAdmin() {
		final LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
		for( GrantedAuthority authority : userDetails.getAuthorities() ) {
			if("ADMIN".equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isUser() {
		final LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
		for( GrantedAuthority authority : userDetails.getAuthorities() ) {
			if("USER".equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAuthority() {
		final LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
		for( GrantedAuthority authority : userDetails.getAuthorities() ) {
			if("USER".equals(authority.getAuthority()) || "ADMIN".equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}

	public UserMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	
}
