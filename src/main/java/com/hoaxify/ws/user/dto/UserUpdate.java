package com.hoaxify.ws.user.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.hoaxify.ws.user.validation.FileType;

public class UserUpdate {
    @NotBlank(message = "{hoaxify.constraint.username.notblank}")
    @Size(min=4, max=255)
    String username;

    @Lob
    @FileType(types={"jpeg","png"})
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
}
