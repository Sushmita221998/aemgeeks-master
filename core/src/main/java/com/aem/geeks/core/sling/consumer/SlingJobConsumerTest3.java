package com.aem.geeks.core.sling.consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = SlingJobConsumerTest3.class, immediate = true, property = {
		JobConsumer.PROPERTY_TOPICS + "=/content/dam/assets/job3" })
public class SlingJobConsumerTest3 implements JobConsumer {

	// Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(SlingJobConsumerTest3.class);

	@Override
	public JobResult process(Job job) {

		String path = (String) job.getProperty("path");
		String action = (String) job.getProperty("action");
		log.info("Action: {}", action);

		if (StringUtils.isBlank(path)) {
			return JobResult.CANCEL;
		} else if (path.startsWith("/content/dam")) {
			log.info("Processing DAM asset: {}", path);
			return JobResult.OK;
		}
		return JobResult.CANCEL;

	}

}