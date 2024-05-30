package com.nakytniak.controller;

import com.nakytniak.annotations.CustomApiOperation;
import com.nakytniak.common.api.CoreResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.grpc.netty.shaded.io.netty.handler.codec.http.HttpHeaders.Values.CHARSET;

@Api(tags = "HealthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/health")
public class HealthController {

    @CustomApiOperation(value = "health", produces = CHARSET)
    @GetMapping()
    public ResponseEntity<CoreResponse<String>> health() {
        return ResponseEntity.ok(CoreResponse.of("ok"));
    }

}
