package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.common.security.JwtUser;
import com.nakytniak.common.security.JwtUtil;
import com.nakytniak.dto.UploadFileDto;
import com.nakytniak.dto.UploadedFileDto;
import com.nakytniak.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.MULTIPART_FORM_DATA;

@Api(tags = "FileIngestionController", authorizations = @Authorization(value = "auth0_jwk"))
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/files")
public class FileIngestionController {

    private final FileService fileService;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "uploadFile", produces = APPLICATION_JSON, consumes = MULTIPART_FORM_DATA,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CoreResponse<UploadedFileDto>> uploadFile(final UploadFileDto request,
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final JwtUser jwtUser) throws IOException {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(CoreResponse.of(fileService.uploadFile(request, schoolId, jwtUser.getName())));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getFileEntries", produces = APPLICATION_JSON, consumes = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @GetMapping(produces = APPLICATION_JSON)
    public ResponseEntity<CoreResponse<List<UploadedFileDto>>> getFileEntries(
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final JwtUser jwtUser) {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(CoreResponse.of(fileService.getFiles(schoolId, jwtUser.getName())));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getFileDownloadUrl", produces = APPLICATION_JSON, consumes = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @GetMapping(value = "/{filename}", produces = APPLICATION_JSON)
    public ResponseEntity<CoreResponse<URL>> getFileDownloadUrl(
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final @ApiParam(name = "filename", value = "Name of file") @PathVariable String filename,
            final JwtUser jwtUser) {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(CoreResponse.of(fileService.downloadFile(schoolId, jwtUser.getName(), filename)));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getFileDownloadUrl", produces = APPLICATION_JSON, consumes = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @DeleteMapping(value = "/{filename}", produces = APPLICATION_JSON)
    public ResponseEntity<Void> deleteFile(
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final @ApiParam(name = "filename", value = "Name of file") @PathVariable String filename,
            final JwtUser jwtUser) {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        fileService.deleteFile(schoolId, jwtUser.getName(), filename);
        return ResponseEntity.ok().build();
    }
}
