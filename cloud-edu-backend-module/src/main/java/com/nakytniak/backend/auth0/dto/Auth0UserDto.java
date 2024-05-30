package com.nakytniak.backend.auth0.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Auth0UserDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    
    @JsonProperty("updated_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedAt;
    
    @JsonProperty("email_verified")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean emailVerified;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean blocked;
    
    @JsonProperty("created_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String createdAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UserIdentityDto> identities;
    
    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userId;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nickname;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String picture;
}
