package com.mfc.batch.batch.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.w3c.dom.stylesheets.LinkStyle;

import com.mfc.batch.batch.domain.PartnerRanking;

public interface PartnerRankingRepository extends JpaRepository<PartnerRanking, Long> {
	@Modifying
	@Query("DELETE FROM PartnerRanking pr")
	void deleteAll();

	@Query("SELECT pr FROM PartnerRanking pr ORDER BY pr.ranking")
	List<PartnerRanking> findOrderByRanking();
}
