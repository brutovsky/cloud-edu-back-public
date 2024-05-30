package com.nakytniak.dto.mapping;

import com.nakytniak.dto.DataSourceVendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mapping implements Serializable {

    @Serial
    private static final long serialVersionUID = -3799370827345097138L;

    private DataSourceVendor sourceVendor;
    private String query;
    private String sourceTable;
    private Map<String, SourceSqlField> tableMappings;
}
