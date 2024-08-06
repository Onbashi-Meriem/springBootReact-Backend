package com.hoaxify.ws.user;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.email.EmailService;
import com.hoaxify.ws.user.validation.ActivationNotificationException;
import com.hoaxify.ws.user.validation.InvalidTokenException;
import com.hoaxify.ws.user.validation.NotUniqueEmailException;

@Service
public class UserService {

     @Autowired
     UserRepository userRepository;

     @Autowired
     EmailService emailService;

     PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     @Transactional(rollbackOn = MailException.class)
     public void save(User user) {
          System.out.println("_____________User Service________________");
          try {
               System.out.println("_____________save database________________");
               user.setPassword(passwordEncoder.encode(user.getPassword()));
               user.setActivationToken(UUID.randomUUID().toString());
               userRepository.save(user);
               emailService.sendActivationEmail(user.getEmail(), user.getActivationToken(), user.getUsername());
          } catch (DataIntegrityViolationException e) {
               System.out.println("_____________DataIntegrityViolationException________________");

               throw new NotUniqueEmailException();

          } catch (MailException exception) {
               System.out.println("_____________Mail Exception________________");
               throw new ActivationNotificationException();
          }

     }
     
     public void activateUser(String token) {
          User verificatedUser = userRepository.findByActivationToken(token);
           
          if (verificatedUser == null) {

             throw new InvalidTokenException();
          }
               verificatedUser.setActive(true);
               verificatedUser.setActivationToken(null);
               userRepository.save(verificatedUser);
     }

    

}
