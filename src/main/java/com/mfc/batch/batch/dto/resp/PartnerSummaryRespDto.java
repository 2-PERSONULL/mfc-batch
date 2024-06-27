package com.mfc.batch.batch.dto.resp;

import com.mfc.batch.batch.domain.PartnerRanking;
import com.mfc.batch.batch.domain.PartnerSummary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerSummaryRespDto {
	private String partnerId;
	private Integer followerCnt;
	private Integer coordinateCnt;
	private Double averageStar;

	public PartnerSummaryRespDto(PartnerRanking ranking) {
		this.partnerId = ranking.getPartnerId();
		this.followerCnt = ranking.getFollowerCnt();
		this.coordinateCnt = ranking.getCoordinateCnt();
		this.averageStar = ranking.getAverageStar();
	}
}
