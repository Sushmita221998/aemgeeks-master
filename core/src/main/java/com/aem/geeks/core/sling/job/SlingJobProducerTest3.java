package com.aem.geeks.core.sling.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SlingJobProducerTest3.class, immediate = true)
public class SlingJobProducerTest3 {

	// Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(SlingJobProducerTest3.class);

	@Reference
	JobManager manager;

	@Activate
	protected void activate() {
		triggerAssetJob("/content/dam/assets/test");
	}

	public void triggerAssetJob(String path) {

		Map<String, Object> mapData = new HashMap<>();
		mapData.put("path", path);
		mapData.put("action", "process");
		mapData.put("triggeredBy", "system");

		manager.addJob("/content/dam/assets/job3", mapData);
		log.info("Job added for path:{}", path);
	}
}
