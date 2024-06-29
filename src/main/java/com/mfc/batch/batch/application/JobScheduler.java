package com.mfc.batch.batch.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PartnerRanking;
import com.mfc.batch.batch.domain.PartnerSummary;
import com.mfc.batch.batch.infrastructure.PartnerRankingRepository;
import com.mfc.batch.batch.infrastructure.PartnerSummaryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JobScheduler {
	private final JobLauncher jobLauncher;
	private final Job postSummaryJob;
	private final Job partnerSummaryJob;

	private final PartnerSummaryRepository partnerSummaryRepository;
	private final PartnerRankingRepository partnerRankingRepository;

	public JobScheduler(JobLauncher jobLauncher,
			@Qualifier("postSummaryJob") Job postBookmarkJob,
			@Qualifier("partnerSummaryJob") Job partnerJobConfig,
			PartnerSummaryRepository partnerSummaryRepository,
			PartnerRankingRepository partnerRankingRepository) {
		this.jobLauncher = jobLauncher;
		this.postSummaryJob = postBookmarkJob;
		this.partnerSummaryJob = partnerJobConfig;
		this.partnerSummaryRepository = partnerSummaryRepository;
		this.partnerRankingRepository = partnerRankingRepository;
	}

	@Scheduled(fixedRate = 30000)
	public void postJobSchedule() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("time", LocalDateTime.now())
				.toJobParameters();

		JobExecution postJob = jobLauncher.run(postSummaryJob, jobParameters);
	}

	@Scheduled(fixedRate = 30000)
	public void partnerJobSchedule() throws Exception {

		JobParameters jobParameters = new JobParametersBuilder()
				.addLocalDateTime("time", LocalDateTime.now())
				.toJobParameters();

		JobExecution postJob = jobLauncher.run(partnerSummaryJob, jobParameters);
	}

	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void partnerRanking() {
		partnerRankingRepository.deleteAll();

		Pageable pageable = PageRequest.of(0, 30);
		List<PartnerSummary> partners = partnerSummaryRepository.findPartnerRank(pageable);

		int rank = 1;
		List<PartnerRanking> ranking = new ArrayList<>();
		for (PartnerSummary partner : partners) {
			ranking.add(PartnerRanking.builder()
					.ranking(rank++)
					.partnerId(partner.getPartnerId())
					.coordinateCnt(partner.getCoordinateCnt())
					.followerCnt(partner.getFollowerCnt())
					.averageStar(partner.getAverageStar())
					.build());
		}

		partnerRankingRepository.saveAll(ranking);
	}
}
