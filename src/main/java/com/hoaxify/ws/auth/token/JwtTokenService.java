package com.hoaxify.ws.auth.token;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.auth.dto.Credentials;
import com.hoaxify.ws.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
@Primary
public class JwtTokenService implements TokenService{

    @Override
    public Token createToken(User user, Credentials creds) {
        SecretKey key = Keys.hmacShaKeyFor("secret-must-be-at-least-32-chars".getBytes());
        String token = Jwts.builder().setSubject(Long.toString(user.getId())).signWith(key).compact();
        return new Token("Bearer", token);
       
    }

    @Override
    public User verifyToken(String authorizationHeader) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyToken'");
    }
    
}
