package com.aem.geeks.core.handlers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
		ResourceChangeListener.PATHS + "=/content/aemgeeks", ResourceChangeListener.CHANGES + "=ADDED",
		ResourceChangeListener.CHANGES + "=CHANGED", ResourceChangeListener.CHANGES + "=REMOVED" })
public class ResourceChangeListenerTest4 implements ResourceChangeListener {

	private static final Logger log = LoggerFactory.getLogger(ResourceChangeListenerTest4.class);

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public void onChange(List<ResourceChange> resourceChangeList) {
		ResourceResolver resolver = null;

		try {
			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(params);

			for (ResourceChange resourceChange : resourceChangeList) {
				String userId = resourceChange.getUserId() != null ? resourceChange.getUserId() : "system";
				String path = resourceChange.getPath();
				ResourceChange.ChangeType changeType = resourceChange.getType();

				if (path.contains("rep:policy")) {
					log.info("Invalid rep:policy Path");
					continue;
				}

				log.info("User Id : {}, Path : {}, Change Type : {}", userId, path, changeType);

//				if (changeType.toString().equals("REMOVED")) {
				if (changeType == ResourceChange.ChangeType.REMOVED) {
					log.info("Resource Removed, User: {}, Path: {}", userId, path);
					continue;
				}

				Resource resource = resolver.getResource(path);
				if (resource != null) {
//					Get Root Data Node After JCR Content
//					Resource root = resource.getChild("root");
//					ValueMap valueMapData = root.getValueMap();
//					String type=valueMapData.get("sling:resourceType", String.class);
//					log.info("Root Data"+ type);

					ValueMap valueMapData = resource.getValueMap();
					String title = valueMapData.get("jcr:title", String.class);
					String primaryType = valueMapData.get("jcr:primaryType", String.class);
					Calendar lastModifiedCalendarDate = valueMapData.get("cq:lastModified", Calendar.class);
					LocalDate lastModifiedDate = null;
					if (lastModifiedCalendarDate != null) {
						lastModifiedDate = lastModifiedCalendarDate.toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
					}
					switch (changeType) {
					case ADDED:
						log.info("Resource Added, Path: {} User: {} JcrPrimaryType: {}", path, userId, primaryType);
						break;
					case CHANGED:
						log.info("Resource Changed, Path: {} User: {} Title: {} LastModifiedDate: {}", path, userId,
								title, lastModifiedDate);
						break;
					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception occured: {}", e.getMessage());
		} finally {
			if (resolver != null)
				resolver.close();
		}

	}

}
