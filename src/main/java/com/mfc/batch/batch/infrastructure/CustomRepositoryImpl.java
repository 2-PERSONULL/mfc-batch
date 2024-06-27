package com.mfc.batch.batch.infrastructure;

import static com.mfc.batch.batch.domain.QPostSummary.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository {
	private final JPAQueryFactory query;

	@Override
	public Slice<Long> getPostList(Pageable page, List<String> partners) {
		List<Long> result = query.select(postSummary.postId)
				.from(postSummary)
				.where(partnerIdIn(partners))
				.offset(page.getOffset())
				.limit(page.getPageSize() + 1)
				.orderBy(postSummary.bookmarkCnt.desc(), postSummary.id.desc())
				.fetch();

		boolean hasNext = false;
		if(result.size() > page.getPageSize()) {
			result.remove(page.getPageSize());
			hasNext = true;
		}

		return new SliceImpl<>(result, page, hasNext);
	}

	private BooleanExpression partnerIdIn(List<String> partners) {
		return partners != null ? postSummary.partnerId.in(partners) : null;
	}
}
