package com.hoaxify.ws.user.dto;

import com.hoaxify.ws.user.User;

public class UserDTO {
  
    Long id;
    String username;
    String email;
    String image;
    String fullName;

    public UserDTO(User user) {
        setId(user.getId());
        setUsername(user.getUsername());
        setEmail(user.getEmail());
        setImage(user.getImage());
        setFullName(user.getFirstName() + " " + user.getLastName());
    }

    public String getFirstName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image==null?"default.png":image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
