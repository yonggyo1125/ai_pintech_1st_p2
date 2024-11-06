package org.koreait.global.exceptions.script;

import org.koreait.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class AlertException extends CommonException {
    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }
}
