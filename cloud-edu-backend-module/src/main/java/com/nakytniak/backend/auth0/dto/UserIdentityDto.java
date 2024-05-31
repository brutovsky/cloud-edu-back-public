package com.nakytniak.backend.auth0.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserIdentityDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String connection;

    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String provider;

    @JsonProperty("isSocial")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean isSocial;
}
