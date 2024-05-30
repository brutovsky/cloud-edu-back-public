package com.nakytniak.dto;

import com.google.cloud.bigquery.Field.Mode;
import com.google.cloud.bigquery.LegacySQLTypeName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldSchemaDto {
    private String name;
    private LegacySQLTypeName type;
    private Mode mode;
    private String description;
    private Long maxLength;
}
