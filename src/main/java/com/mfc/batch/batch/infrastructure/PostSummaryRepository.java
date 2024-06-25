package com.mfc.batch.batch.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mfc.batch.batch.domain.PostSummary;

public interface PostSummaryRepository extends JpaRepository<PostSummary, Long> {
	Optional<PostSummary> findByPostId(Long postId);

	@Modifying
	@Query("delete from PostSummary ps where ps.postId = :postId")
	void deleteByPostId(@Param("postId") Long postId);
}
