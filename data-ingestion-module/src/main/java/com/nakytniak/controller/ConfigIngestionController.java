package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import com.nakytniak.dto.DataSourceVendor;
import com.nakytniak.dto.IngestionConfigDto;
import com.nakytniak.dto.SchemaListingDto;
import com.nakytniak.dto.TableSchemaDto;
import com.nakytniak.dto.mapping.Mapping;
import com.nakytniak.service.BigQueryMetadataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.CHARSET;
import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.MULTIPART_FORM_DATA;

@Api(tags = "ConfigIngestionController", authorizations = @Authorization(value = "auth0_jwk"))
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/config")
public class ConfigIngestionController {

    private final BigQueryMetadataService bigQueryMetadataService;


    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getMapping", produces = MULTIPART_FORM_DATA, consumes = APPLICATION_JSON)
    @GetMapping(path = "/mappings/{mappingId}")
    public ResponseEntity<CoreResponse<Mapping>> getMapping(
            @ApiParam(name = "mappingId", value = "ID of the mapping to fetch")
            @Parameter(description = "Mapping id") @PathVariable final Integer mappingId) {
        return ResponseEntity.ok(CoreResponse.of(Mapping.builder().sourceVendor(DataSourceVendor.MYSQL).build()));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getMapping", produces = MULTIPART_FORM_DATA, consumes = APPLICATION_JSON)
    @GetMapping(path = "/mappings")
    public ResponseEntity<CoreResponse<List<Mapping>>> getMappings(
            @ApiParam(name = "school", value = "School name")
            @Parameter(description = "School name") @RequestParam(value = "school", required = false)
            final String school) {
        return ResponseEntity.ok(
                CoreResponse.of(
                        List.of(
                                Mapping.builder().sourceVendor(DataSourceVendor.MYSQL).sourceTable(school).build()
                        )
                )
        );
    }

//    Saves mapping to the unapproved folder
//    + add to sql table with status and location of the actual mapping
//    After admin approve move to main folder
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "submitMapping", produces = MULTIPART_FORM_DATA, consumes = MULTIPART_FORM_DATA)
    @PostMapping(path = "/mappings")
    public ResponseEntity<CoreResponse<Mapping>> submitMapping(final IngestionConfigDto request) {
        return ResponseEntity.ok(CoreResponse.of(request.getMapping()));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "editMapping", produces = MULTIPART_FORM_DATA, consumes = MULTIPART_FORM_DATA)
    @PutMapping(path = "/mappings/{mappingId}")
    public ResponseEntity<CoreResponse<Mapping>> editMapping(final IngestionConfigDto request,
            @ApiParam(name = "mappingId", value = "ID of the mapping to fetch")
            @Parameter(description = "Mapping id") @PathVariable final Integer mappingId) {
        return ResponseEntity.ok(CoreResponse.of(request.getMapping()));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "deleteMapping")
    @DeleteMapping(path = "/mappings/{mappingId}")
    public ResponseEntity deleteMapping(
            @ApiParam(name = "mappingId", value = "ID of the mapping to fetch")
            @Parameter(description = "Mapping id") @PathVariable final Integer mappingId) {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getSchemaListing", produces = CHARSET)
    @GetMapping()
    public ResponseEntity<CoreResponse<SchemaListingDto>> getSchemaListing() {
        return ResponseEntity.ok(CoreResponse.of(bigQueryMetadataService.getSchemaListing()));
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @CustomApiOperation(value = "getSchema", produces = CHARSET)
    @GetMapping("/schema/{tableName}")
    public ResponseEntity<CoreResponse<TableSchemaDto>> getSchema(
            @ApiParam(name = "tableName", value = "Table name") @PathVariable final String tableName) {
        return ResponseEntity.ok(CoreResponse.of(bigQueryMetadataService.getSchema(tableName)));
    }

}
