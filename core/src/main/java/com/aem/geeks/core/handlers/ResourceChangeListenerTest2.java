package com.aem.geeks.core.handlers;

import java.util.List;
import java.util.Set;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
		ResourceChangeListener.PATHS + "=/content/dam/aemgeeks", ResourceChangeListener.CHANGES + "=ADDED",
		ResourceChangeListener.CHANGES + "=CHANGED", ResourceChangeListener.CHANGES + "=REMOVED" })
public class ResourceChangeListenerTest2 implements ResourceChangeListener {

	private static final Logger log = LoggerFactory.getLogger(ResourceChangeListenerTest2.class);

	@Override
	public void onChange(List<ResourceChange> resourceChangeList) {
		for (ResourceChange resourceChange : resourceChangeList) {
			String path = resourceChange.getPath();
			ResourceChange.ChangeType changeType = resourceChange.getType();
			String userId = resourceChange.getUserId() != null ? resourceChange.getUserId() : "system";
			Set<String> properties = resourceChange.getChangedPropertyNames();

			switch (changeType) {
			case ADDED:
				log.info("Asset ADDED at: {} by user: {}", path, userId);
				break;
			case CHANGED:
				log.info("Asset MODIFIFED at: {} by user: {} changed properties: {}", path, userId,properties);
				break;
			case REMOVED:
				log.info("Asset REMOVED at: {} by user: {}", path, userId);
				break;
			default:
				break;
			}
		}

	}

}
