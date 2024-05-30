package com.nakytniak.backend.auth0.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateAuth0User {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonProperty(value = "phone_number")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;

    @JsonProperty(value = "user_metadata")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object userMetadata;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean blocked;

    @JsonProperty(value = "email_verified")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean emailVerified;

//    @JsonProperty(value = "phone_verified")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private boolean phoneVerified;

    @JsonProperty(value = "app_metadata")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> appMetadata;

    @JsonProperty(value = "given_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String givenName;

    @JsonProperty(value = "family_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String familyName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String picture;

    @JsonProperty(value = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String connection;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    @JsonProperty(value = "verify_email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean verifyEmail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;
}
