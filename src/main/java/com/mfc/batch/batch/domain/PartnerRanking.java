package com.mfc.batch.batch.domain;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@BatchSize(size = 50)
public class PartnerRanking {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ranking_seq")
	@SequenceGenerator(name = "ranking_seq", sequenceName = "ranking_seq", allocationSize = 30)
	private Long id;
	private Integer ranking;
	private String partnerId;
	private Double averageStar;
	private Integer coordinateCnt;
	private Integer followerCnt;

	@Builder
	public PartnerRanking(Integer ranking, String partnerId, Double averageStar, Integer coordinateCnt,
			Integer followerCnt) {
		this.ranking = ranking;
		this.partnerId = partnerId;
		this.averageStar = averageStar;
		this.coordinateCnt = coordinateCnt;
		this.followerCnt = followerCnt;
	}
}
