package com.kmccol1.to_do_app.security;

import com.kmccol1.to_do_app.Models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails
{
    private Integer id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Integer id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities)
    {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList() // Use roles if available
        );
    }

    public Integer getId()
    {
        return id;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true; // Implement as needed
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true; // Implement as needed
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true; // Implement as needed
    }

    @Override
    public boolean isEnabled()
    {
        return true; // Implement as needed
    }
}