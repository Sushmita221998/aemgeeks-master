package com.aem.geeks.core.workflows;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
		"process.label" + "=TestWorkflowProcessModel2" })
public class TestWorkflowProcess2 implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(TestWorkflowProcess2.class);

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public void execute(WorkItem workItem, WorkflowSession workFlowSession, MetaDataMap metaDataMap)
			throws WorkflowException {

		ResourceResolver resourceResolver = null;
		String payload = workItem.getWorkflowData().getPayload().toString();

		try {
			Map<String, Object> params = new HashMap<>();
			params.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resourceResolver = factory.getServiceResourceResolver(params);
			Resource resource = resourceResolver.getResource(payload);
			if (resource != null) {
				ValueMap valueMapData = resource.getValueMap();

				String jcrTitle = valueMapData.get("jcr:title", String.class);
				String jcrDescription = valueMapData.get("jcr:description", String.class);
				String lastModifiedBy = valueMapData.get("cq:lastModifiedBy", String.class);
//				Calendar lastModifiedCalendarDate = valueMapData.get("cq:lastModified", Calendar.class);
//				LocalDate lastModifiedDate = null;
//				if (lastModifiedCalendarDate != null) {
//					lastModifiedDate = lastModifiedCalendarDate.toInstant().atZone(ZoneId.systemDefault())
//							.toLocalDate();
//				}

				if (!StringUtils.isBlank(jcrTitle) && !StringUtils.isBlank(jcrDescription)) {
					log.info("INFO: Page valid, proceeding: {} title: {} modifiedBy:{}", payload, jcrTitle,
							lastModifiedBy);
					workFlowSession.complete(workItem, workFlowSession.getRoutes(workItem, false).get(0));
				} else if (StringUtils.isBlank(jcrTitle)) {
					log.warn("Validation failed - title empty for: {}", payload);
					workFlowSession.terminateWorkflow(workItem.getWorkflow());
					return;
				} else if (StringUtils.isBlank(jcrDescription)) {
					log.warn("WARN: Validation failed - description empty for: {}", payload);
					workFlowSession.terminateWorkflow(workItem.getWorkflow());
					return;
				}
			}

		} catch (Exception e) {
			log.error("Exception occured:{}", e.getMessage());
		} finally {
			if (resourceResolver != null)
				resourceResolver.close();
		}

	}

}
