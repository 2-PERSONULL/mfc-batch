package com.mfc.batch.batch.application;

import com.mfc.batch.batch.dto.resp.PartnerRankingRespDto;
import com.mfc.batch.batch.dto.resp.PostSummaryRespDto;

public interface BatchService {
	PostSummaryRespDto getBookmarkCnt(Long postId);
	PartnerRankingRespDto getPartnerRanking();
}
