package com.adaptionsoft.games.trivia.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
