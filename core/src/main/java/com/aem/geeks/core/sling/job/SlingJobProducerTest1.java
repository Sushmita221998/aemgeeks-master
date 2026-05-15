package com.aem.geeks.core.sling.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SlingJobProducerTest1.class, immediate = true)
public class SlingJobProducerTest1 {

	private static final Logger log = LoggerFactory.getLogger(SlingJobProducerTest1.class);

	@Reference
	private JobManager jobManager;
	
	@Activate // This makes it run the moment the bundle starts
    protected void activate() {
        triggerJob();
    }

	protected void triggerJob() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("path", "/content/aemgeeks/us/en");
		properties.put("triggeredBy", "admin");
		properties.put("action", "publish");

		jobManager.addJob("com/aemgeeks/page/job", properties);

		log.info("Job added for path: {}", "/content/aemgeeks/us/en");
	}

}
