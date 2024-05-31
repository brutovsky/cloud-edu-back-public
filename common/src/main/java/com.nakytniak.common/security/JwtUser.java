package com.nakytniak.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JwtUser extends JwtAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 729674946898334604L;

    private final Set<String> schools;

    public JwtUser(final Jwt jwt, final Collection<? extends GrantedAuthority> authorities, final Set<String> school) {
        super(jwt, authorities);
        this.schools = school == null ? Collections.emptySet() : new HashSet<>(school);
    }

    public Set<String> getSchools() {
        return Collections.unmodifiableSet(schools);
    }
}
