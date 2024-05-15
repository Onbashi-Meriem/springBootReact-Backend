package com.hoaxify.ws.user;




import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/api/v1/users")
    ResponseEntity<?> createUser(@Valid @RequestBody UserCreate user) {
        System.err.println("--------->"+LocaleContextHolder.getLocale().getLanguage());
        userService.save(user.toUser());
       
        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message",  LocaleContextHolder.getLocale());

        return ResponseEntity.ok(new GenericMessage(message));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
        apiError.setMessage(message);
        apiError.setStatus(400);
        // Map<String, String> validationErrors = new HashMap<>();
        // System.out.println(exception.getBindingResult().getFieldError());

        // for (var fieldError : exception.getBindingResult().getFieldErrors()) {
        //     validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        // }
        var validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (existing, replacing) -> existing));
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.badRequest().body(apiError);
    }
    
    // @ExceptionHandler(NotUniqueEmailException.class)
    // ResponseEntity<ApiError> handleNotUniqueEmailException(NotUniqueEmailException exception){
    //       ApiError apiError = new ApiError();
    //       apiError.setPath("/api/v1/users");
    //      String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
    //     apiError.setMessage(message);
    //     apiError.setStatus(400);

    //     Map<String, String> validationErrors = new HashMap();
    //      String messageForEmail = Messages.getMessageForLocale("hoaxify.constraint.email.not-unique", LocaleContextHolder.getLocale());
    //     validationErrors.put("email", messageForEmail);
    //     apiError.setValidationErrors(validationErrors);

    //     return ResponseEntity.badRequest().body(apiError);
    // }
}
