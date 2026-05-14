package com.aem.geeks.core.workflows;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;

@Component(service = WorkflowProcess.class, immediate = true, property = {
		"process.label" + "=TestWorkflowProcessModel1" })
public class TestWorkflowProcess1 implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(TestWorkflowProcess1.class);

	@Reference
	ResourceResolverFactory factory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		log.info("Inside execute method");

		String payload = workItem.getWorkflowData().getPayload().toString();
		ResourceResolver resolver = null;

		if (payload.contains("jcr:content")) {

			try {
				Map<String, Object> params = new HashMap<>();
				params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
				resolver = factory.getServiceResourceResolver(params);
				Resource resource = resolver.getResource(payload);
				if (resource != null) {

					ModifiableValueMap map = resource.adaptTo(ModifiableValueMap.class);
					map.put("workflowStatus", "PROCESSED");
					log.info("Property Added");

					ValueMap valueMapData = resource.getValueMap();
					String workflowData = valueMapData.get("workflowStatus", String.class);
					log.info("Property Value Added is {}", workflowData);

				}

				resolver.commit();
				// IMPORTANT -------- Always Complete the workflow Session and Select Handler Advance CheckBox at Workflow Level
				workflowSession.complete(workItem, workflowSession.getRoutes(workItem, false).get(0));
			} catch (Exception e) {
				log.error("Exception occured: {}", e.getMessage());
			} finally {
				if (resolver != null)
					resolver.close();
			}
		}

	}

}
