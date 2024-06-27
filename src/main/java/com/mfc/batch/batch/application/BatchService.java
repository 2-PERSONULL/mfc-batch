package com.mfc.batch.batch.application;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mfc.batch.batch.dto.resp.PartnerRankingRespDto;
import com.mfc.batch.batch.dto.resp.PartnerSummaryRespDto;
import com.mfc.batch.batch.dto.resp.PostListRespDto;
import com.mfc.batch.batch.dto.resp.PostSummaryRespDto;

public interface BatchService {
	PostSummaryRespDto getBookmarkCnt(Long postId);
	PartnerRankingRespDto getPartnerRanking();
	PartnerSummaryRespDto getPartnerSummary(String uuid);
	PostListRespDto getPostList(Pageable page, List<String> partners);
}
