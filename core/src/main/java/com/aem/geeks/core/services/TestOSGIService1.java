package com.aem.geeks.core.services;

import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;

public interface TestOSGIService1 {

	//Not Tested in AEM
	Map<String, String> getPageInfo(String path, ResourceResolver resolver);
}
