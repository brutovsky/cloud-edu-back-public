/*
 * Copyright myayo.world, Inc 2024-Present. All Rights Reserved.
 * No unauthorized use of this software.
 */

package com.nakytniak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolDto {
    private Integer id;
    private String schoolId;
    private String name;
}
