package com.aem.geeks.core.services.impl;

import org.osgi.service.component.annotations.Component;

import com.aem.geeks.core.services.UserService;

@Component(service=UserService.class,immediate=true)
public class UserServiceImpl implements UserService {
	
	StringBuilder stringBuilder;

	@Override
	public String getUserFullName(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return new StringBuilder().append(firstName).append(" ").append(lastName).toString();
	}
	
	

}
