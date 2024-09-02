package com.hoaxify.ws.error;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DisabledException.class,AccessDeniedException.class})

    ResponseEntity<ApiError> handleSecurityFrameworkExceptions(Exception exception,
            HttpServletRequest request) {
        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        if (exception instanceof DisabledException) {
            apiError.setStatus(401);
        }
        else if (exception instanceof AccessDeniedException) {
            apiError.setStatus(403);
        }
        
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }
}
