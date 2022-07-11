package com.tempter.urlshortener.exception;

import com.tempter.urlshortener.exception.exceptions.URLShortenerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({URLShortenerException.class})
    public String handleURLExceptions(Exception e) {
        log.error(e.getMessage());
        return e.getMessage();
    }

}
