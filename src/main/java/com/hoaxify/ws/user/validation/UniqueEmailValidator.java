package com.hoaxify.ws.user.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserRepository;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      
        User tempUser = userRepository.findByEmail(value);
        return tempUser == null;    
    }

}
