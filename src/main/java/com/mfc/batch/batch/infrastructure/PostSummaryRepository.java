package com.mfc.batch.batch.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mfc.batch.batch.domain.PostSummary;

public interface PostSummaryRepository extends JpaRepository<PostSummary, Long> {
	Optional<PostSummary> findByPostId(Long postId);
}
