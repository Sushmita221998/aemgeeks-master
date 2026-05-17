package com.aem.geeks.core.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.geeks.core.services.TestOSGIService1;

@Component(service = TestOSGIService1.class, immediate = true)
public class TestOSGIServiceImpl1 implements TestOSGIService1 {
	
	//Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(TestOSGIServiceImpl1.class);

	@Override
	public Map<String, String> getPageInfo(String path, ResourceResolver resolver) {

		Resource resource = resolver.getResource(path);
		Map<String, String> pageInfoData = new HashMap<String, String>();
		if (resource == null) {
			log.warn("Resource not found for path: {}", path);
			return pageInfoData;
		}
		else{

			Resource jcrData = resource.getChild("jcr:content");
			if (jcrData != null) {
				ValueMap valueMapData = jcrData.getValueMap();
				String title = valueMapData.get("jcr:title", String.class);
				String description = valueMapData.get("jcr:description", String.class);
				String lastModifiedBy = valueMapData.get("cq:lastModifiedBy", String.class);

				// Lengthy code
//				if (!StringUtils.isBlank(title)) {
//					pageInfoData.put("title", title);
//				} else {
//					pageInfoData.put("title", "");
//				}
//				if (!StringUtils.isBlank(description)) {
//					pageInfoData.put("description", description);
//				} else {
//					pageInfoData.put("description", "");
//				}
//				if (!StringUtils.isBlank(lastModifiedBy)) {
//					pageInfoData.put("lastModifiedBy", lastModifiedBy);
//				} else {
//					pageInfoData.put("lastModifiedBy", "");
//				}

				pageInfoData.put("title", StringUtils.defaultString(title)); // returns "" if null
				pageInfoData.put("description", StringUtils.defaultString(description));
				pageInfoData.put("lastModifiedBy", StringUtils.defaultString(lastModifiedBy));

			}

		}
		
		return pageInfoData;

	}

}
