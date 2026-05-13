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

import com.aem.geeks.core.config.TestSlingSchedulerConfig5;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = TestSlingSchedulerConfig5.class)
public class TestSlingScheduler5 implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(TestSlingScheduler5.class);

	@Reference
	Scheduler scheduler;

	@Reference
	ResourceResolverFactory factory;

	private String cronExp, schedulerName, pagePath;

	@Override
	public void run() {
		log.info("Inside Run method");
		logWarning();
	}

	@Activate
	protected void activate(TestSlingSchedulerConfig5 config) {
		this.cronExp = config.cronExp();
		this.schedulerName = config.schedulerName();
		this.pagePath = config.pagePath();

		ScheduleOptions options = scheduler.EXPR(cronExp);
		options.name(schedulerName);
		options.canRunConcurrently(false);
		scheduler.schedule(this, options);
	}

	@Deactivate
	protected void deactivate() {
		scheduler.unschedule(schedulerName);
	}

	protected void logWarning() {
		ResourceResolver resolver = null;
		Map<String, Object> param = new HashMap<>();
		param.put(ResourceResolverFactory.SUBSERVICE, "geeksserviceuser");
		try {
			resolver = factory.getServiceResourceResolver(param);
			Resource resource = resolver.getResource(pagePath);
			if (resource != null) {
				Iterator<Resource> iteratorData = resource.listChildren();
				int count=0;
				while (iteratorData != null && iteratorData.hasNext()) {
					Resource page = iteratorData.next();
					Resource jcrPageContent = page.getChild("jcr:content");

					if (jcrPageContent != null) {
						ValueMap pageProperties = jcrPageContent.getValueMap();
						Calendar lastModifiedCalendarDate = pageProperties.get("cq:lastModified", Calendar.class);

						LocalDate currentDate = LocalDate.now();

						if (lastModifiedCalendarDate != null) {
							LocalDate lastModifiedDate = lastModifiedCalendarDate.toInstant()
									.atZone(ZoneId.systemDefault()).toLocalDate();
							LocalDate dateCheck = currentDate.minusDays(30);

//							if (!lastModifiedDate.isAfter(dateCheck) && !lastModifiedDate.isBefore(currentDate)) {
							if (lastModifiedDate.isBefore(dateCheck)) {
								log.warn("Page {} has not been modified since {}", page, lastModifiedDate);
								count++;
							} else {
								log.warn("Page {} has been modified lately", page);
							}
						}
					}

				}
				log.info("Total Inactive Pages found:{}",count);
			}

		} catch (Exception e) {
			log.info("Exception:{}", e.getMessage());
		} finally {
			resolver.close();
		}
	}

}
