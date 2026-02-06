package com.aem.geeks.core.models;

import java.util.List;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Test4Model {

	@Inject
	private String id;

	@Inject
	private String name;

	@Inject
	private String address;

	// NEW APPROACH STARTS HERE --- Using @ChildResource Annotation
	@ChildResource(name = "multifieldHobbiesSection")
	private List<Hobbies> hobbiesList;
	// NEW APPROACH ENDS HERE ---

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public List<Hobbies> getHobbiesList() {
		return hobbiesList;
	}

}