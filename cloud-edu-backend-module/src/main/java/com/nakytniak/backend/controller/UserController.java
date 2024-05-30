package com.nakytniak.backend.controller;

import com.nakytniak.backend.annotations.CustomApiOperation;
import com.nakytniak.backend.auth0.Auth0ManagementService;
import com.nakytniak.backend.auth0.dto.Auth0UserDto;
import com.nakytniak.common.api.CoreResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.CHARSET;

@Api(tags = "UserController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final Auth0ManagementService auth0ManagementService;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "createStudentAccount", produces = CHARSET,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @PostMapping()
    public ResponseEntity<String> createStudentAccount() {
        final String result = auth0ManagementService.createUser();
        return ResponseEntity.ok(result);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getUsers", produces = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @GetMapping()
    public ResponseEntity<CoreResponse<List<Auth0UserDto>>> getUsers() {
        final List<Auth0UserDto> result = auth0ManagementService.getUsers();
        return ResponseEntity.ok(CoreResponse.of(result));
    }

}
