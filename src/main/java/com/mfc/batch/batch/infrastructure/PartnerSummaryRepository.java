package com.mfc.batch.batch.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.batch.batch.domain.PartnerSummary;

public interface PartnerSummaryRepository extends JpaRepository<PartnerSummary, Long> {
}
