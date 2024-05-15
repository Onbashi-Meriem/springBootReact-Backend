package com.hoaxify.ws.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.hoaxify.ws.user.User;
import com.hoaxify.ws.user.validation.UniqueEmail;

public class UserCreate {
    @NotBlank(message = "{hoaxify.constraint.username.notblank}")
    @Size(min=4, max=255)
    String username;



    @NotBlank
    @Email
    @UniqueEmail
    String email;


    @Size(min = 8, max = 255)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "{hoaxify.constraint.password.pattern}")
    String password;

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
