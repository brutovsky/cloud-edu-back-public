package com.nakytniak.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class DataflowJobInfo {
    private String jobId;
    private String currentState;
    private LocalDateTime createDateTime;
    private LocalDateTime startDateTime;
    private Integer stagesCount;
    private String jobName;
    private String jobType;
}
