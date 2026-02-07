package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import junitx.util.PrivateAccessor;

@ExtendWith(AemContextExtension.class)
class Test3ModelTest2 {

	private Page page;

	private Resource component;

	private final AemContext context = new AemContext();

	@BeforeEach
	public void setup() throws Exception {
		context.addModelsForClasses(Test3Model.class, Hobbies.class);
		page = context.create().page("/content/aemgeeks/us/en/testing");
		component = context.create().resource(page, "test2", "sling:resourceType",
				"/apps/aemgeeks/components/testing/test2", "id", "123", "name", "TestUser", "address", "XYZ, Japan");
		context.create().resource(component, "multifieldHobbiesSection/item0", "hobbies", "Hockey");

		context.create().resource(component, "multifieldHobbiesSection/item1", "hobbies", "Singing");

		// 🔑 Set request resource
		context.currentResource(component);
	}

	@Test
	void test() throws NoSuchFieldException {
		// 🔥 Adapt from REQUEST (not resource)
		Test3Model test3Model = context.request().adaptTo(Test3Model.class);

		assertNotNull(test3Model.getHobbiesList());
		assertEquals("123", test3Model.getId());
		assertEquals("TestUser", test3Model.getName());
		assertEquals("XYZ, Japan", test3Model.getAddress());

		Hobbies hobbies = new Hobbies();
		PrivateAccessor.setField(hobbies, "hobbies", "Drawing");
		assertEquals("Drawing", hobbies.getHobbies());

	}

}
