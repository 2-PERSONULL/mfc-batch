package com.mfc.batch.batch.vo.resp;

import lombok.Getter;

@Getter
public class PartnerSummaryRespVo {
	private String partnerId;
	private Integer followerCnt;
	private Integer coordinateCnt;
	private Double averageStar;
}
