package com.hoaxify.ws.error;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.exception.NotFoundException;
import com.hoaxify.ws.user.validation.ActivationNotificationException;
import com.hoaxify.ws.user.validation.AuthorizationException;
import com.hoaxify.ws.user.validation.InvalidTokenException;
import com.hoaxify.ws.user.validation.NotUniqueEmailException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class,
            NotUniqueEmailException.class,
            ActivationNotificationException.class,
            InvalidTokenException.class,
            NotFoundException.class,
            AuthenticationException.class,
            AuthorizationException.class })
    ResponseEntity<ApiError> handleException(Exception exception,
            HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        if (exception instanceof MethodArgumentNotValidException) {
            String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
            apiError.setMessage(message);
            apiError.setStatus(400);
             Map<String, String> validationErrors = new HashMap<>();
            validationErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                            (existing, replacing) -> existing));
            apiError.setValidationErrors(validationErrors);
        } else if (exception instanceof NotUniqueEmailException) {
            apiError.setStatus(400);
            Map<String, String> validationErrors = new HashMap();
            String messageForEmail = Messages.getMessageForLocale("hoaxify.constraint.email.not-unique",
                    LocaleContextHolder.getLocale());
            validationErrors.put("email", messageForEmail);
            apiError.setValidationErrors(validationErrors);
            // apiError.setValidationErrors(((NotUniqueEmailException)exception).getValidationErrors());

        }
        else if(exception instanceof ActivationNotificationException){
            apiError.setStatus(502);
        }
        else if(exception instanceof InvalidTokenException){
            apiError.setStatus(400);
        }
        else if(exception instanceof NotFoundException){
            apiError.setStatus(404);
        }
        else if(exception instanceof AuthenticationException){
            apiError.setStatus(401);
        }
        else if(exception instanceof AuthorizationException){
            apiError.setStatus(403);
        }

        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

    // @ExceptionHandler(NotUniqueEmailException.class)
    // ResponseEntity<ApiError> handleNotUniqueEmailException(NotUniqueEmailException exception) {
    //     System.out.println("_____________Not Unique Email Exception________________");
    //     ApiError apiError = new ApiError();
    //     apiError.setPath("/api/v1/users");
    //     String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
    //     apiError.setMessage(message);
    //     apiError.setStatus(400);

    //     Map<String, String> validationErrors = new HashMap();
    //     String messageForEmail = Messages.getMessageForLocale("hoaxify.constraint.email.not-unique",
    //             LocaleContextHolder.getLocale());
    //     validationErrors.put("email", messageForEmail);
    //     apiError.setValidationErrors(validationErrors);

    //     return ResponseEntity.badRequest().body(apiError);
    // }

    // @ExceptionHandler(ActivationNotificationException.class)
    // ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException exception) {
    //     System.out.println("_____________Activation Notification Exception________________");
    //     ApiError apiError = new ApiError();
    //     apiError.setPath("/api/v1/users");
    //     apiError.setMessage(exception.getMessage());
    //     apiError.setStatus(502);

    //     return ResponseEntity.badRequest().body(apiError);
    // }

    // @ExceptionHandler(InvalidTokenException.class)
    // ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception, HttpServletRequest request) {
    //     System.out.println("_____________Invalid Token Exception________________");
    //     ApiError apiError = new ApiError();
    //     apiError.setPath(request.getRequestURI());
    //     apiError.setMessage(exception.getMessage());
    //     apiError.setStatus(400);

    //     return ResponseEntity.badRequest().body(apiError);
    // }

    // @ExceptionHandler(NotFoundException.class)
    // ResponseEntity<ApiError> handleNotFoundUser(NotFoundException exception, HttpServletRequest request) {

    //     ApiError apiError = new ApiError();
    //     apiError.setPath(request.getRequestURI());
    //     apiError.setMessage(exception.getMessage());
    //     apiError.setStatus(404);

    //     return ResponseEntity.badRequest().body(apiError);
    // }

    // @ExceptionHandler(AuthenticationException.class)
    // ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException exception) {

    //     ApiError apiError = new ApiError();
    //     apiError.setPath("/api/v1/auth");
    //     apiError.setMessage(exception.getMessage());
    //     apiError.setStatus(401);

    //     return ResponseEntity.badRequest().body(apiError);
    // }
}
