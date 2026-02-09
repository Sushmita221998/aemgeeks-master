package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import junitx.util.PrivateAccessor;

@ExtendWith(MockitoExtension.class)
class Test2ModelTest {

	private String MULTIFIELD_CHILD_NODE_NAME = "multifieldHobbiesSection";

	@Mock
	private Resource resource;

	@Mock
	private Resource linkRootRes;

	private Test2Model test2Model = new Test2Model();

	@BeforeEach
	void setup() throws NoSuchFieldException {
		resource = mock(Resource.class);
		linkRootRes = mock(Resource.class);

		Resource childRes1 = mock(Resource.class);
		Resource childRes2 = mock(Resource.class);

		// 1st Approach - Do not need a setter method for Hobbies class
		// STARTS HERE---
		Hobbies hobbies1 = mock(Hobbies.class);
		when(hobbies1.getHobbies()).thenReturn("Chess");

		Hobbies hobbies2 = mock(Hobbies.class);
		when(hobbies2.getHobbies()).thenReturn("Reading");
		// ENDS HERE---

		// 2nd Approach -- Setter method required for Hobbies class
		// STARTS HERE---
		/*
		 * Hobbies hobbies1 = new Hobbies(); hobbies1.setHobbies("Chess");
		 * 
		 * Hobbies hobbies2 = new Hobbies(); hobbies2.setHobbies("Reading");
		 */
		// ENDS HERE---

		// 3rd Approach -- No Setter method required for Hobbies class. Using
		// PrivateAccessor
		// STARTS HERE---
		/*
		 * Hobbies hobbies1 = new Hobbies(); PrivateAccessor.setField(hobbies1,
		 * "hobbies", "Chess");
		 * 
		 * Hobbies hobbies2 = new Hobbies(); PrivateAccessor.setField(hobbies2,
		 * "hobbies", "Reading");
		 * 
		 */
		// ENDS HERE---

		PrivateAccessor.setField(test2Model, "resource", resource);
		PrivateAccessor.setField(test2Model, "id", "123");
		PrivateAccessor.setField(test2Model, "name", "TestUser");
		PrivateAccessor.setField(test2Model, "address", "XYZ, Japan");

		when(resource.getChild(MULTIFIELD_CHILD_NODE_NAME)).thenReturn(linkRootRes);
		when(linkRootRes.getChildren()).thenReturn(Arrays.asList(childRes1, childRes2));

		when(childRes1.adaptTo(Hobbies.class)).thenReturn(hobbies1);
		when(childRes2.adaptTo(Hobbies.class)).thenReturn(hobbies2);

	}

	@Test
	void test() throws NoSuchFieldException {
		test2Model.init();
		assertNotNull(test2Model.getHobbiesList());
		assertEquals(2, test2Model.getHobbiesList().size());
		assertEquals("Chess", test2Model.getHobbiesList().get(0).getHobbies());
		assertEquals("Reading", test2Model.getHobbiesList().get(1).getHobbies());
		assertEquals("123", test2Model.getId());
		assertEquals("TestUser", test2Model.getName());
		assertEquals("XYZ, Japan", test2Model.getAddress());

	}

}
