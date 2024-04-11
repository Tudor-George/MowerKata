package com.mowitnow.tondeuse;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = null;
		try {
			if (args.length < 1) {
				logger.error("File path is mandatory !");
			} else {

				context = new AnnotationConfigApplicationContext("com.mowitnow.*");

				context.getAutowireCapableBeanFactory().autowireBeanProperties(Main.class,
						AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);

				Job job = (Job) context.getBean("mowitnowJob");

				JobParametersBuilder builder = new JobParametersBuilder();
				builder.addString("startDate", LocalDateTime.now().toString());
				builder.addString("inputFilePath", args[0]);

				JobLauncher joblauncher = (JobLauncher) context.getBean("jobLauncher");
				JobExecution je = joblauncher.run(job, builder.toJobParameters());
			}

		} catch (JobExecutionAlreadyRunningException e) {
			logger.error("An JobExecutionAlreadyRunningException occurred: ", e);
		} catch (JobRestartException e) {
			logger.error("An JobRestartException occurred: ", e);
		} catch (JobInstanceAlreadyCompleteException e) {
			logger.error("An JobInstanceAlreadyCompleteException occurred: ", e);
		} catch (JobParametersInvalidException e) {
			logger.error("An JobParametersInvalidException occurred: ", e);
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}
}
