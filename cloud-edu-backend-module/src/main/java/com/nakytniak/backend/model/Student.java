package com.nakytniak.backend.model;

import com.google.cloud.spring.data.firestore.Document;
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
@Document(collectionName = Student.COLLECTION_NAME)
public class Student extends BaseDocument {
    public static final String COLLECTION_NAME = "students_info";
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String school;
    private Integer grade;
    private Boolean loggedIn;
    private String accountUsername;
}
