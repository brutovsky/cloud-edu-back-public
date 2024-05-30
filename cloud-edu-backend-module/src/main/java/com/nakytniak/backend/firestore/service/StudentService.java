package com.nakytniak.backend.firestore.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.nakytniak.backend.dto.BaseDocumentDto;
import com.nakytniak.backend.dto.PaginatedResponseDto;
import com.nakytniak.backend.dto.StudentDto;
import com.nakytniak.backend.dto.StudentsResponseDto;
import com.nakytniak.backend.exception.EntityNotFoundException;
import com.nakytniak.backend.firestore.FirestoreClient;
import com.nakytniak.backend.model.Metadata;
import com.nakytniak.backend.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentService {

    private final FirestoreClient firestoreClient;
    private final MetadataService metadataService;

    public StudentDto saveStudent(final String schoolId, final StudentDto studentDto)
            throws ExecutionException, InterruptedException {
        final Student student = mapStudentDto(studentDto);

        final Firestore firestore = firestoreClient.getClient(schoolId);
        final Pair<String, List<WriteResult>> writeResultPair = metadataService
                .addDocumentAndIncrementCounter(firestore, Student.COLLECTION_NAME, student);

        return getStudent(firestore, writeResultPair.getFirst());
    }

    public StudentDto getStudentById(final String schoolId, final String studentId)
            throws ExecutionException, InterruptedException {
        final Firestore firestore = firestoreClient.getClient(schoolId);
        return getStudent(firestore, studentId);
    }

    public StudentsResponseDto getStudentsWithPagination(final String schoolId, final int pageSize,
            final String cursorFirst, final String cursorLast)
            throws ExecutionException, InterruptedException {
        final Firestore firestore = firestoreClient.getClient(schoolId);
        Query query = firestore.collection(Student.COLLECTION_NAME)
                .orderBy("__name__")
                .limit(pageSize);

        if (cursorFirst != null) {
            DocumentSnapshot firstDocument = firestore.collection(Student.COLLECTION_NAME).document(cursorFirst).get()
                    .get();
            query = query.endBefore(firstDocument).limitToLast(pageSize);
        } else if (cursorLast != null) {
            DocumentSnapshot lastDocument = firestore.collection(Student.COLLECTION_NAME).document(cursorLast).get()
                    .get();
            query = query.startAfter(lastDocument).limit(pageSize);
        }

        QuerySnapshot querySnapshot = query.get().get();
        final List<StudentDto> students = querySnapshot.getDocuments().stream()
                .map(document -> document.toObject(Student.class))
                .map(this::mapStudent)
                .collect(Collectors.toList());

        final Metadata metadata = metadataService.getMetadata(firestore, Student.COLLECTION_NAME);

        final PaginatedResponseDto<StudentDto> paginatedResponseDto = getPaginatedResult(students, metadata);

        return StudentsResponseDto.builder().students(paginatedResponseDto).build();
    }

    private <T extends BaseDocumentDto> PaginatedResponseDto<T> getPaginatedResult(final List<T> paginatedList,
            final Metadata metadata) {
        String firstCursor = null;
        String lastCursor = null;
        if (!paginatedList.isEmpty()) {
            firstCursor = paginatedList.get(0).getId();
            lastCursor = paginatedList.get(paginatedList.size() - 1).getId();
        }
        return PaginatedResponseDto.<T>builder()
                .result(paginatedList)
                .count(paginatedList.size())
                .totalCount(metadata.getCount())
                .firstCursor(firstCursor)
                .lastCursor(lastCursor)
                .build();
    }

    private StudentDto getStudent(final Firestore firestore, final String studentId)
            throws ExecutionException, InterruptedException {
        return Optional.ofNullable(
                firestore.collection(Student.COLLECTION_NAME).document(studentId).get().get().toObject(Student.class)
        ).map(this::mapStudent).orElseThrow(() -> new EntityNotFoundException(Student.class, studentId));
    }

    private Student mapStudentDto(final StudentDto studentDto) {
        return Student.builder()
                .id(studentDto.getId())
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .phoneNumber(studentDto.getPhoneNumber())
                .school(studentDto.getSchool())
                .grade(studentDto.getGrade())
                .loggedIn(studentDto.getLoggedIn())
                .accountUsername(studentDto.getAccountUsername())
                .build();
    }

    private StudentDto mapStudent(final Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .school(student.getSchool())
                .grade(student.getGrade())
                .loggedIn(student.getLoggedIn())
                .accountUsername(student.getAccountUsername())
                .build();
    }

}
