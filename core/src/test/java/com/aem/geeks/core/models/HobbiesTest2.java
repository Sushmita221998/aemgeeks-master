package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class HobbiesTest2 {
	
	//NEW CORRECT APPROACH

	private Resource resource;

	private final AemContext context = new AemContext();

	@BeforeEach
	void setup() throws NoSuchFieldException {
		context.addModelsForClasses(Hobbies.class);
		resource=context.create().resource("/content/aemgeeks/us/en/testing", "hobbies", "Shopping");
	}

	@Test
	void test() throws Exception {
		Hobbies model = resource.adaptTo(Hobbies.class);
		assertNotNull(model);
		assertEquals("Shopping", model.getHobbies());
	}

}
