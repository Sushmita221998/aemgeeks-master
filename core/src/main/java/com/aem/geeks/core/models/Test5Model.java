package com.aem.geeks.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Test5Model {

	//Not Tested in AEM
	
	@ValueMapValue
	private String title;

	@ValueMapValue
	private String description;

	@ValueMapValue
	private String publishedDate;

	@ValueMapValue
	private String author;

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public String getAuthor() {
		return author;
	}

	public String getSummary() {
		return title + " by " + author;
	}

}
