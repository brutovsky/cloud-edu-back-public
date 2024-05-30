package com.nakytniak.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Set;

public class JwtUser extends JwtAuthenticationToken {

    private final Set<String> schools;

    public JwtUser(final Jwt jwt, Collection<? extends GrantedAuthority> authorities, final Set<String> school) {
        super(jwt, authorities);
        this.schools = school;
    }

    public Set<String> getSchools() {
        return schools;
    }
}