package com.nakytniak.backend.controller;

import com.nakytniak.backend.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.CHARSET;

@Api(tags = "HealthCloudEduBackendController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/health-cloud-edu-backend")
public class HealthController {

    @CustomApiOperation(value = "health-cloud-edu-backend", produces = CHARSET)
    @GetMapping()
    public ResponseEntity<CoreResponse<String>> health2() {
        return ResponseEntity.ok(CoreResponse.of("ok"));
    }

}
