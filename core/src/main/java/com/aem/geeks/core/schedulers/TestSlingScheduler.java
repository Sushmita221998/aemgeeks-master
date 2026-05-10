package com.aem.geeks.core.schedulers;

import java.util.Date;

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

@Component(service=Runnable.class, immediate=true)
@Designate(ocd=TestSlingSchedulerConfig.class)
public class TestSlingScheduler implements Runnable{
	
	private static final Logger LOG = LoggerFactory.getLogger(TestSlingScheduler.class);

	@Reference
	private Scheduler scheduler;
	
	private String cronExp;
	
	String schedulerName="Test the Scheduler";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		LOG.info("Scheduler is running at: {}", new Date().toString());
	}
	
	@Activate
	protected void activate(TestSlingSchedulerConfig config) {
		cronExp=config.cronExpression();
		ScheduleOptions scheduleOptions=scheduler.EXPR(cronExp);
		scheduleOptions.name(schedulerName);
		scheduleOptions.canRunConcurrently(false);
		scheduler.schedule(this, scheduleOptions);
	}
	
	@Deactivate
	protected void deactivate() {
		scheduler.unschedule(schedulerName);
	}

}
