package com.gridone.scraping.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ModuleSecurityConfig extends GlobalMethodSecurityConfiguration {
		
	@Autowired
	private ModulePermissionEvaluator permissionEvaluator;
	
	@Autowired
	private ModuleMethodSecurityExpressionHandler expressionHandler;

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		expressionHandler.setPermissionEvaluator(permissionEvaluator);
		return expressionHandler;
    }
		
}
