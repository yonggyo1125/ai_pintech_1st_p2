package org.koreait.global.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 사용자 요청 정보 검증 실패시 주로 사용
 * 응답 코드는 400 - BAD_REQUEST로 고정
 */
public class BadRequestException extends CommonException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
