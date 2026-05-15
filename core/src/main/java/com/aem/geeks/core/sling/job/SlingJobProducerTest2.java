package com.aem.geeks.core.sling.job;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = SlingJobProducerTest2.class, immediate = true)
public class SlingJobProducerTest2 {

	private static final Logger log = LoggerFactory.getLogger(SlingJobProducerTest2.class);

	@Reference
	private JobManager jobManager;

	@Reference
	private QueryBuilder queryBuilder;

	@Reference
	private ResourceResolverFactory factory;

	@Activate // This makes it run the moment the bundle starts
	protected void activate() {
		triggerJob();
	}

	protected void triggerJob() {

		log.info("Inside Trigger Job");
		ResourceResolver resolver = null;

		try {
			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(params);

			Map<String, Object> queryData = new HashMap<>();
			queryData.put("path", "/content/aemgeeks");
			queryData.put("type", "cq:Page");
			queryData.put("p.limit", "-1");

//			queryData.put("daterange.property", "jcr:content/cq:lastModified");
//			queryData.put("daterange.lowerBound", "-30d");

			Query query = queryBuilder.createQuery(PredicateGroup.create(queryData), resolver.adaptTo(Session.class));
			SearchResult result = query.getResult();

			for (Hit hit : result.getHits()) {

				String path = hit.getPath();
				ValueMap valueMapData = hit.getProperties();

				Calendar lastModifiedDate = valueMapData.get("cq:lastModified", Calendar.class);
				Map<String, Object> properties = new HashMap<>();
				properties.put("path", path);
				if (lastModifiedDate != null) {
					properties.put("lastModifiedDate", lastModifiedDate);
				}

				jobManager.addJob("aemgeeks/job/stale-check", properties);
				log.info("Job is Created for Path : /content/aemgeeks");

			}
		} catch (Exception e) {
			log.error("Exception occured:{}", e.getMessage());
		} finally {
			if (resolver != null)
				resolver.close();
		}

	}

}
