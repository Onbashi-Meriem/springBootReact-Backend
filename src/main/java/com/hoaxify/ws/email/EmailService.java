package com.hoaxify.ws.email;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.hoaxify.ws.configuration.HoaxifyProperties;


@Service
public class EmailService {

    JavaMailSenderImpl mailSender;

    @Autowired
    HoaxifyProperties hoaxifyProperties;

    @Autowired
    MessageSource messageSource;

    @PostConstruct
    public void initialize() {

        this.mailSender = new JavaMailSenderImpl();
        mailSender.setHost(hoaxifyProperties.getEmail().getHost());
        mailSender.setPort(hoaxifyProperties.getEmail().getPort());
        mailSender.setUsername(hoaxifyProperties.getEmail().getUsername());
        mailSender.setPassword(hoaxifyProperties.getEmail().getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
    }
    
    String activationEmail = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>${title}</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <h2>Welcome to Our Service!</h2>\n" +
            "    <h2>${title}</h2>\n" +
            "    <p>Dear ${name},</p>\n" +
            "    <p>Thank you for registering with us. Please click the link below to activate your account:</p>\n" +
            "    <p>\n" +
            "        <a href=\"${url}\" target=\"_blank\">${clickHere}</a>\n" +
            "    </p>\n" +
            "    <p>If you did not register for our service, please ignore this email.</p>\n" +
            "    <br>\n" +
            "    <p>Best regards,</p>\n" +
            "    <p>The Company Team</p>\n" +
            "</body>\n" +
            "</html>";

    public void sendActivationEmail(String email, String activationToken, String newUser) {
        var activationUrl = hoaxifyProperties.getClient().getHost() + "/activation/" + activationToken;
        var title = messageSource.getMessage("hoaxify.mail.user.created.title", null, LocaleContextHolder.getLocale());
        var clickHere = messageSource.getMessage("hoaxify.mail.click.hier", null, LocaleContextHolder.getLocale());
        var mailBody = activationEmail.replace("${url}", activationUrl)
                                .replace("${name}", newUser)
                                .replace("${title}", title)
                                .replace("${clickHere}",clickHere);
       

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);

        // SimpleMailMessage message = new SimpleMailMessage();
        try {
           message.setFrom(hoaxifyProperties.getEmail().getFrom());
        message.setTo(email);
        message.setSubject("Account Activation");
        message.setText(mailBody, true);  
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
       
        this.mailSender.send(mimeMessage);

    }

}
