package com.mfc.batch.batch.application;

import static com.mfc.batch.common.response.BaseResponseStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.resp.PostSummaryRespDto;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;
import com.mfc.batch.common.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchServiceImpl implements BatchService {
	private final PostSummaryRepository postSummaryRepository;

	@Override
	public PostSummaryRespDto getBookmarkCnt(Long postId) {
		PostSummary summary = postSummaryRepository.findByPostId(postId)
				.orElseThrow(() -> new BaseException(POST_NOT_FOUND));

		return PostSummaryRespDto.builder()
				.postId(postId)
				.bookmarkCnt(summary.getBookmarkCnt())
				.build();
	}
}
