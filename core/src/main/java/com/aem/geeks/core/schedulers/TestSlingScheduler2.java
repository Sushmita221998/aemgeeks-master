package com.aem.geeks.core.schedulers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = TestSlingSchedulerConfig.class)
public class TestSlingScheduler2 implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(TestSlingScheduler2.class);

	private String cronExp;

	private String schedulerName;

	@Reference
	private Scheduler scheduler;

	@Reference
	private ResourceResolverFactory resolverFactory;

	ArrayList<Resource> finalPageTitlesList;

	@Override
	public void run() {
		LOG.info("Scheduler 2 is running at: {}", new Date().toString());
		ArrayList<String> pages = getPageTitles();
		LOG.info("Total pages found: {}", pages.size());
	}

	@Activate
	protected void activate(TestSlingSchedulerConfig config) {
		this.cronExp = config.cronExpression();
		this.schedulerName = config.schedulerName();
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
			Resource resource = resolver.getResource("/content/aemgeeks/us/en");
			if (resource != null) {
				Iterator<Resource> children = resource.listChildren();
				while (children.hasNext()) {
					Resource child = children.next();
					pageTitlesList.add(child.getName());
					LOG.info("Page found: {}", child.getName());
				}
			}
		} catch (Exception e) {
			LOG.info("Exception is:" + e.getMessage());
		} finally {
			if (resolver != null && resolver.isLive()) {
				resolver.close();
			}
		}
		return pageTitlesList;
	}

}
