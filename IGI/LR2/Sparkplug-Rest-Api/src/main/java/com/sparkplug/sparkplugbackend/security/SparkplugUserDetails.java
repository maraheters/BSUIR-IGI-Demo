package com.sparkplug.sparkplugbackend.security;

import com.sparkplug.sparkplugbackend.user.model.entity.SparkplugUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SparkplugUserDetails implements UserDetails {
    private final SparkplugUser user;

    public SparkplugUserDetails(SparkplugUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<UserAuthority> authorities = new ArrayList<>();
//        authorities.add(new UserAuthority(user.getAuthority()));
//        return authorities;
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    public SparkplugUser getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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
}
