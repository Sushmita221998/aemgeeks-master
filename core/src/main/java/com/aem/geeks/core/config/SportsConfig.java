package com.aem.geeks.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import javax.print.attribute.Attribute;

@ObjectClassDefinition(name = "Sports URL",
                        description = " Dynamic Url Update")
public @interface SportsConfig {

    @AttributeDefinition(name = "Path",
            description = "Path Url",
            type = AttributeType.STRING)
    public String path() default "/bin/content";
}
