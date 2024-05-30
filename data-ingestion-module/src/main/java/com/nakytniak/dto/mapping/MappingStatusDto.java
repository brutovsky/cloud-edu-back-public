package com.nakytniak.dto.mapping;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "MappingStatusDto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MappingStatusDto {
    private Integer mappingId;
    private MappingStatus status;
}
