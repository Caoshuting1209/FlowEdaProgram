package com.shuting.flowEdaProgram.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyOauth2UserDetails implements UserDetails {
    private MyOauth2User oauthUser;

    @Override
    public boolean isAccountNonExpired() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public boolean isEnabled() {
        return oauthUser.getStatus() == 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(oauthUser.getAuthorities().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return oauthUser.getPassword();
    }

    @Override
    public String getUsername() {
        return oauthUser.getUsername();
    }
}
