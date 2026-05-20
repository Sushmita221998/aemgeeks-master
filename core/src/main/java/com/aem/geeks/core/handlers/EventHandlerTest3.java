package com.aem.geeks.core.handlers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;

@Component(service = EventHandler.class, immediate = true, property = {
		EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC,
		EventConstants.EVENT_FILTER + "=(path=/content/aemgeeks/*)" })
public class EventHandlerTest3 implements EventHandler {

	// Not Tested in AEM
	private static final Logger log = LoggerFactory.getLogger(EventHandlerTest3.class);

	private int count = 0;

	@Override
	public void handleEvent(Event event) {

		ReplicationAction action = ReplicationAction.fromEvent(event);

		if (action != null) {

			String path = action.getPath();
			String userId = action.getUserId();

			ReplicationActionType actionType = action.getType();

			if (actionType == ReplicationActionType.DEACTIVATE) {
				count++;
				log.warn("Page {} path was deactivated by {} - check if intentional !", path, userId);
				log.info("Total Deactivations so far : {}", count);
			}

		}

	}

}
