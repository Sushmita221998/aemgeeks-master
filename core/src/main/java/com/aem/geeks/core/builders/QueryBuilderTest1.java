package com.aem.geeks.core.builders;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component(service = QueryBuilderTest1.class, immediate = true)
public class QueryBuilderTest1 {

	private static final Logger log = LoggerFactory.getLogger(QueryBuilderTest1.class);

	@Reference
	private QueryBuilder queryBuilder;

	public void searchPages(ResourceResolver resolver) {
		// 1. Define the Query Map
		Map<String, String> map = new HashMap<>();

		// Path and Type
		map.put("path", "/content/aemgeeks");
		map.put("type", "cq:Page");

		// Property constraint: Title is not empty
		map.put("1_property", "jcr:content/jcr:title");
		map.put("1_property.operation", "exists");

		// Date constraint: Modified in last 30 days
		map.put("daterange.property", "jcr:content/cq:lastModified");
		map.put("daterange.lowerBound", "-30d"); // Shortcut for last 30 days

		// Ordering
		map.put("orderby", "@jcr:content/jcr:title");
		map.put("orderby.sort", "asc");

		// Remove pagination limits (AEM defaults to 10 hits otherwise)
		map.put("p.limit", "-1");

		// 2. Execute the Query
		Query query = queryBuilder.createQuery(PredicateGroup.create(map), resolver.adaptTo(Session.class));
		SearchResult result = query.getResult();

		// 3. Log Results
		int count = 0;
		for (Hit hit : result.getHits()) {
			try {
				log.info("INFO: Page found — path: {}", hit.getPath());
				count++;
			} catch (Exception e) {
				log.error("Error reading query hit", e);
			}
		}

		log.info("INFO: Total pages found: {}", count);
		
		//Test using URL: http://localhost:4502/bin/querybuilder.json?path=/content/aemgeeks&type=cq:Page&p.limit=-1
	}
}
