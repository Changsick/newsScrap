package com.gridone.scraping.configuration.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.gridone.scraping.mapper.UserMapper;
import com.gridone.scraping.model.LoginUserDetails;


public class ModuleSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
	
	UserMapper userMapper;
	
	public ModuleSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

	public boolean isUser() {
		final LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
		for( GrantedAuthority authority : userDetails.getAuthorities() ) {
			if("USER".equals(authority.getAuthority() ) ) {
				return true;
			}
		}
		return false;
	}

	public boolean isAdmin() {
		final LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
		for( GrantedAuthority authority : userDetails.getAuthorities() ) {
			if("ADMIN".equals(authority.getAuthority() ) ) {
				return true;
			}
		}
		return false;
	}

	@Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

	public UserMapper getUserMapper() {
		return userMapper;
	}

	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

    

}
