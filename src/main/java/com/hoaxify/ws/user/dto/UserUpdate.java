package com.hoaxify.ws.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserUpdate {
    @NotBlank(message = "{hoaxify.constraint.username.notblank}")
    @Size(min=4, max=255)
    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
