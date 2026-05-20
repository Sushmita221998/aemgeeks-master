package com.aem.geeks.core.workflows;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
		"process.label" + "=TestWorkflowModelProcess3" })
public class TestWorkflowProcess3 implements WorkflowProcess {

	// Not Tested in AEM

	private static final Logger log = LoggerFactory.getLogger(TestWorkflowProcess3.class);

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		String payload = workItem.getWorkflowData().getPayload().toString();
		ResourceResolver resolver = null;

		Map<String, Object> params = new HashMap<>();
		params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
		try {
			resolver = factory.getServiceResourceResolver(params);
			Resource resource = resolver.getResource(payload);
			if (resource != null) {
				Resource jcrData = resource.getChild("jcr:content");
				if (jcrData != null) {
					ValueMap valueMapData = jcrData.getValueMap();
					String jcrTitle = valueMapData.get("jcr:title", String.class);
					String jcrDescription = valueMapData.get("jcr:description", String.class);
					if (StringUtils.isBlank(jcrDescription)) {
						ModifiableValueMap modifiableValueMap = jcrData.adaptTo(ModifiableValueMap.class);
						if (modifiableValueMap != null) {
							String description = String.format("Default description for %s", jcrTitle);
							modifiableValueMap.put("jcr:description", description);
							resolver.commit();
							log.info("Property Added");
						} else {
							log.warn("ModifiableValueMap is Null");
						}
					}
				}
			}
			// NOTE: AEM auto-advances 'WorkflowProcess' components.
			// Do not call workflowSession.complete() manually here unless Handler Advance
			// is unchecked in the model.
			workflowSession.complete(workItem, workflowSession.getRoutes(workItem, false).get(0));
		} catch (Exception e) {
			log.error("Exception occured: {}", e.getMessage());
		} finally {
			if (resolver != null) {
				resolver.close();
			}
		}

	}

}
