package com.mfc.batch.batch.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PostBookmarkDto {
	private Long postId;
	private String partnerId;
	private Integer bookmarkCnt;

	public PostBookmarkDto(Long postId, Integer bookmarkCnt) {
		this.postId = postId;
		this.bookmarkCnt = bookmarkCnt;
	}
}
