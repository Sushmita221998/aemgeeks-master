package com.aem.geeks.core.sling.consumer;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = JobConsumer.class, immediate = true, property = {
		JobConsumer.PROPERTY_TOPICS + "=aemgeeks/job/stale-check" })
public class SlingJobConsumerTest2 implements JobConsumer {

	private static final Logger log = LoggerFactory.getLogger(SlingJobConsumerTest2.class);

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public JobResult process(Job job) {

		ResourceResolver resolver = null;
		try {
			String path = job.getProperty("path", String.class);
			Calendar lastModifiedCalendarDate = job.getProperty("lastModifiedDate", Calendar.class);

			LocalDate lastModifiedDate = null;
			if (lastModifiedCalendarDate != null) {
				lastModifiedDate = lastModifiedCalendarDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}

			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(params);

			Resource resource = resolver.getResource(path);
			if (resolver != null) {
				Resource jcrData = resource.getChild("jcr:content");
				if (jcrData != null) {
					ModifiableValueMap modifiableValueMap = jcrData.adaptTo(ModifiableValueMap.class);
					if (modifiableValueMap != null) {
						modifiableValueMap.put("reviewStatus", "STALE");

						log.info("INFO: Processing job — path: {} with lastModifiedDate: {}", path, lastModifiedDate);
						resolver.commit();
						return JobResult.OK;
					}
				}
			}
			log.warn("CANCEL: Resource not found or not modifiable: {}", path);
			return JobResult.CANCEL;

		} catch (Exception e) {
			log.info("Exception occured:{}", e.getMessage());
			return JobResult.FAILED;
		} finally {
			if (resolver != null)
				resolver.close();
		}

	}
}
