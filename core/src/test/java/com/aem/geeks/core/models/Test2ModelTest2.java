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
class Test2ModelTest2 {

	private Page page;

	private Resource component;

	private final AemContext context = new AemContext();

	@BeforeEach
	public void setup() throws Exception {
		context.addModelsForClasses(Test2Model.class, Hobbies.class);
		page = context.create().page("/content/aemgeeks/us/en/testing");
		component = context.create().resource(page, "test2", "sling:resourceType",
				"/apps/aemgeeks/components/testing/test2", "id", "123", "name", "TestUser", "address", "XYZ, Japan");
		context.create().resource(component, "multifieldHobbiesSection/item0", "hobbies", "Gaming");

		context.create().resource(component, "multifieldHobbiesSection/item1", "hobbies", "Dancing");
	}

	@Test
	void test() throws NoSuchFieldException {
		Test2Model test2Model = component.adaptTo(Test2Model.class);

		assertNotNull(test2Model.getHobbiesList());
		assertEquals("123", test2Model.getId());
		assertEquals("TestUser", test2Model.getName());
		assertEquals("XYZ, Japan", test2Model.getAddress());

		Hobbies hobbies = new Hobbies();
		PrivateAccessor.setField(hobbies, "hobbies", "Shopping");
		assertEquals("Shopping", hobbies.getHobbies());

	}

}
