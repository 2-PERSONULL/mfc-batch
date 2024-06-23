package com.mfc.batch.batch.application;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.kafka.PostSummaryDto;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KafkaConsumer {
	private final PostSummaryRepository postSummaryRepository;
	private final RedisTemplate<String, Object> redisTemplate;

	private static final String POST_PREFIX = "post:like:";

	@KafkaListener(topics = "create-post", containerFactory = "postSummaryListener")
	public void createPostSummary(PostSummaryDto dto) {
		postSummaryRepository.save(PostSummary.builder()
				.postId(dto.getPostId())
				.bookmarkCnt(0)
				.build());
	}

	@KafkaListener(topics = "create-bookmark", containerFactory = "postSummaryListener")
	public void createBookmark(PostSummaryDto dto) {
		PostSummary summary = postSummaryRepository.findByPostId(dto.getPostId())
				.orElseGet(() -> postSummaryRepository.save(PostSummary.builder()
						.postId(dto.getPostId())
						.bookmarkCnt(0)
						.build()));

		String key = POST_PREFIX + dto.getPostId();
		redisTemplate.opsForValue().increment(key, 1);
		log.info("like count={}", redisTemplate.opsForValue().get(key));
	}

	@KafkaListener(topics = "delete-bookmark", containerFactory = "postSummaryListener")
	public void deleteBookmark(PostSummaryDto dto) {
		PostSummary summary = postSummaryRepository.findByPostId(dto.getPostId())
				.orElse(postSummaryRepository.save(PostSummary.builder()
						.postId(dto.getPostId())
						.bookmarkCnt(0)
						.build()));

		String key = POST_PREFIX + dto.getPostId();
		redisTemplate.opsForValue().decrement(key, 1);
		log.info("like count={}", redisTemplate.opsForValue().get(key));
	}
}
