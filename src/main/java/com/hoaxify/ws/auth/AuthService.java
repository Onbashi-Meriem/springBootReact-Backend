package com.hoaxify.ws.auth;

import com.hoaxify.ws.auth.exception.AuthenticationException;
import com.hoaxify.ws.auth.token.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.auth.dto.AuthResponse;
import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.UserService;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.auth.token.Token;

@Service
public class AuthService {
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    public AuthResponse authenticate(Credentials creds) throws AuthenticationException {
        User inDB = userService.findByEmail(creds.getEmail());
        if (inDB == null)
            throw new AuthenticationException();
        
        if (!passwordEncoder.matches(creds.getPassword(), inDB.getPassword()))
            throw new AuthenticationException();

        Token token = tokenService.createToken(inDB, creds);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setUser(new UserDTO(inDB));
        return authResponse;
        
    }

}
