package com.hoaxify.ws.user;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.email.EmailService;
import com.hoaxify.ws.file.FileService;
import com.hoaxify.ws.user.validation.ActivationNotificationException;
import com.hoaxify.ws.user.validation.InvalidTokenException;
import com.hoaxify.ws.user.validation.NotUniqueEmailException;
import com.hoaxify.ws.user.dto.UserUpdate;
import com.hoaxify.ws.user.exception.NotFoundException;
import com.hoaxify.ws.configuration.CurrentUser;

@Service
public class UserService {

     @Autowired
     UserRepository userRepository;

     @Autowired
     EmailService emailService;

     @Autowired
     PasswordEncoder passwordEncoder;

     @Autowired
     FileService fileService;

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

     public Page<User> getAllUsers(Pageable pageable, CurrentUser currentUser) {
          if (currentUser == null) {
          return userRepository.findAll(pageable);
         }
         return userRepository.findByIdNot(currentUser.getId(), pageable);
     }

     public User getUserById(Long id) {
          User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
          return user;
     }

     public User findByEmail(String email) {
          return userRepository.findByEmail(email);
          
     }

     public User userUpdate(Long id, UserUpdate userUpdate) {
          User inDB = getUserById(id);
          inDB.setUsername(userUpdate.getUsername());

          if (userUpdate.getImage() != null) {
              
               String filename = fileService.saveBase64StringAsFile(userUpdate.getImage());
               inDB.setImage(filename);
                System.out.println("-----------"+inDB.getImage());
                
          }
          return userRepository.save(inDB);
     }
}
