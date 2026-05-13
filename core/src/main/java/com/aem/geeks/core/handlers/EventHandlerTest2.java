package com.aem.geeks.core.handlers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import com.day.cq.replication.ReplicationAction;

@Component(service = EventHandler.class, immediate = true, property = {
		EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC })

//		EventConstants.EVENT_TOPIC + "=" + SlingConstants.TOPIC_RESOURCE_ADDED,
//		EventConstants.EVENT_TOPIC + "=" + SlingConstants.TOPIC_RESOURCE_CHANGED,
//		EventConstants.EVENT_TOPIC + "=" + SlingConstants.TOPIC_RESOURCE_REMOVED
public class EventHandlerTest2 implements EventHandler {

	@Override
	public void handleEvent(Event event) {
		// DEPRECATED

	}

}
