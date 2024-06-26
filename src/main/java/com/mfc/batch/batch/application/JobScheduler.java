package com.mfc.batch.batch.application;

import java.time.LocalDateTime;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JobScheduler {
	private final JobLauncher jobLauncher;
	private final Job postSummaryJob;
	private final Job partnerSummaryJob;

	public JobScheduler(JobLauncher jobLauncher,
			@Qualifier("postSummaryJob") Job postBookmarkJob,
			@Qualifier("partnerSummaryJob") Job partnerJobConfig) {
		this.jobLauncher = jobLauncher;
		this.postSummaryJob = postBookmarkJob;
		this.partnerSummaryJob = partnerJobConfig;
	}

	@Scheduled(cron = "0 */2 * * * *")
	public void postJobSchedule() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("time", LocalDateTime.now())
				.toJobParameters();

		JobExecution postJob = jobLauncher.run(postSummaryJob, jobParameters);
	}

	@Scheduled(cron = "0 */2 * * * *")
	public void partnerJobSchedule() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("time", LocalDateTime.now())
				.toJobParameters();

		JobExecution postJob = jobLauncher.run(partnerSummaryJob, jobParameters);
	}
}
