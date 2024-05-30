package com.nakytniak.backend.controller;

import com.nakytniak.backend.annotations.CustomApiOperation;
import com.nakytniak.backend.dto.StudentDto;
import com.nakytniak.backend.dto.StudentsResponseDto;
import com.nakytniak.backend.firestore.service.StudentService;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.common.security.JwtUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;


@Api(value = "StudentController")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/schools/{schoolId}/students")
public class StudentController {

    private final StudentService studentService;

    @CustomApiOperation(value = "createStudent", produces = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<CoreResponse<StudentDto>> createStudent(
            final @ApiParam(name = "schoolId", value = "ID of the school") @PathVariable String schoolId,
            final @RequestBody StudentDto studentDto, final JwtUser jwtUser)
            throws ExecutionException, InterruptedException {
        hasAccessToSchool(jwtUser, schoolId);
        return ResponseEntity.ok(CoreResponse.of(studentService.saveStudent(schoolId, studentDto)));
    }

    @CustomApiOperation(value = "getStudent", produces = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/{studentId}")
    public ResponseEntity<CoreResponse<StudentDto>> getStudent(
            final @ApiParam(name = "schoolId", value = "ID of the school") @PathVariable String schoolId,
            final @ApiParam(name = "studentId", value = "ID of the student") @PathVariable String studentId,
            final JwtUser jwtUser)
            throws ExecutionException, InterruptedException {
        hasAccessToSchool(jwtUser, schoolId);
        return ResponseEntity.ok(CoreResponse.of(studentService.getStudentById(schoolId, studentId)));
    }


    @CustomApiOperation(value = "getStudentsWithPagination", produces = APPLICATION_JSON,
            authorizations = @Authorization(value = "auth0_jwk",
                    scopes = @AuthorizationScope(description = "Admin scope", scope = "admin:admin")))
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping("/paginate")
    public ResponseEntity<CoreResponse<StudentsResponseDto>> getStudentsWithPagination(
            final @ApiParam(name = "schoolId", value = "ID of the school") @PathVariable String schoolId,
            final @ApiParam(name = "pageSize", value = "Page size") @RequestParam(defaultValue = "5") @Min(1) @Max(25) Integer pageSize,
            final @ApiParam(name = "cursorFirst", value = "Cursor first") @RequestParam(required = false) String cursorFirst,
            final @ApiParam(name = "cursorLast", value = "Cursor last") @RequestParam(required = false) String cursorLast,
            final JwtUser jwtUser) throws ExecutionException, InterruptedException {
        hasAccessToSchool(jwtUser, schoolId);
        return ResponseEntity.ok(
                CoreResponse.of(studentService.getStudentsWithPagination(schoolId, pageSize, cursorFirst, cursorLast))
        );
    }

    private void hasAccessToSchool(final JwtUser jwtUser, final String schoolId) {
        if (jwtUser == null || !jwtUser.getSchools().contains(schoolId)) {
            throw new AccessDeniedException(String.format("User does not have access to school [%s]", schoolId));
        }
    }

}