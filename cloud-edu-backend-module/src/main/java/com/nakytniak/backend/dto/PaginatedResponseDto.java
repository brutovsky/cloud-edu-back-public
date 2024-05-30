package com.nakytniak.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponseDto<T extends BaseDocumentDto> {
    private List<T> result;
    private int count;
    private int totalCount;
    private String firstCursor;
    private String lastCursor;
}
