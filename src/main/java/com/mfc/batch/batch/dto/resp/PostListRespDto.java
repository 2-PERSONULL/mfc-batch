package com.mfc.batch.batch.dto.resp;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListRespDto {
	private List<Long> posts;
	private Boolean isLast;
}
