package com.aem.geeks.core.services.impl;

import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

import com.aem.geeks.core.config.ConfigurableGreetingConfig;
import com.aem.geeks.core.services.ConfigurableGreetingService;

@Component(service = ConfigurableGreetingService.class)
@Designate(ocd = ConfigurableGreetingConfig.class)
public class ConfigurableGreetingServiceImpl implements ConfigurableGreetingService {

	String prefix;

	StringBuilder strbuilder;
	@Activate
	protected void init(ConfigurableGreetingConfig config) {
		prefix = config.greetingPrefix();
	}

	@Override
	public String getGreeting(String name) {
		strbuilder=new StringBuilder().append(prefix).append(", ").append(name).append("! Welcome to AEM.");
		// TODO Auto-generated method stub
		return strbuilder.toString();
	}

}
