package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import junitx.util.PrivateAccessor;

@ExtendWith(MockitoExtension.class)
class Test3ModelTest {

	private String MULTIFIELD_CHILD_NODE_NAME = "multifieldHobbiesSection";

	@Mock
	private Resource resource;

	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private Resource linkRootRes;

	private Test3Model test3Model = new Test3Model();

	@BeforeEach
	void setup() throws NoSuchFieldException {
		request = mock(SlingHttpServletRequest.class);
		resource = mock(Resource.class);
		linkRootRes = mock(Resource.class);

		Resource childRes1 = mock(Resource.class);
		Resource childRes2 = mock(Resource.class);

		// 1st Approach - Do not need a setter method for Hobbies class
		// STARTS HERE---
		Hobbies hobbies1 = mock(Hobbies.class);
		when(hobbies1.getHobbies()).thenReturn("Hockey");

		Hobbies hobbies2 = mock(Hobbies.class);
		when(hobbies2.getHobbies()).thenReturn("Singing");
		// ENDS HERE---

		PrivateAccessor.setField(test3Model, "request", request);
		PrivateAccessor.setField(test3Model, "id", "567");
		PrivateAccessor.setField(test3Model, "name", "TestUser1");
		PrivateAccessor.setField(test3Model, "address", "ABC, Germany");

		when(request.getResource()).thenReturn(resource);
		when(resource.getChild(MULTIFIELD_CHILD_NODE_NAME)).thenReturn(linkRootRes);
		when(linkRootRes.getChildren()).thenReturn(Arrays.asList(childRes1, childRes2));

		when(childRes1.adaptTo(Hobbies.class)).thenReturn(hobbies1);
		when(childRes2.adaptTo(Hobbies.class)).thenReturn(hobbies2);

	}

	@Test
	void test() throws NoSuchFieldException {
		test3Model.init();
		assertNotNull(test3Model.getHobbiesList());
		assertEquals(2, test3Model.getHobbiesList().size());
		assertEquals("Hockey", test3Model.getHobbiesList().get(0).getHobbies());
		assertEquals("Singing", test3Model.getHobbiesList().get(1).getHobbies());
		assertEquals("567", test3Model.getId());
		assertEquals("TestUser1", test3Model.getName());
		assertEquals("ABC, Germany", test3Model.getAddress());

	}

}
