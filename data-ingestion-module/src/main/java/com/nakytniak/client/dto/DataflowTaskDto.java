package com.nakytniak.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class DataflowTaskDto {
    private Integer id;
    private TaskStatus status;
    private String dataflowJobId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}