package com.aem.geeks.core.schedulers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
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
import com.aem.geeks.core.config.TestSlingSchedulerConfig4;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = TestSlingSchedulerConfig4.class)
public class TestSlingScheduler4 implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(TestSlingScheduler4.class);

	private String cronExp;

	private String pagePath;

	private String schedulerName;

	@Reference
	private Scheduler scheduler;

	@Reference
	private ResourceResolverFactory factory;

	@Override
	public void run() {
		log.info("Inside Run method");
		getContentExpiryNotifier();

	}

	@Activate
	protected void activate(TestSlingSchedulerConfig config) {
		log.info("Inside Activate method");
		this.cronExp = config.cronExpression();
//		this.cronExp="0 0 9 * * ?";
		this.pagePath = config.pagePath();
		this.schedulerName = config.schedulerName();

		ScheduleOptions scheduleOptions = scheduler.EXPR(cronExp);
		scheduleOptions.name(schedulerName);
		scheduleOptions.canRunConcurrently(false);
		scheduler.schedule(this, scheduleOptions);
	}

	@Deactivate
	protected void deactivate() {
		scheduler.unschedule(schedulerName);
	}

	protected void getContentExpiryNotifier() {
		ResourceResolver resolver = null;
		try {
			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
			resolver = factory.getServiceResourceResolver(param);
			Resource resource = resolver.getResource(pagePath);
			if (resource != null) {
				Iterator<Resource> childData = resource.listChildren();

				while (childData.hasNext()) {
					Resource child = childData.next();
					Resource jcrContent = child.getChild("jcr:content");
					if (jcrContent != null) {
						ValueMap properties = jcrContent.getValueMap();
						Calendar lastModified = properties.get("cq:lastModified", Calendar.class);

						LocalDate today = LocalDate.now();

						// Option 1
						if (lastModified != null) {
							LocalDate lastModifiedDate = lastModified.toInstant().atZone(ZoneId.systemDefault())
									.toLocalDate();

							log.info("Last Modified Page {} at : {}", child.getPath(), lastModifiedDate);

							LocalDate threeDaysAgo = today.minusDays(3);

							//Last 3 days
							if (!lastModifiedDate.isBefore(threeDaysAgo) && !lastModifiedDate.isAfter(today)) {
								log.warn("WARNING: Page {} was last modified on {}", child.getPath(), lastModifiedDate);
							}
						}

						// Option 2
						String expiryDate = properties.get("expiryDate", String.class);
						if (expiryDate != null) {
							LocalDate expiry = LocalDate.parse(expiryDate);
							log.info("Expired Page {} at : {}", child.getPath(), expiry);
							LocalDate threeDaysLater = today.plusDays(3);

							//Next 3 days
							if (!expiry.isBefore(today) && !expiry.isAfter(threeDaysLater)) {
								log.warn("WARNING: Page {} is expiring on {}", child.getPath(), expiry);
							}
						}
					}

				}

			}
		} catch (Exception e) {
			log.info("Exception : {}", e.getMessage());
		} finally {
			if (resolver != null)
				resolver.close();
		}

	}

}
