package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name="ConfigurableGreetingConfig")
public @interface ConfigurableGreetingConfig {

	@AttributeDefinition(name="Greeting Prefix", description="Configurable property Greeting Prefix")
	String greetingPrefix() default "Hello";
}
