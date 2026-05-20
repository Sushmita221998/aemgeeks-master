package com.aem.geeks.core.builders;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = QueryBuilderTest2.class, immediate = true)
public class QueryBuilderTest2 {

	// Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(QueryBuilderTest2.class);

	@Reference
	private ResourceResolverFactory factory;

	@Reference
	QueryBuilder builder;

	protected void findRecentAssets() {
		// Hardcoded for now
		String path = "/content/dam/aemgeeks";
		ResourceResolver resolver = null;

		try {
			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(params);

			Map<String, Object> queryMapData = new HashMap<String, Object>();

			// Path and Type
			queryMapData.put("path", "/content/dam/aemgeeks");
			queryMapData.put("type", "dam:Asset");

			// Date constraint: Modified in last 7 days
			queryMapData.put("daterange.property", "jcr:content/jcr:lastModified");
			queryMapData.put("daterange.lowerBound", "-7d"); // Shortcut for last 7 days

			// Ordering
			queryMapData.put("orderby", "@jcr:content/jcr:lastModified");
			queryMapData.put("orderby.sort", "desc");

			queryMapData.put("p.limit", "10");

			Query query = builder.createQuery(PredicateGroup.create(queryMapData), resolver.adaptTo(Session.class));
			SearchResult result = query.getResult();
			int count = 0;

			for (Hit hit : result.getHits()) {
				try {
					log.info("Asset found: {}", hit.getPath());
					count++;
				} catch (Exception e) {
					log.error("Asset Not found: {}", e.getMessage());
				}
			}
			log.info("Total recent assets: {}", count);

		} catch (Exception e) {
			log.error("Exception occured:{}", e.getMessage());
		} finally {
			if (resolver != null)
				resolver.close();
		}

	}
}
