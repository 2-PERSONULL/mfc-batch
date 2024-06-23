package com.mfc.batch.batch.dto.resp;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSummaryRespDto {
	private Long postId;
	private Integer bookmarkCnt;
}
