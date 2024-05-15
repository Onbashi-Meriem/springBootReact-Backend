package com.hoaxify.ws.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

     @Autowired
     UserRepository userRepository;

     PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     public void save(User user) {

          user.setPassword(passwordEncoder.encode(user.getPassword()));
          user.setActivationToken(UUID.randomUUID().toString());
          userRepository.save(user);
     }

}
