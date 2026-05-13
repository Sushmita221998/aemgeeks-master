package com.aem.geeks.core.handlers;

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
		ResourceChangeListener.PATHS + "=/content/dam/aemgeeks", ResourceChangeListener.CHANGES + "=ADDED",
		ResourceChangeListener.CHANGES + "=CHANGED", ResourceChangeListener.CHANGES + "=REMOVED" })
public class ResourceChangeListenerTest3 implements ResourceChangeListener {

	private static final Logger log = LoggerFactory.getLogger(ResourceChangeListenerTest3.class);

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
				String path = resourceChange.getPath();
				String userId = resourceChange.getUserId();
				ResourceChange.ChangeType changeType = resourceChange.getType();

				log.info("Details:" + path, userId, changeType);

				// Added here as it not not get logged because resource will be null
				if (changeType == ResourceChange.ChangeType.REMOVED) {
					log.info("Asset REMOVED at: {} by user: {}", path, userId);
					continue;
				}

				Resource resource = resolver.getResource(path);
				// Resource metadata = resource.getChild("jcr:content");
				if (resource != null) {
					ValueMap valueMapData = resource.getValueMap();

					// log.info("valueMap data"+valueMapData);

					// String title = valueMapData.get("jcr:title", String.class);
//					String mimeType = valueMapData.get("jcr:mimeType", String.class);

					String mixinTypeData = valueMapData.get("jcr:mixinTypes", String.class);
//					String title = valueMapData.get("dc:title", String.class);
//					String description = valueMapData.get("dc:description", String.class);
					switch (changeType) {
					case ADDED:
						log.info("Asset ADDED at: {} by user: {}", path, userId);
						break;
					case CHANGED:
						log.info("Asset MODIFIFED at: {} by user: {} changed properties: {}", path, userId,
								mixinTypeData);
						break;
					default:
						break;

					}
				}
			}
		} catch (Exception e) {
			log.info("Exception:{}", e.getMessage());
		} finally {
			if (resolver != null) {
				resolver.close();
			}
		}
	}

}
