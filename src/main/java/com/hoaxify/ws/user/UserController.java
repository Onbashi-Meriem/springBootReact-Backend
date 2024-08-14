package com.hoaxify.ws.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserProjection;
import com.hoaxify.ws.user.exception.NotFoundException;
import com.hoaxify.ws.user.validation.ActivationNotificationException;
import com.hoaxify.ws.user.validation.InvalidTokenException;
import com.hoaxify.ws.user.validation.NotUniqueEmailException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/api/v1/users")
    ResponseEntity<GenericMessage> createUser(@Valid @RequestBody UserCreate user) {
        userService.save(user.toUser());

        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message",
                LocaleContextHolder.getLocale());

        return ResponseEntity.ok(new GenericMessage(message));
    }

    @PatchMapping("/api/v1/users/{token}/active")
    ResponseEntity<GenericMessage> activateUser(@PathVariable String token) {
        userService.activateUser(token);

        String message = Messages.getMessageForLocale("hoaxify.activate.user.success.message",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new GenericMessage(message));
    }
    
    @GetMapping("/api/v1/users")
    ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 10) Pageable pageable) {
        System.out.println(pageable + "controller");
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users.map(UserDTO::new));
    }
    
    @GetMapping("/api/v1/users/{user-id}")
    ResponseEntity<UserDTO> getUser(@PathVariable(name = "user-id") Long id) {
     
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception) {
         System.out.println("_____________Validation Exception________________");
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
        apiError.setMessage(message);
        apiError.setStatus(400);
        // Map<String, String> validationErrors = new HashMap<>();
        // System.out.println(exception.getBindingResult().getFieldError());

        // for (var fieldError : exception.getBindingResult().getFieldErrors()) {
        // validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        // }
        var validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (existing, replacing) -> existing));
        apiError.setValidationErrors(validationErrors);
        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(NotUniqueEmailException.class)
    ResponseEntity<ApiError> handleNotUniqueEmailException(NotUniqueEmailException exception) {
        System.out.println("_____________Not Unique Email Exception________________");
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        String message = Messages.getMessageForLocale("hoaxify.error.validation", LocaleContextHolder.getLocale());
        apiError.setMessage(message);
        apiError.setStatus(400);

        Map<String, String> validationErrors = new HashMap();
        String messageForEmail = Messages.getMessageForLocale("hoaxify.constraint.email.not-unique",
                LocaleContextHolder.getLocale());
        validationErrors.put("email", messageForEmail);
        apiError.setValidationErrors(validationErrors);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ActivationNotificationException.class)
    ResponseEntity<ApiError> handleActivationNotificationException(ActivationNotificationException exception) {
        System.out.println("_____________Activation Notification Exception________________");
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(502);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(InvalidTokenException.class)
    ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception, HttpServletRequest request) {
        System.out.println("_____________Invalid Token Exception________________");
        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(400);

        return ResponseEntity.badRequest().body(apiError);
    }


    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ApiError> handleNotFoundUser(NotFoundException  exception, HttpServletRequest request) {

        ApiError apiError = new ApiError();
        apiError.setPath(request.getRequestURI());
        apiError.setMessage(exception.getMessage());
        apiError.setStatus(404);

        return ResponseEntity.badRequest().body(apiError);
    }
}
