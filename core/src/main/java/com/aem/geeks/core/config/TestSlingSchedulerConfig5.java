package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Test Sling Scheduler Config 5")
public @interface TestSlingSchedulerConfig5 {

	@AttributeDefinition(name="Cron Exp", description="Give the cron expression")
	String cronExp() default "0 0 12 * * ?";
	
	@AttributeDefinition(name="Page Path", description="Path of the Page")
	String pagePath() default "/content/aemgeeks/us/en";
	
	@AttributeDefinition(name="Scheduler Name", description="Give the name of the Scheduler")
	String schedulerName() default "Test Sling Scheduler Config 5";

}
