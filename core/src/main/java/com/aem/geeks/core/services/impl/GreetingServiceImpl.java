package com.aem.geeks.core.services.impl;

import org.osgi.service.component.annotations.Component;

import com.aem.geeks.core.services.GreetingService;

@Component(service=GreetingService.class, immediate=true)
public class GreetingServiceImpl implements GreetingService{

	@Override
	public String getGreeting(String name) {
		// TODO Auto-generated method stub
		return "Hello, <name>! Welcome to AEM.";
	}
	
	

}
