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

@ExtendWith(AemContextExtension.class)
class Test4ModelTest2 {

	private Page page;

	private Resource component;

	private final AemContext context = new AemContext();

	@BeforeEach
	public void setup() throws Exception {

		context.addModelsForClasses(Test4Model.class, Hobbies.class);
		page = context.create().page("/content/aemgeeks/us/en/testing");
		component = context.create().resource(page, "test2", "sling:resourceType",
				"/apps/aemgeeks/components/testing/test2", "id", "789", "name", "TestUser2", "address",
				"123 SYX, Korea");
		context.create().resource(component, "multifieldHobbiesSection/item0", "hobbies", "Tennis");

		context.create().resource(component, "multifieldHobbiesSection/item1", "hobbies", "Painting");
	}

	@Test
	void test() {
		Test4Model test4Model = component.adaptTo(Test4Model.class);

		assertNotNull(test4Model.getHobbiesList());
		assertEquals("789", test4Model.getId());
		assertEquals("TestUser2", test4Model.getName());
		assertEquals("123 SYX, Korea", test4Model.getAddress());

		assertEquals(2, test4Model.getHobbiesList().size());
		assertEquals("Tennis", test4Model.getHobbiesList().get(0).getHobbies());
		assertEquals("Painting", test4Model.getHobbiesList().get(1).getHobbies());

	}

}
