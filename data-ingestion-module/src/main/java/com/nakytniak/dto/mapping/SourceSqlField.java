package com.nakytniak.dto.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SourceSqlField implements Serializable {

    @Serial
    private static final long serialVersionUID = -770668581793669254L;

    private String name;
    private MySqlDataType type;
}
