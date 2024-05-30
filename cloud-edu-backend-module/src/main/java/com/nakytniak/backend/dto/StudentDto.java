package com.nakytniak.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto extends BaseDocumentDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String school;
    private Integer grade;
    private Boolean loggedIn;
    private String accountUsername;
}
