package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.common.security.JwtUser;
import com.nakytniak.controller.APIController.Message;
import com.nakytniak.dto.NewSchoolRegistrationInfo;
import com.nakytniak.dto.SchoolDto;
import com.nakytniak.dto.SchoolRegistrationInfo;
import com.nakytniak.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@Api(tags = "SchoolController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @CustomApiOperation(value = "registerNewSchool", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "superadmin:superadmin")))
    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registerNewSchool(final JwtUser jwtUser,
            final @RequestBody SchoolRegistrationInfo schoolRegistrationInfo) {
        schoolService.registerNewSchool(schoolRegistrationInfo);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @CustomApiOperation(value = "showAllRequests", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "superadmin:superadmin")))
    @GetMapping(value = "/requests")
    public ResponseEntity<CoreResponse<List<NewSchoolRegistrationInfo>>> showAllRequests(final JwtUser jwtUser) {
        return ResponseEntity.ok(CoreResponse.of(schoolService.showAllRequests()));
    }

    @PreAuthorize(value = "hasAuthority('SUPER_ADMIN')")
    @CustomApiOperation(value = "getAllSchools", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "superadmin:superadmin")))
    @GetMapping(value = "")
    public ResponseEntity<CoreResponse<List<SchoolDto>>> getAllSchools(final JwtUser jwtUser) {
        return ResponseEntity.ok(CoreResponse.of(schoolService.getAllSchools()));
    }

    @CustomApiOperation(value = "requestNewSchool", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/requests/new")
    public ResponseEntity<Message> requestNewSchool(
            final @RequestBody NewSchoolRegistrationInfo newSchoolRegistrationInfo) {
        final int requestNumber = schoolService.createSchoolRequest(newSchoolRegistrationInfo);
        return ResponseEntity.ok(new Message(String.format("Your request number id [%s]", requestNumber)));
    }

}
