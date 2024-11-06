package org.koreait.global.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 모든 예외의 공통
 * 새롭게 정의하는 예외는 모두 이 예외를 상속 받아 정의한다.
 *
 */
public class CommonException extends RuntimeException {
    private HttpStatus status;

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
