package org.koreait.global.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * 모든 예외의 공통
 * 새롭게 정의하는 예외는 모두 이 예외를 상속 받아 정의한다.
 *
 */
@Getter
@Setter
public class CommonException extends RuntimeException {
    private boolean errorCode;
    private HttpStatus status;
    private Map<String, List<String>> errorMessages;

    public CommonException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR); // 기본 응답 코드는 500
    }

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
