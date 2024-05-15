package com.hoaxify.ws.user;



import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;

import com.hoaxify.ws.error.ApiError;
import com.hoaxify.ws.shared.GenericMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class UserController {
    @Autowired
    UserService userService;


    @PostMapping("/api/v1/users")
    ResponseEntity<?> createUser(@Valid @RequestBody User user) {

        userService.save(user);
        return ResponseEntity.ok(new GenericMessage("User is created"));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> handleMethodArgNotValidEx(MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();
        apiError.setPath("/api/v1/users");
        apiError.setMessage("Validation Error");
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
    //     apiError.setPath("/api/v1/users");
    //     apiError.setMessage("Validation error");
    //     apiError.setStatus(400);

    //     Map<String, String> validationErrors = new HashMap();
    //     validationErrors.put("email", "Email is in use");
    //     apiError.setValidationErrors(validationErrors);

    //     return ResponseEntity.badRequest().body(apiError);
    // }
}
