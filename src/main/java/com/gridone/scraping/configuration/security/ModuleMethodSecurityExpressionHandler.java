package com.gridone.scraping.configuration.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.gridone.scraping.mapper.UserMapper;

@Component
public class ModuleMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
	private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
	
	@Autowired
	UserMapper userMapper;
	
	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
		ModuleSecurityExpressionRoot root = new ModuleSecurityExpressionRoot(authentication);
		root.setPermissionEvaluator(getPermissionEvaluator());
		root.setTrustResolver(this.trustResolver);
		root.setRoleHierarchy(getRoleHierarchy());
		
		root.setUserMapper(userMapper);
		return root;
	}

}
