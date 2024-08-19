package com.hoaxify.ws.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class Credentials {
    @Email
    String email;

    @NotBlank(message = "{hoaxify.constraint.username.notblank}")
    String password;
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
