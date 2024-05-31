package com.nakytniak.common.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JwtUtil {

    private JwtUtil() {

    }

    public static JwtUser createJwtUser(final Jwt jwt) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (jwt.hasClaim("userRoles")) {
            List<String> rolesClaim = jwt.getClaim("userRoles");
            List<Role> collect = rolesClaim
                    .stream()
                    .map(String::toUpperCase)
                    .map(Role::valueOf)
                    .toList();

            for (Role role : collect) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }
        }

        final Set<String> schools = new HashSet<>();
        if (jwt.hasClaim("app_metadata")) {
            final Map<String, Object> appMetadata = jwt.getClaim("app_metadata");
            final String school = (String) appMetadata.get("school");
            if (school != null) {
                schools.add(school);
            }
        }

        return new JwtUser(jwt, authorities, schools);
    }

    public static boolean hasAccessToSchool(final JwtUser jwtUser, final String schoolId) {
        return jwtUser != null && jwtUser.getSchools().contains(schoolId);
    }
}
