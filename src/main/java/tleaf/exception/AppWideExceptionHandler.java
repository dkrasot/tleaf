package tleaf.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(DuplicateTweetException.class)
    public String handleDuplicateTweet() {
        return "error/duplicate";
    }
}
