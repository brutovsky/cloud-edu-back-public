package com.nakytniak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewSchoolRegistrationInfo {
    private Integer id;
    private String fullName;
    private String phoneNumber;
    private String email;
    private Integer numberOfStudents;
    private String region;
    private String address;
    private String typeOfEducation;
}
