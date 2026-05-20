package com.aem.geeks.core.handlers;

import java.util.List;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
		ResourceChangeListener.PATHS + "=/content/dam/aemgeeks", ResourceChangeListener.CHANGES + "=ADDED",
		ResourceChangeListener.CHANGES + "=REMOVED" })
public class ResourceChangeListenerTest5 implements ResourceChangeListener {
	
	//Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(ResourceChangeListenerTest5.class);

	@Override
	public void onChange(List<ResourceChange> resourceChangeList) {

		for (ResourceChange resourceChange : resourceChangeList) {

			String path = resourceChange.getPath();
			if (path.contains("rep:policy") || path.contains("jcr:system")) {
				continue;
			}
			String user = resourceChange.getUserId();
			ResourceChange.ChangeType changeType = resourceChange.getType();
			switch (changeType) {

			case ADDED: {
				log.info("Asset Added: {} by  {}", path, user);
				break;
			}
			case REMOVED: {
				log.warn("WARN: Asset deleted: {} by {} - verify if intentional !", path, user);
				break;
			}
			default:
				break;
			}
		}

	}

}
