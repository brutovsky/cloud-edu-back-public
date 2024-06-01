/*
 * Copyright myayo.world, Inc 2024-Present. All Rights Reserved.
 * No unauthorized use of this software.
 */

package com.nakytniak.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "school_request")
public class SchoolRequestEntity extends CommonEntity {
    private String fullName;
    private String phoneNumber;
    private String email;
    private Integer numberOfStudents;
    private String region;
    private String address;
    private String typeOfEducation;
    private SchoolRequestStatus status;
}
