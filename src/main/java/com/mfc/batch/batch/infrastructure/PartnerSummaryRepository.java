package com.mfc.batch.batch.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.batch.batch.domain.PartnerSummary;
import com.mfc.batch.batch.domain.PostSummary;

public interface PartnerSummaryRepository extends JpaRepository<PartnerSummary, Long> {
	Optional<PartnerSummary> findByPartnerId(String partnerId);
}
