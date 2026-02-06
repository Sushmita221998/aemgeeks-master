package com.aem.geeks.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = { SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Test3Model {

	@Inject
	private SlingHttpServletRequest request;

	private String MULTIFIELD_CHILD_NODE_NAME = "multifieldHobbiesSection";
	private static final Logger LOG = LoggerFactory.getLogger(Test3Model.class);

	@Inject
	private String id;

	@Inject
	private String name;

	@Inject
	private String address;

	private List<Hobbies> hobbiesList;

	// OLD APPROACH STARTS HERE --- USING SlingHttpServletRequest

	@PostConstruct
	protected void init() {
		LOG.info("Inside init method");
		if ((request != null) && (request.getResource() != null)
				&& (request.getResource().getChild(MULTIFIELD_CHILD_NODE_NAME) != null)) {
			hobbiesList = new ArrayList<Hobbies>();
			Resource linkRootRes = request.getResource().getChild(MULTIFIELD_CHILD_NODE_NAME);
			Iterable<Resource> resItr = linkRootRes.getChildren();
			for (Resource res : resItr) {
				Hobbies hobbies = res.adaptTo(Hobbies.class);
				createHobbiesList(hobbies, hobbiesList);
			}
		}
	}

	private void createHobbiesList(Hobbies hobbies, List<Hobbies> hobbiesList) {
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
