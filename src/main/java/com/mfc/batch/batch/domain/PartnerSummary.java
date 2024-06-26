package com.mfc.batch.batch.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PartnerSummary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String partnerId;
	private Integer postCnt;
	private Integer followerCnt;
	private Double averageStar;
	private Integer coordinateCnt;

	@Builder
	public PartnerSummary(Long id, String partnerId, Integer postCnt, Integer followerCnt, Double averageStar,
			Integer coordinateCnt) {
		this.id = id;
		this.partnerId = partnerId;
		this.postCnt = postCnt;
		this.followerCnt = followerCnt;
		this.averageStar = averageStar;
		this.coordinateCnt = coordinateCnt;
	}
}
