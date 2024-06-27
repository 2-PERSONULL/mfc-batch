package com.mfc.batch.batch.infrastructure;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CustomRepository {
	Slice<Long> getPostList(Pageable page, List<String> partners);
}
