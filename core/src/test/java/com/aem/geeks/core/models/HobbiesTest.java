package com.aem.geeks.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import junitx.util.PrivateAccessor;

class HobbiesTest {
	
	//OLD INCORRECT APPROACH

	@Test
	void test() throws Exception {
		Hobbies hobbies = new Hobbies();
		PrivateAccessor.setField(hobbies, "hobbies", "Shopping");
		assertEquals("Shopping", hobbies.getHobbies());
	}

}
