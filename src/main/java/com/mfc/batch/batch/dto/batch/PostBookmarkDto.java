package com.mfc.batch.batch.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostBookmarkDto {
	private Long postId;
	private Integer bookmarkCnt;
}
