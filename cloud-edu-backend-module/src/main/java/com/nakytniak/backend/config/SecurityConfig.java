package com.nakytniak.backend.config;

import com.nakytniak.common.security.CustomBearerTokenResolver;
import com.nakytniak.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.CrossOriginResourcePolicyHeaderWriter.CrossOriginResourcePolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configures our application with Spring Security to restrict access to our API endpoints.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        /*
        This is where we configure the security required for our endpoints and setup our app to serve as
        an OAuth2 Resource Server, using JWT validation.
        */
        return http
                .headers()
                .crossOriginResourcePolicy()
                .policy(CrossOriginResourcePolicy.SAME_ORIGIN)
                .and()
                .frameOptions().sameOrigin()
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .cors()
                .and()
                .csrf()
                .and()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/v1/**").authenticated()
                        .requestMatchers("/v1/health-cloud-edu-backend").permitAll()
                        .requestMatchers("/error").permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(JwtUtil::createJwtUser))
                        .bearerTokenResolver(customBearerTokenResolver())
                )
                .build();
    }

    @Bean
    public BearerTokenResolver customBearerTokenResolver() {
        return new CustomBearerTokenResolver();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${white.domain.list}") final List<String> whiteDomainList) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(whiteDomainList);
        configuration.setAllowedMethods(List.of("POST", "PUT", "PATCH", "DELETE", "GET", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
