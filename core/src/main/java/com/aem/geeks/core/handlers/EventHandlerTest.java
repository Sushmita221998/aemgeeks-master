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
		EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC })
//		,EventConstants.EVENT_FILTER + "=(path=/content/aemgeeks/*)" 

public class EventHandlerTest implements EventHandler {

	private static final Logger log = LoggerFactory.getLogger(EventHandlerTest.class);

	@Override
	public void handleEvent(Event event) {

		ReplicationAction action = ReplicationAction.fromEvent(event);

		if (action != null) {
			String path = action.getPath();
			String userId = action.getUserId();
			ReplicationActionType actionType = action.getType();

			if (actionType == ReplicationActionType.ACTIVATE) {
				log.info("Page{} was activated by {}", path, userId);
			} else if (actionType == ReplicationActionType.DEACTIVATE) {
				log.info("Page{} was deactivated by {}", path, userId);
			}
		}

	}

}
