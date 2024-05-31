package com.nakytniak.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class DataflowTaskDto {
    private Integer taskId;
    private TaskStatus status;
    private String schoolId;
    private String dataflowJobId;
    private String dataflowJobName;
    private DataflowJobInfo jobInfo;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
