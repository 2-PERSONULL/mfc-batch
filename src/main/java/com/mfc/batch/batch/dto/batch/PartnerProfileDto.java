package com.mfc.batch.batch.dto.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PartnerProfileDto {
	private String partnerId;
	private Integer postCnt;
	private Integer followerCnt;
	private Integer coordinateCnt;
}
