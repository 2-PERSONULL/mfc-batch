package com.mfc.batch.batch.application;

import static com.mfc.batch.common.response.BaseResponseStatus.*;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mfc.batch.batch.domain.PostSummary;
import com.mfc.batch.batch.dto.resp.PartnerRankingRespDto;
import com.mfc.batch.batch.dto.resp.PartnerSummaryRespDto;
import com.mfc.batch.batch.dto.resp.PostSummaryRespDto;
import com.mfc.batch.batch.infrastructure.PartnerRankingRepository;
import com.mfc.batch.batch.infrastructure.PartnerSummaryRepository;
import com.mfc.batch.batch.infrastructure.PostSummaryRepository;
import com.mfc.batch.common.exception.BaseException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchServiceImpl implements BatchService {
	private final PostSummaryRepository postSummaryRepository;
	private final PartnerSummaryRepository partnerSummaryRepository;
	private final PartnerRankingRepository partnerRankingRepository;

	@Override
	public PostSummaryRespDto getBookmarkCnt(Long postId) {
		PostSummary summary = postSummaryRepository.findByPostId(postId)
				.orElseThrow(() -> new BaseException(POST_NOT_FOUND));

		return PostSummaryRespDto.builder()
				.postId(postId)
				.bookmarkCnt(summary.getBookmarkCnt())
				.build();
	}

	@Override
	public PartnerRankingRespDto getPartnerRanking(Pageable page) {
		return PartnerRankingRespDto.builder()
				.partners(partnerRankingRepository.findAll(page).stream()
						.map(PartnerSummaryRespDto::new)
						.toList())
				.build();
	}
}
