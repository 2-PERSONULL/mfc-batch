package com.mfc.batch.batch.application;

import static com.mfc.batch.common.response.BaseResponseStatus.*;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PartnerSummary;
import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.infrastructure.PartnerSummaryRepository;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;
import com.mfc.batch.common.exception.BaseException;
import com.mfc.batch.common.response.BaseResponseStatus;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Transactional
public class PartnerJobConfig {
	private final RedisTemplate<String, Object> redisTemplate;
	private final PartnerSummaryRepository partnerSummaryRepository;

	@Bean(name = "partnerSummaryJob")
	public Job partnerSummaryJob(JobRepository jobRepository, PlatformTransactionManager txm) {
		return new JobBuilder("bookmark-cnt-job", jobRepository)
				.start(partnerSummaryStep(jobRepository, txm))
				.build();
	}

	@Bean(name = "partnerSummaryStep")
	public Step partnerSummaryStep(JobRepository jobRepository, PlatformTransactionManager txm) {
		return new StepBuilder("bookmark-cnt-step", jobRepository)
				.tasklet(partnerSummaryTasklet(), txm)
				.allowStartIfComplete(true)
				.build();
	}

	@Bean(name = "partnerSummaryTasklet")
	public Tasklet partnerSummaryTasklet() {
		return (contribution, chunkContext) -> {
			Set<String> keys = scanKeys("partner:*");

			for (String key : keys) {
				String partnerId = key.replace("partner:", "");

				Object post = redisTemplate.opsForHash().get(key, "postCnt");
				Object follower = redisTemplate.opsForHash().get(key, "followCnt");
				Object coordinate = redisTemplate.opsForHash().get(key, "coordinateCnt");

				Integer postCnt = post == null ? 0 :Integer.parseInt(String.valueOf(post));
				Integer followerCnt = follower == null ? 0 : Integer.parseInt(String.valueOf(follower));
				Integer coordinateCnt = coordinate == null ? 0 : Integer.parseInt(String.valueOf(coordinate)) ;

				PartnerSummary summary = partnerSummaryRepository.findByPartnerId(partnerId)
						.orElseThrow(() -> new BaseException(PARTNER_NOT_FOUND));

				partnerSummaryRepository.save(PartnerSummary.builder()
						.id(summary.getId())
						.partnerId(partnerId)
						.postCnt(summary.getPostCnt() + postCnt)
						.followerCnt(summary.getFollowerCnt() + followerCnt)
						.coordinateCnt(summary.getCoordinateCnt() + coordinateCnt)
						.averageStar(summary.getAverageStar())
						.build());

				redisTemplate.delete(key);
			}
			return RepeatStatus.FINISHED;
		};
	}

	private Set<String> scanKeys(String pattern) {
		Set<String> keys = new HashSet<>();
		ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();

		try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
				.getConnection()
				.scan(options)) {

			while (cursor.hasNext()) {
				keys.add(new String(cursor.next()));
			}
		} catch (Exception e) {
			// 예외 처리
			throw new RuntimeException("Failed to scan Redis keys", e);
		}
		return keys;
	}
}
