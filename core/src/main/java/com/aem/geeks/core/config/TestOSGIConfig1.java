package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Test OSGI Config 1", description = " Description for Test OSGI Config 1")
public @interface TestOSGIConfig1 {
	
	//Not Tested in AEM

	// To use Config in Service or any Class, use
	// @Designate(ocd=TestOSGIConfig1.class)

	@AttributeDefinition(name = "Service enabled", description = "Check if the service is enabled")
	boolean serviceEnabled() default true;

	@AttributeDefinition(name = "Service Name", description = "Provide the name for the service")
	String serviceName() default "AEM Geeks Service";

	@AttributeDefinition(name = "Max Entries", description = "Provide the Max Entries")
	int maxEntries() default 3;

	@AttributeDefinition(name = "Allowed Paths", description = "Provide the Allowed Paths")
	String[] allowedPaths() default { "/content/aemgeeks" };

}
