package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="Test Sling Scheduler Config 4")
public @interface TestSlingSchedulerConfig4 {

	@AttributeDefinition(name="Cron Expression", description="Give the Cron Expression")
	String cronExpression() default "0/10 * * * * ?";
	
	@AttributeDefinition(name="Scheduler name", description="Name of the Scheduler")
	String schedulerName() default "Test Sling Scheduler 4";
	
	@AttributeDefinition(name="Page Path", description="The Page Path")
	String pagePath() default "/content/aemgeeks/us/en";
	
}
