package com.gridone.scraping.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.stereotype.Component;

import com.gridone.scraping.mapper.UserMapper;

@Component
public class CustomWebSecurityExpressionHandler extends DefaultWebSecurityExpressionHandler {

	private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	@Autowired
	UserMapper userMapper;

	@Override
	protected SecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication, FilterInvocation fi) {
		CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication, fi);
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(this.trustResolver);
		root.setRoleHierarchy(getRoleHierarchy());
		
		root.setUserMapper(userMapper);
		
		return root;
	}

}
