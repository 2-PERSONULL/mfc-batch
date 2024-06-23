package com.mfc.batch.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {

	/**
	 * 200: 요청 성공
	 **/
	SUCCESS(HttpStatus.OK, true, 200, "요청에 성공했습니다."),

	POST_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "존재하지 않는 포스팅입니다."),
	NO_REQUIRED_HEADER(HttpStatus.BAD_REQUEST, false, 400, "헤더에 UUID 혹은 Role이 존재하지 않습니다."),
	BOOKMARK_CONFLICT(HttpStatus.CONFLICT, false, 409, "이미 좋아요 한 포스팅입니다."),
	FOLLOW_CONFLICT(HttpStatus.CONFLICT, false, 409, "이미 팔로우 한 파트너입니다.");


	private final HttpStatusCode httpStatusCode;
	private final boolean isSuccess;
	private final int code;
	private final String message;
}