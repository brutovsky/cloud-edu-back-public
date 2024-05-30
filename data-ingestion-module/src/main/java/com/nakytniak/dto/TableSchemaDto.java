package com.nakytniak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableSchemaDto {
    private String tableName;
    private List<FieldSchemaDto> fields;
}
