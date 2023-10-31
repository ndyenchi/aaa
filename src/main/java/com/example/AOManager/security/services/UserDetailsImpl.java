package com.example.AOManager.security.services;

import com.example.AOManager.entity.UsersEntity;
import com.example.AOManager.entity.UserRoleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private String email;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(UsersEntity user) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (UserRoleEntity userRole :user.getUserRoleList()) {
            GrantedAuthority role = new SimpleGrantedAuthority(userRole.getRoleId().getName());
            authorities.add(role);
        }
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(email, user.email);
    }
}
