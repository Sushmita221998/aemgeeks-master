package com.aem.geeks.core.sling.consumer;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true, property = {
		JobConsumer.PROPERTY_TOPICS + "=com/aemgeeks/page/job" })
public class SlingJobConsumerTest1 implements JobConsumer {

	private static final Logger log = LoggerFactory.getLogger(SlingJobConsumerTest1.class);

	@Override
	public JobResult process(Job job) {

		String path = (String) job.getProperty("path");
		String action = (String) job.getProperty("action");
		String user = (String) job.getProperty("triggeredBy");
		if (StringUtils.isBlank(path)) {
			return JobResult.CANCEL;
		} else {
			log.info("INFO: Processing job — path: {} action: {} by: {}", path, action, user);
			return JobResult.OK;
		}
	}

}
