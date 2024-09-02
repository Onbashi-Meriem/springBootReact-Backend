package com.hoaxify.ws.configuration;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.hoaxify.ws.user.User;

public class CurrentUser implements UserDetails {

    Long id;
    String username;
    String password;
    boolean enabled;

    public CurrentUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.enabled = user.isActive();

    }
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
               return AuthorityUtils.createAuthorityList("ROLE_USER");
            }

            @Override
            public String getPassword() {
                return this.password;
            }

            @Override
            public String getUsername() {
                 return this.username;
            }

            @Override
            public boolean isAccountNonExpired() {
                 return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
              return true;
            }

            @Override
            public boolean isEnabled() {
                return this.enabled;
            }
}
