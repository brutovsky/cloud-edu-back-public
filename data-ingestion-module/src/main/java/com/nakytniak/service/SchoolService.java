package com.nakytniak.service;

import com.nakytniak.dto.NewSchoolRegistrationInfo;
import com.nakytniak.dto.SchoolDto;
import com.nakytniak.dto.SchoolRegistrationInfo;
import com.nakytniak.exception.EntityNotFoundException;
import com.nakytniak.model.SchoolEntity;
import com.nakytniak.model.SchoolRequestEntity;
import com.nakytniak.model.SchoolRequestStatus;
import com.nakytniak.repository.SchoolRepository;
import com.nakytniak.repository.SchoolRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class SchoolService {

    private final SchoolRequestRepository schoolRequestRepository;
    private final SchoolRepository schoolRepository;

    public int createSchoolRequest(final NewSchoolRegistrationInfo newSchoolRegistrationInfo) {
        SchoolRequestEntity schoolRequestEntity = mapNewSchoolRegistrationInfo(newSchoolRegistrationInfo);
        schoolRequestEntity = schoolRequestRepository.save(schoolRequestEntity);
        return schoolRequestEntity.getId();
    }

    public void registerNewSchool(final SchoolRegistrationInfo schoolRegistrationInfo) {
        final SchoolRequestEntity schoolRequestEntity = schoolRequestRepository.findById(
                        schoolRegistrationInfo.getRequestId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Request with id [%s] does not exist", schoolRegistrationInfo.getRequestId())));
        switch (schoolRegistrationInfo.getSchoolRequestStatus()) {
            case APPROVED -> {
                final SchoolEntity schoolEntity = SchoolEntity.builder()
                        .schoolId(schoolRegistrationInfo.getSchoolId())
                        .name(schoolRequestEntity.getFullName())
                        .build();
                schoolRequestEntity.setStatus(SchoolRequestStatus.APPROVED);
                schoolRepository.save(schoolEntity);
                schoolRequestRepository.save(schoolRequestEntity);
            }
            case REJECTED -> {
                schoolRequestEntity.setStatus(SchoolRequestStatus.REJECTED);
                schoolRequestRepository.save(schoolRequestEntity);
            }
            default -> throw new IllegalArgumentException(
                    String.format("Wrong status: [%s]", schoolRegistrationInfo.getSchoolRequestStatus()));
        }
    }

    public List<NewSchoolRegistrationInfo> showAllRequests() {
        final List<SchoolRequestEntity> schoolRequestEntities = schoolRequestRepository.findAll();
        return schoolRequestEntities.stream().map(this::mapSchoolRequestEntity).toList();
    }

    public List<SchoolDto> getAllSchools() {
        final List<SchoolEntity> schoolEntities = schoolRepository.findAll();
        return schoolEntities.stream().map(this::mapSchool).toList();
    }

    private SchoolDto mapSchool(final SchoolEntity schoolEntity) {
        return SchoolDto.builder()
                .id(schoolEntity.getId())
                .schoolId(schoolEntity.getSchoolId())
                .name(schoolEntity.getName())
                .build();
    }

    private NewSchoolRegistrationInfo mapSchoolRequestEntity(final SchoolRequestEntity schoolRequestEntity) {
        return NewSchoolRegistrationInfo.builder()
                .id(schoolRequestEntity.getId())
                .fullName(schoolRequestEntity.getFullName())
                .phoneNumber(schoolRequestEntity.getPhoneNumber())
                .email(schoolRequestEntity.getEmail())
                .numberOfStudents(schoolRequestEntity.getNumberOfStudents())
                .region(schoolRequestEntity.getRegion())
                .address(schoolRequestEntity.getAddress())
                .typeOfEducation(schoolRequestEntity.getTypeOfEducation())
                .build();
    }

    private SchoolRequestEntity mapNewSchoolRegistrationInfo(
            final NewSchoolRegistrationInfo newSchoolRegistrationInfo) {
        return SchoolRequestEntity.builder()
                .fullName(newSchoolRegistrationInfo.getFullName())
                .phoneNumber(newSchoolRegistrationInfo.getPhoneNumber())
                .email(newSchoolRegistrationInfo.getEmail())
                .numberOfStudents(newSchoolRegistrationInfo.getNumberOfStudents())
                .region(newSchoolRegistrationInfo.getRegion())
                .address(newSchoolRegistrationInfo.getAddress())
                .typeOfEducation(newSchoolRegistrationInfo.getTypeOfEducation())
                .status(SchoolRequestStatus.REQUESTED)
                .build();
    }

}
