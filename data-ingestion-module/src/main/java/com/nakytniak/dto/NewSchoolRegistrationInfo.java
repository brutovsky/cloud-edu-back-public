package com.nakytniak.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewSchoolRegistrationInfo {
    private Integer id;
    @NotBlank
    @NotNull
    private String fullName;
    @NotBlank
    @NotNull
    private String phoneNumber;
    @NotBlank
    @NotNull
    private String email;
    @Min(1)
    @NotNull
    private Integer numberOfStudents;
    @NotBlank
    @NotNull
    private String region;
    @NotBlank
    @NotNull
    private String address;
    @NotBlank
    @NotNull
    private String typeOfEducation;
}
