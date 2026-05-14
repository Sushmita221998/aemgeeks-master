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
				if (resource != null) {
					//Get Metadata Properties
					Resource metadataResource = resource.getChild("jcr:content/metadata");
					if (metadataResource != null) {
						ValueMap metadata = metadataResource.getValueMap();

						String title = metadata.get("dc:title", String.class);
						String description = metadata.get("dc:description", String.class);
						String format = metadata.get("dc:format", String.class);
						String creator = metadata.get("dc:creator", String.class);

						log.info("Title: {}", title);
						log.info("Description: {}", description);
						log.info("Format: {}", format);
						log.info("Creator: {}", creator);

						switch (changeType) {
						case ADDED:
							log.info("Asset ADDED at: {} by user: {}", path, userId);
							break;
						case CHANGED:
							log.info("Asset MODIFIFED at: {} by user: {} changed properties: {}", path, userId, title,
									description, format, creator);
							break;
						default:
							break;

						}
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
