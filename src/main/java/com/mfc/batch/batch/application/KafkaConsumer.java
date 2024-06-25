package com.mfc.batch.batch.application;

import static com.mfc.batch.common.response.BaseResponseStatus.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PartnerSummary;
import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.kafka.PartnerSummaryDto;
import com.mfc.batch.batch.dto.kafka.PostSummaryDto;
import com.mfc.batch.batch.infrastructure.PartnerSummaryRepository;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;
import com.mfc.batch.common.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KafkaConsumer {
	private final PostSummaryRepository postSummaryRepository;
	private final PartnerSummaryRepository partnerSummaryRepository;

	private final RedisTemplate<String, Object> redisTemplate;

	private static final String POST_PREFIX = "post:like:";
	private static final String PARTNER_PREFIX = "partner:";

	@KafkaListener(topics = "create-post", containerFactory = "postSummaryListener")
	public void createPostSummary(PostSummaryDto dto) {
		postSummaryRepository.save(PostSummary.builder()
				.postId(dto.getPostId())
				.bookmarkCnt(0)
				.build());

		String key = PARTNER_PREFIX + dto.getPartnerId();
		redisTemplate.opsForHash().increment(key, "postCnt", 1);
		log.info("postCnt={}", redisTemplate.opsForHash().get(key, "postCnt"));
	}

	@KafkaListener(topics = "delete-post", containerFactory = "postSummaryListener")
	public void deletePostSummary(PostSummaryDto dto) {
		postSummaryRepository.deleteByPostId(dto.getPostId());

		String key = PARTNER_PREFIX + dto.getPartnerId();
		redisTemplate.opsForHash().increment(key, "postCnt", -1);
		log.info("postCnt={}", redisTemplate.opsForHash().get(key, "postCnt"));
	}

	@KafkaListener(topics = "create-bookmark", containerFactory = "postSummaryListener")
	public void createBookmark(PostSummaryDto dto) {
		PostSummary summary = postSummaryRepository.findByPostId(dto.getPostId())
				.orElseThrow(() -> new BaseException(POST_NOT_FOUND));

		String key = POST_PREFIX + dto.getPostId();
		redisTemplate.opsForValue().increment(key, 1);
		log.info("like count={}", redisTemplate.opsForValue().get(key));
	}

	@KafkaListener(topics = "delete-bookmark", containerFactory = "postSummaryListener")
	public void deleteBookmark(PostSummaryDto dto) {
		PostSummary summary = postSummaryRepository.findByPostId(dto.getPostId())
				.orElseThrow(() -> new BaseException(POST_NOT_FOUND));

		String key = POST_PREFIX + dto.getPostId();
		redisTemplate.opsForValue().decrement(key, 1);
		log.info("like count={}", redisTemplate.opsForValue().get(key));
	}

	@KafkaListener(topics = "create-partner", containerFactory = "partnerSummaryListener")
	public void createPartner(PartnerSummaryDto dto) {
		partnerSummaryRepository.save(
				PartnerSummary.builder()
						.partnerId(dto.getPartnerId())
						.followerCnt(0)
						.postCnt(0)
						.coordinateCnt(0)
						.averageStar(0.0)
						.build()
		);
	}
}
