package com.aem.geeks.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Hobbies {

	@Inject
	private String hobbies;

//Only Required for one of the approach for Junit class
//	public void setHobbies(String hobbies) {
//		this.hobbies = hobbies;
//	}

	public String getHobbies() {
		return hobbies;
	}

}
