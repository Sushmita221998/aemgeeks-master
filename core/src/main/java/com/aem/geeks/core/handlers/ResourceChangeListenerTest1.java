package com.aem.geeks.core.handlers;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
		ResourceChangeListener.PATHS + "=/content/aemgeeks", ResourceChangeListener.CHANGES + "=ADDED",
		ResourceChangeListener.CHANGES + "=CHANGED", ResourceChangeListener.CHANGES + "=REMOVED"

})
public class ResourceChangeListenerTest1 implements ResourceChangeListener {

	private static final Logger log = LoggerFactory.getLogger(ResourceChangeListenerTest1.class);

	@Override
	public void onChange(List<ResourceChange> resourceChangeList) {
		for (ResourceChange resourceChange : resourceChangeList) {
			String path = resourceChange.getPath();
			ResourceChange.ChangeType changeType = resourceChange.getType();

			switch (changeType) {
			case ADDED:
				log.info("Resource Added at Path:{}", path);
				break;

			case CHANGED:
				log.info("Resource Changed at Path:{}", path);
				break;

			case REMOVED:
				log.info("Resource Removed at Path:{}", path);
				break;

			default:
				break;
			}

		}

	}
}
