package com.hoaxify.ws.user.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface UniqueEmail {
    String message() default "{hoaxify.constraint.email.not-unique}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
    
}
