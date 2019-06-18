package com.softserveinc.itacademy.bikechampionship.server.payload.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class CurrentUserResponse {
    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> grantedAuthorities;
}
