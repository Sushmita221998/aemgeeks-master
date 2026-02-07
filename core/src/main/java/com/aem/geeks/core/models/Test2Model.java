package com.aem.geeks.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Test2Model {

	@Self
	private Resource resource;

	private String MULTIFIELD_CHILD_NODE_NAME = "multifieldHobbiesSection";
	private static final Logger LOG = LoggerFactory.getLogger(Test2Model.class);

	@Inject
	private String id;

	@Inject
	private String name;

	@Inject
	private String address;

	private List<Hobbies> hobbiesList;

	// OLD APPROACH STARTS HERE --- USING RESOURCE

	@PostConstruct
	protected void init() {
		LOG.info("Inside init method");
		if ((resource != null) && (resource.getChild(MULTIFIELD_CHILD_NODE_NAME) != null)) {
			hobbiesList = new ArrayList<Hobbies>();
			Resource linkRootRes = resource.getChild(MULTIFIELD_CHILD_NODE_NAME);
			Iterable<Resource> resItr = linkRootRes.getChildren();
			for (Resource res : resItr) {
				Hobbies hobbies = res.adaptTo(Hobbies.class);
				createHobbiesList(hobbies, hobbiesList, resource);
			}
		}
	}

	private void createHobbiesList(Hobbies hobbies, List<Hobbies> hobbiesList, Resource res) {
		if (hobbies != null) {
			hobbiesList.add(hobbies);
		}
		LOG.info("List Size----" + hobbiesList.size());

	}

	// OLD APPROACH ENDS HERE ---

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
