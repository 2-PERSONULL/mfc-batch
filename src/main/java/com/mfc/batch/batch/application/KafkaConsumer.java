package com.mfc.batch.batch.application;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.CreatePostDto;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class KafkaConsumer {
	private final PostSummaryRepository postSummaryRepository;

	@KafkaListener(topics = "create-post", containerFactory = "createPostDtoListener")
	public void createPostSummary(CreatePostDto dto) {
		postSummaryRepository.save(PostSummary.builder()
				.postId(dto.getPostId())
				.bookmarkCnt(0)
				.build());
	}
}
