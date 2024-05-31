package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.security.JwtUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@Api(tags = "ApiController")
@RestController
@RequestMapping(value = "/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class APIController {

    @CustomApiOperation(value = "publicEndpoint", produces = APPLICATION_JSON)
    @GetMapping(value = "/public")
    public Message publicEndpoint() {
        return new Message("All good. You DO NOT need to be authenticated to call /api/public.");
    }

    @CustomApiOperation(value = "privateEndpoint", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping(value = "/private")
    public Message privateEndpoint(final JwtUser jwtUser) {
        return new Message(
                "All good. You can see this because you are Authenticated. Your username is " + jwtUser.getName());
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "privateEndpointAdmin", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @GetMapping(value = "/private/admin")
    public Message privateAdminEndpoint(final JwtUser jwtUser) {
        return new Message(String.format(
                "All good. You can see this because you are Authenticated. Your username is %s and your roles are [%s]",
                jwtUser.getName(), jwtUser.getAuthorities()));
    }

    @PreAuthorize(value = "hasAuthority('STUDENT')")
    @CustomApiOperation(value = "privateEndpointStudent", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Student scope", scope = "student:student")))
    @GetMapping(value = "/private/student")
    public Message privateStudentEndpoint(final JwtUser jwtUser) {
        return new Message(String.format(
                "All good. You can see this because you are Authenticated. Your username is %s and your roles are [%s]",
                jwtUser.getName(), jwtUser.getAuthorities()));
    }

    @CustomApiOperation(value = "privateScopedEndpoint", produces = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "read:messages scope", scope = "read:messages"))
    )
    @GetMapping(value = "/private-scoped")
    public Message privateScopedEndpoint() {
        return new Message(
                "You can see this because you are Authenticated with a Token granted the 'read:messages' scope");
    }

    public record Message(String message) {
    }
}
