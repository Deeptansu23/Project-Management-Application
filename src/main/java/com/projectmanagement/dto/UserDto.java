package com.projectmanagement.dto;


import com.projectmanagement.entity.User;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDto implements UserDetails {
    private String userId;
    private String username;
    private String password;
    Set<SimpleGrantedAuthority> authorities;
    boolean isAccountNonExpired;

    boolean isAccountNonLocked;

    boolean isCredentialsNonExpired;

    boolean isEnabled;

    public UserDto(User user) {
        Set<SimpleGrantedAuthority> authority= user.getPermissions().stream().map(role->new SimpleGrantedAuthority(role.getPermission())).collect(Collectors.toSet());    ;
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = authority;
        this.isAccountNonExpired = user.isAccountNonExpired();
        this.isAccountNonLocked = user.isAccountNonLocked();
        this.isCredentialsNonExpired = user.isCredentialsNonExpired();
        this.isEnabled = user.isEnabled();
    }

}
