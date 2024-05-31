package com.nakytniak.dto;

import com.nakytniak.model.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadedFileDto {
    private Integer id;
    private String schoolId;
    private String filename;
    private String filepath;
    private String fullpath;
    private FileType type;
    private String creatorId;
    private String creatorName;
}
