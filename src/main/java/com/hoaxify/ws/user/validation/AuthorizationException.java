package com.hoaxify.ws.user.validation;

import org.springframework.context.i18n.LocaleContextHolder;

import com.hoaxify.ws.shared.Messages;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException() {
        super(Messages.getMessageForLocale("hoaxify.unauthorized.transaction", LocaleContextHolder.getLocale()));
    }
}
