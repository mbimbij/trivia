package com.adaptionsoft.games.trivia.game.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Order(-1)
public class FrontendRedirectionControllerAdvice {
    @ExceptionHandler({
            NoResourceFoundException.class,
    })
    public Object handleStaticResourceNotFound(final Exception ex, HttpServletRequest req) {
        if (req.getRequestURI().startsWith("/api") || req.getRequestURI().startsWith("/index.html"))
            return this.getApiResourceNotFoundBody(ex, req);
        else {
            return "forward:/index.html";
        }
    }

    private ResponseEntity<String> getApiResourceNotFoundBody(Exception ex, HttpServletRequest req) {
        return new ResponseEntity<>("%s - Not Found !!".formatted(req.getRequestURI()), HttpStatus.NOT_FOUND);
    }
}
