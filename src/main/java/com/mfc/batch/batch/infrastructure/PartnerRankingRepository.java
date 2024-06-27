package com.mfc.batch.batch.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mfc.batch.batch.domain.PartnerRanking;

public interface PartnerRankingRepository extends JpaRepository<PartnerRanking, Long> {
	@Modifying
	@Query("DELETE FROM PartnerRanking pr")
	void deleteAll();
}
