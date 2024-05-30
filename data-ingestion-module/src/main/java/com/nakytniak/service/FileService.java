package com.nakytniak.service;

import com.nakytniak.dto.UploadFileDto;
import com.nakytniak.dto.UploadedFileDto;
import com.nakytniak.exception.EntityNotFoundException;
import com.nakytniak.model.FileEntry;
import com.nakytniak.model.FileType;
import com.nakytniak.model.SchoolEntity;
import com.nakytniak.repository.FileEntryRepository;
import com.nakytniak.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static com.nakytniak.model.FileType.getType;

@Service
@RequiredArgsConstructor
public class FileService {

    private final CloudStorageService cloudStorageService;
    private final SchoolRepository schoolRepository;
    private final FileEntryRepository fileEntryRepository;
    @Value("${storage.bucket.name}")
    private String bucketName;
    public static final String FILE_PATH_TEMPLATE = "%s/%s/%s";

    public UploadedFileDto uploadFile(final UploadFileDto request, final String schoolId, final String creatorId)
            throws IOException {
        final SchoolEntity school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("School %s does not exists", schoolId)));
        final String filename = request.getFile().getOriginalFilename();
        assert filename != null;
        final FileType fileType = getType(filename);
        final String filepath = String.format(FILE_PATH_TEMPLATE, "files", schoolId, filename);
        final String uploadedFilename = cloudStorageService.uploadFile(request.getFile().getInputStream(), bucketName,
                filepath);
        FileEntry fileEntry = FileEntry.builder()
                .schoolId(school.getId())
                .creatorId(creatorId)
                .filename(filename)
                .filepath(filepath)
                .fullpath(uploadedFilename)
                .type(fileType)
                .build();
        fileEntry = fileEntryRepository.save(fileEntry);
        return UploadedFileDto.builder()
                .id(fileEntry.getId())
                .schoolId(schoolId)
                .creatorId(fileEntry.getCreatorId())
                .filename(filename)
                .filepath(filepath)
                .fullpath(fileEntry.getFullpath())
                .type(fileEntry.getType())
                .build();
    }

    public List<UploadedFileDto> getFiles(final String schoolId, final String creatorId) {
        final SchoolEntity school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("School %s does not exists", schoolId)));
        final List<FileEntry> files = fileEntryRepository.findAllBySchoolId(school.getId());
        return files.stream().map(fileEntry -> mapFileEntryToDto(fileEntry, schoolId)).toList();
    }

    public URL downloadFile(final String schoolId, final String creatorId, final String filename) {
        final SchoolEntity school = schoolRepository.findBySchoolId(schoolId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("School %s does not exists", schoolId)));
        fileEntryRepository.findBySchoolIdAndFilename(school.getId(), filename)
                .orElseThrow(() -> new EntityNotFoundException(String.format("File %s does not exists", filename)));
        final String filepath = String.format(FILE_PATH_TEMPLATE, "files", schoolId, filename);
        return cloudStorageService.generateSignedUrl(bucketName, filepath);
    }

    private UploadedFileDto mapFileEntryToDto(final FileEntry fileEntry, final String schoolId) {
        return UploadedFileDto.builder()
                .id(fileEntry.getId())
                .schoolId(schoolId)
                .creatorId(fileEntry.getCreatorId())
                .filename(fileEntry.getFilename())
                .filepath(fileEntry.getFilepath())
                .fullpath(fileEntry.getFullpath())
                .type(fileEntry.getType())
                .build();
    }

}
