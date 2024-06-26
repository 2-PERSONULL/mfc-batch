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
	PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, false, 404, "존재하지 않는 파트너입니다.");


	private final HttpStatusCode httpStatusCode;
	private final boolean isSuccess;
	private final int code;
	private final String message;
}