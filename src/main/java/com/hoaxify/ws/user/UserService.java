package com.hoaxify.ws.user;

import java.util.Properties;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

     @Autowired
     UserRepository userRepository;

     PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

     @Transactional(rollbackOn = MailException.class)
     public void save(User user) {
          try {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          user.setActivationToken(UUID.randomUUID().toString());
          userRepository.saveAndFlush(user);
          sendActivationEmail(user);  
          } catch (Exception e) {
               throw new NotUniqueEmailException();
          }
          
     }

     private void sendActivationEmail(User user) {
          SimpleMailMessage message = new SimpleMailMessage();
          message.setFrom("noreply@my-app.com");
          message.setTo(user.getEmail());
          message.setSubject("Account Activation");
          message.setText("http://127.0.0.1:5173/activation" + user.getActivationToken());
          getJavaMailSender().send(message);

     }
     
     public JavaMailSender getJavaMailSender() {
          JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
          mailSender.setHost("smtp.ethereal.email");
          mailSender.setPort(587);
          mailSender.setUsername("ewald.huel19@ethereal.email");
          mailSender.setPassword("rFSJcRxnn5EQ3g9QFJ");

          Properties properties = mailSender.getJavaMailProperties();
          properties.put("mail.smtp.starttls.enable", true);
          return mailSender;
     }

}
