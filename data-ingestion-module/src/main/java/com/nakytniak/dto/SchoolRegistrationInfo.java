package com.nakytniak.dto;

import com.nakytniak.model.SchoolRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolRegistrationInfo {
    private Integer requestId;
    private String schoolId;
    private SchoolRequestStatus schoolRequestStatus;
}
