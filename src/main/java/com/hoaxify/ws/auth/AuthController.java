package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.exception.AuthenticationException;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;




@RestController
public class AuthController {

    @Autowired
    public AuthService authService;

    @PostMapping("/api/v1/auth")
    AuthResponse handleAuthentication(@Valid @RequestBody Credentials creds) throws AuthenticationException {
        return authService.authenticate(creds);
    }  
}
