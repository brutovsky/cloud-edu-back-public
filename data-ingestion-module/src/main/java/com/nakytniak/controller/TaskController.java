package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.client.TaskManagerClient;
import com.nakytniak.client.dto.DataflowTaskDto;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.common.security.JwtUser;
import com.nakytniak.common.security.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Api(tags = "TaskController", authorizations = @Authorization(value = "auth0_jwk"))
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/tasks")
public class TaskController {

    private final TaskManagerClient taskManagerClient;

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getTasks", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "admin:admin")))
    @GetMapping
    public ResponseEntity<CoreResponse<List<DataflowTaskDto>>> getTasks(final JwtUser jwtUser,
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId
    ) throws IOException {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(taskManagerClient.getTasks(schoolId));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getTask", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "admin:admin")))
    @GetMapping("/{taskId}")
    public ResponseEntity<CoreResponse<DataflowTaskDto>> getTask(final JwtUser jwtUser,
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final @ApiParam(name = "taskId", value = "ID of the task") @PathVariable Integer taskId)
            throws IOException {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(taskManagerClient.getTask(taskId, schoolId));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "createTask", produces = MediaType.APPLICATION_JSON_VALUE,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "SuperAdmin scope", scope = "admin:admin")))
    @PostMapping
    public ResponseEntity<CoreResponse<DataflowTaskDto>> createTask(final JwtUser jwtUser,
            final @ApiParam(name = "schoolId", value = "ID of the school") @RequestParam String schoolId,
            final @ApiParam(name = "type", value = "Type of task") @RequestParam String type,
            final @ApiParam(name = "entityType", value = "Type of entity") @RequestParam String entityType,
            final @ApiParam(name = "filename", value = "filename") @RequestParam String filename)
            throws IOException {
        if (!JwtUtil.hasAccessToSchool(jwtUser, schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
        return ResponseEntity.ok(taskManagerClient.createTask(type, entityType, schoolId, filename));
    }

}
