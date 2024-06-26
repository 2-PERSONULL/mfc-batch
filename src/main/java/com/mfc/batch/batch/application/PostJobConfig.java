package com.mfc.batch.batch.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.batch.PostBookmarkDto;
import com.mfc.batch.batch.dto.kafka.PostSummaryDto;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;
import com.mfc.batch.common.exception.BaseException;
import com.mfc.batch.common.response.BaseResponseStatus;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@Transactional
public class PostJobConfig {
	private final EntityManagerFactory emf;
	private final RedisTemplate<String, Object> redisTemplate;
	private final PostSummaryRepository postSummaryRepository;

	@Bean
	public Job postSummaryJob(JobRepository jobRepository, PlatformTransactionManager txm) {
		return new JobBuilder("postSummary-job", jobRepository)
				.start(step(jobRepository, txm))
				.build();
	}

	@JobScope
	@Bean(name = "postStep")
	public Step step(JobRepository jobRepository, PlatformTransactionManager txm) {
		return new StepBuilder("postSummary-step", jobRepository)
				.<PostBookmarkDto, PostSummary>chunk(1000, txm)
				.reader(postSummaryItemReader())
				.processor(postSummaryProcessor())
				.writer(postSummaryWriter())
				.allowStartIfComplete(true)
				.build();
	}

	@Bean(name = "postReader")
	@StepScope
	public ItemReader<PostBookmarkDto> postSummaryItemReader() {
		Set<String> keys = scanKeys("post:like:*");

		List<PostBookmarkDto> bookmarkList = new ArrayList<>();
		for (String key : keys) {
			Long postId = Long.valueOf(key.replace("post:like:", ""));
			Integer bookmarkCnt = Integer.valueOf((String)redisTemplate.opsForValue().get(key));

			bookmarkList.add(new PostBookmarkDto(postId, bookmarkCnt));

			redisTemplate.delete(key);
		}
		return new ListItemReader<>(bookmarkList);
	}

	@Bean(name = "postProcessor")
	@StepScope
	public ItemProcessor<PostBookmarkDto, PostSummary> postSummaryProcessor() {
		return dto -> {
			PostSummary summary = postSummaryRepository.findByPostId(dto.getPostId())
					.orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));

			return PostSummary.builder()
					.id(summary.getId())
					.postId(dto.getPostId())
					.bookmarkCnt(summary.getBookmarkCnt() + dto.getBookmarkCnt())
					.build();
		};
	}

	@Bean(name = "postWriter")
	@StepScope
	public JpaItemWriter<PostSummary> postSummaryWriter() {
		return new JpaItemWriterBuilder<PostSummary>()
				.entityManagerFactory(emf)
				.build();
	}

	// @Bean(name = "postSummaryJob")
	// public Job postSummaryJob(JobRepository jobRepository, PlatformTransactionManager txm) {
	// 	return new JobBuilder("postSummaryJob", jobRepository)
	// 			.start(postSummaryStep(jobRepository, txm))
	// 			.build();
	// }
	//
	// @Bean(name = "postSummaryStep")
	// public Step postSummaryStep(JobRepository jobRepository, PlatformTransactionManager txm) {
	// 	return new StepBuilder("postSummaryStep", jobRepository)
	// 			.tasklet(postSummaryTasklet(), txm)
	// 			.allowStartIfComplete(true)
	// 			.build();
	// }
	//
	// @Bean(name = "postSummaryTasklet")
	// public Tasklet postSummaryTasklet() {
	// 	return (contribution, chunkContext) -> {
	// 		Set<String> keys = scanKeys("post:like:*");
	//
	// 		for (String key : keys) {
	// 			Long postId = Long.valueOf(key.replace("post:like:", ""));
	// 			Integer bookmarkCnt = Integer.valueOf(String.valueOf(redisTemplate.opsForValue().get(key)));
	//
	// 			PostSummary summary = postSummaryRepository.findByPostId(postId)
	// 					.orElseThrow(() -> new BaseException(BaseResponseStatus.POST_NOT_FOUND));
	//
	// 			postSummaryRepository.save(PostSummary.builder()
	// 					.id(summary.getId())
	// 					.postId(postId)
	// 					.bookmarkCnt(summary.getBookmarkCnt() + bookmarkCnt)
	// 					.build());
	//
	// 			redisTemplate.delete(key);
	// 		}
	// 		return RepeatStatus.FINISHED;
	// 	};
	// }

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
