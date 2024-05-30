package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.dto.DataSourceVendor;
import com.nakytniak.dto.mapping.Mapping;
import com.nakytniak.dto.mapping.MappingStatus;
import com.nakytniak.dto.mapping.MappingStatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;

@Api(tags = "AdminController", authorizations = @Authorization(value = "auth0_jwk"))
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/admin/config")
public class AdminController {

    @CustomApiOperation(value = "getMappingStatus", produces = APPLICATION_JSON)
    @GetMapping(path = "/mappings/{mappingId}")
    public ResponseEntity<CoreResponse<MappingStatusDto>> getMappingStatus(
            @ApiParam(name = "mappingId", value = "ID of the mapping to fetch")
            @Parameter(description = "Mapping id") @PathVariable final Integer mappingId) {
        return ResponseEntity.ok(CoreResponse.of(
                MappingStatusDto.builder().mappingId(mappingId).status(MappingStatus.WAITING_FOR_APPROVE).build()));
    }

    @CustomApiOperation(value = "getAllMappings", produces = APPLICATION_JSON)
    @GetMapping(path = "/mappings")
    public ResponseEntity<CoreResponse<List<Mapping>>> getAllMappings(
            @ApiParam(name = "school", value = "School name")
            @Parameter(description = "School name") @RequestParam(value = "school", required = false) final String school) {
        return ResponseEntity.ok(
                CoreResponse.of(
                        List.of(
                                Mapping.builder().sourceVendor(DataSourceVendor.MYSQL).sourceTable(school).build()
                        )
                )
        );
    }

    @CustomApiOperation(value = "updateMappingStatus", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    @PostMapping(path = "/mappings/{mappingId}")
    public ResponseEntity<CoreResponse<MappingStatusDto>> updateMappingStatus(
            @ApiParam(name = "mappingId", value = "ID of the mapping to fetch")
            @Parameter(description = "Mapping id") @PathVariable final Integer mappingId,
            final MappingStatusDto request) {
        return ResponseEntity.ok(CoreResponse.of(
                MappingStatusDto.builder().mappingId(mappingId).status(request.getStatus()).build()));
    }

}
