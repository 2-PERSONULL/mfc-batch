package com.mfc.batch.batch.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mfc.batch.batch.domain.PartnerSummary;

public interface PartnerSummaryRepository extends JpaRepository<PartnerSummary, Long> {
	Optional<PartnerSummary> findByPartnerId(String partnerId);

	@Query("SELECT ps FROM PartnerSummary ps " +
			"ORDER BY (ps.followerCnt * 0.4 + ps.coordinateCnt * 0.3 + ps.averageStar * 0.3) DESC")
	List<PartnerSummary> findPartnerRank(Pageable page);
}
