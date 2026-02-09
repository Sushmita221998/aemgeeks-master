package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import junitx.util.PrivateAccessor;

class Test4ModelTest {

	private Test4Model test4Model = new Test4Model();

	private List<Hobbies> hobbiesList = new ArrayList<Hobbies>();

	@BeforeEach
	void setUp() throws Exception {

		// 1st Approach - Do not need a setter method for Hobbies class
		// STARTS HERE---
		Hobbies hobbies1 = mock(Hobbies.class);
		when(hobbies1.getHobbies()).thenReturn("Tennis");
		hobbiesList.add(hobbies1);

		Hobbies hobbies2 = mock(Hobbies.class);
		when(hobbies2.getHobbies()).thenReturn("Painting");
		hobbiesList.add(hobbies2);
		// ENDS HERE---

		PrivateAccessor.setField(test4Model, "id", "789");
		PrivateAccessor.setField(test4Model, "name", "TestUser2");
		PrivateAccessor.setField(test4Model, "address", "123 SYX, Korea");
		PrivateAccessor.setField(test4Model, "hobbiesList", hobbiesList);
	}

	@Test
	void test() throws NoSuchFieldException {
		assertNotNull(test4Model.getHobbiesList());
		assertEquals(2, test4Model.getHobbiesList().size());
		assertEquals("Tennis", test4Model.getHobbiesList().get(0).getHobbies());
		assertEquals("Painting", test4Model.getHobbiesList().get(1).getHobbies());
		assertEquals("789", test4Model.getId());
		assertEquals("TestUser2", test4Model.getName());
		assertEquals("123 SYX, Korea", test4Model.getAddress());

	}

}
