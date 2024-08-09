package com.hoaxify.ws.user.dto;

import org.springframework.beans.factory.annotation.Value;

public interface UserProjection {
   Long getId();
   String getUsername();

   String getEmail();

   @Value("#{target.image!=null?target.image:'default.png'}")
   String getImage();

   String getFirstName();

   @Value("#{target.getFirstName()}"+" "+"#{target.getLastName()}")
   String getFullName();  
}
