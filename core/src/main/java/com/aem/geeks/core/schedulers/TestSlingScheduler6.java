package com.aem.geeks.core.schedulers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Runnable.class, immediate = true)
public class TestSlingScheduler6 implements Runnable {
	
	//Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(TestSlingScheduler6.class);

	private String cronExp = "0 0 0/6 * * ?";

	private String schedulerName = "Test Sling Scheduler 6";

	// Hardcoded for now
	private String path = "/content/aemgeeks/";

	@Reference
	private Scheduler scheduler;

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public void run() {

		searchAllPages(path);
	}

	@Activate
	protected void activate() {

		ScheduleOptions options = scheduler.EXPR(cronExp);
		options.name(schedulerName);
		options.canRunConcurrently(false);
		scheduler.schedule(this, options);
	}

	@Deactivate
	protected void deactivate() {
		scheduler.unschedule(schedulerName);
	}

	private void searchAllPages(String path) {
		ResourceResolver resolver = null;
		try {
			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(params);
			Resource resource = resolver.getResource(path);
			if (resource != null) {
				Iterator<Resource> iteratorData = resource.listChildren();
				int count = 0;
				while (iteratorData != null && iteratorData.hasNext()) {
					Resource childResource = iteratorData.next();
					Resource jcrData = childResource.getChild("jcr:content");
					if (jcrData != null) {
						ValueMap valueMapData = jcrData.getValueMap();
						String jcrTitle = valueMapData.get("jcr:title", String.class);
						count++;
						if (StringUtils.isBlank(jcrTitle)) {
							log.warn("WARN: Page {} has no title!", childResource.getPath());
						}
					}
				}
				log.info("Total Pages found: {}", count);
			}
		} catch (Exception e) {
			log.error("Exception occured: {}", e.getMessage());
		} finally {
			if (resolver != null)
				resolver.close();
		}
	}
}
