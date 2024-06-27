package com.mfc.batch.batch.presentation;

import static com.mfc.batch.common.response.BaseResponseStatus.*;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mfc.batch.batch.application.BatchService;
import com.mfc.batch.batch.vo.resp.PartnerRankingRespVo;
import com.mfc.batch.batch.vo.resp.PartnerSummaryRespVo;
import com.mfc.batch.batch.vo.resp.PostListRespVo;
import com.mfc.batch.batch.vo.resp.PostSummaryRespVo;
import com.mfc.batch.common.exception.BaseException;
import com.mfc.batch.common.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
@Tag(name = "batch", description = "배치 서비스 컨트롤러")
public class BatchController {
	private final BatchService batchService;
	private final ModelMapper modelMapper;

	@GetMapping("/bookmark/{postId}")
	@Operation(summary = "포스팅 별 좋아요 개수 조회 API", description = "포스팅 별 좋아요 개수")
	public BaseResponse<PostSummaryRespVo> getBookmarkCnt(@PathVariable Long postId) {
		return new BaseResponse<>(modelMapper.map(
				batchService.getBookmarkCnt(postId), PostSummaryRespVo.class));
	}

	@GetMapping("/ranking")
	@Operation(summary = "파트너 랭킹 목록 조회 API", description = "파트너 랭킹 목록 조회 (id + 기준값)")
	public BaseResponse<PartnerRankingRespVo> getPartnerRanking() {
		return new BaseResponse<>(modelMapper.map(
				batchService.getPartnerRanking(), PartnerRankingRespVo.class));
	}

	@GetMapping("/partners/summary")
	@Operation(summary = "파트너 집계 데이터 조회 API", description = "팔로워 수, 코디 매칭 수, 평균 별점")
	public BaseResponse<PartnerSummaryRespVo> getPartnerSummary(
			@RequestHeader(value = "partnerId", defaultValue = "") String partnerId) {
		checkUuid(partnerId);
		return new BaseResponse<>(modelMapper.map(
				batchService.getPartnerSummary(partnerId), PartnerSummaryRespVo.class));
	}

	@GetMapping("/explore")
	@Operation(summary = "포스팅 좋아요 순 정렬 조회 API", description = "좋아요 순 정렬 + 카테고리 필터링")
	public BaseResponse<PostListRespVo> getPosts(Pageable page, @RequestParam(required = false) List<String> partners) {
		return new BaseResponse<>(modelMapper.map(
				batchService.getPostList(page, partners), PostListRespVo.class));
	}

	private void checkUuid(String uuid) throws BaseException {
		if(!StringUtils.hasText(uuid)) {
			throw new BaseException(NO_REQUIRED_HEADER);
		}
	}
}
