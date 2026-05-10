package com.aem.geeks.core.schedulers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aem.geeks.core.config.TestSlingSchedulerConfig;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = TestSlingSchedulerConfig.class)
public class TestSlingScheduler3 implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TestSlingScheduler3.class);

	private String cronExp;

	private String schedulerName;

	private String pagePath;

	@Reference
	private Scheduler scheduler;

	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	Replicator replicator;

	@Override
	public void run() {
		LOG.info("Scheduler 3 is running at: {}", new Date().toString());
		ArrayList<String> pages = getPageTitles();
		LOG.info("Total pages published: {}", pages.size());
	}

	@Activate
	protected void activate(TestSlingSchedulerConfig config) {
		this.cronExp = config.cronExpression();
		this.schedulerName = config.schedulerName();
		this.pagePath = config.pagePath();
		ScheduleOptions schedulerOptions = scheduler.EXPR(cronExp);
		schedulerOptions.name(schedulerName);
		schedulerOptions.canRunConcurrently(false);
		scheduler.schedule(this, schedulerOptions);
	}

	@Deactivate
	protected void deactivate() {
		scheduler.unschedule(schedulerName);
	}

	protected ArrayList<String> getPageTitles() {
		ArrayList<String> pageTitlesList = new ArrayList<String>();
		ResourceResolver resolver = null;

		try {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = resolverFactory.getServiceResourceResolver(param);
			Session session = resolver.adaptTo(Session.class);
			Resource resource = resolver.getResource(pagePath);
			if (resource != null) {
				Iterator<Resource> children = resource.listChildren();
				while (children.hasNext()) {
					Resource child = children.next();
					pageTitlesList.add(child.getName());
					replicator.replicate(session, ReplicationActionType.ACTIVATE, child.getPath());
					LOG.info("Page published: {}", child.getName());
				}
			}
		} catch (Exception e) {
			LOG.error("Exception is: {}", e.getMessage());
		} finally {
			if (resolver != null && resolver.isLive()) {
				resolver.close();
			}
		}
		return pageTitlesList;
	}

}
