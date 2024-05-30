package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.security.JwtUser;
import com.nakytniak.controller.APIController.Message;
import com.nakytniak.dto.SchoolRegistrationInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "SchoolController", authorizations = @Authorization(value = "auth0_jwk"))
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/schools")
public class SchoolController {

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @CustomApiOperation(value = "registerNewSchool", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "superadmin:superadmin")))
    @GetMapping(value = "/registration")
    public Message registerNewSchool(final JwtUser jwtUser, final SchoolRegistrationInfo schoolRegistrationInfo) {
        return new Message(String.format(
                "All good. School is registered %s. Your username is %s and your roles are [%s]",
                schoolRegistrationInfo.getSchoolId(), jwtUser.getName(), jwtUser.getAuthorities()));
    }

}
