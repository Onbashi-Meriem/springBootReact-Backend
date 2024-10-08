package com.hoaxify.ws.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.hoaxify.ws.configuration.CurrentUser;
import com.hoaxify.ws.shared.GenericMessage;
import com.hoaxify.ws.shared.Messages;
import com.hoaxify.ws.user.dto.UserCreate;
import com.hoaxify.ws.user.dto.UserDTO;
import com.hoaxify.ws.user.dto.UserUpdate;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/api/v1/users")
    ResponseEntity<GenericMessage> createUser(@Valid @RequestBody UserCreate user) {
        userService.save(user.toUser());

        String message = Messages.getMessageForLocale("hoaxify.create.user.success.message",
                LocaleContextHolder.getLocale());

        return ResponseEntity.ok(new GenericMessage(message));
    }

    @PatchMapping("/api/v1/users/{token}/active")
    ResponseEntity<GenericMessage> activateUser(@PathVariable String token) {
        userService.activateUser(token);

        String message = Messages.getMessageForLocale("hoaxify.activate.user.success.message",
                LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new GenericMessage(message));
    }
    
    @GetMapping("/api/v1/users")
    ResponseEntity<Page<UserDTO>> getAllUsers(@PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal CurrentUser currentUser) {
        
        Page<User> users = userService.getAllUsers(pageable, currentUser);
        return ResponseEntity.ok(users.map(UserDTO::new));
    }
    
    @GetMapping("/api/v1/users/{user-id}")
    ResponseEntity<UserDTO> getUser(@PathVariable(name = "user-id") Long id) {

        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserDTO(user));
    }
    
    @PutMapping("/api/v1/users/{user-id}")
    @PreAuthorize("#id==principal.id")
    UserDTO updateUser(@PathVariable(name = "user-id") Long id,@Valid @RequestBody UserUpdate userUpdate) {
               
        return new UserDTO(userService.userUpdate(id, userUpdate));
   }

   

   
}
