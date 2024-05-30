package com.nakytniak.dto;

import com.nakytniak.dto.mapping.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngestionConfigDto {
    private Mapping mapping;
}
