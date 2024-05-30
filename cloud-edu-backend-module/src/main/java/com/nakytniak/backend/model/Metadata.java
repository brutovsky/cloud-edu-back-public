package com.nakytniak.backend.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collectionName = Student.COLLECTION_NAME)
public class Metadata {
    public static final String COLLECTION_NAME = "metadata";
    @DocumentId
    private String collectionName;
    private int count;
}
