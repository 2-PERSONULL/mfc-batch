package com.mfc.batch.batch.vo.resp;

import java.util.List;

import lombok.Getter;

@Getter
public class PostListRespVo {
	private List<Long> posts;
	private boolean isLast;
}
