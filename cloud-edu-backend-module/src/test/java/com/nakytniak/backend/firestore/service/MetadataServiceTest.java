package com.nakytniak.backend.firestore.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.nakytniak.backend.model.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class MetadataServiceTest {

    @Mock
    private Firestore firestore;

    @Mock
    private CollectionReference collectionReference;

    @Mock
    private DocumentReference documentReference1;
    @Mock
    private DocumentReference documentReference2;

    @Mock
    private WriteBatch writeBatch;

    @Mock
    private ApiFuture<DocumentSnapshot> apiFuture;

    @Mock
    private ApiFuture<List<WriteResult>> apiFutureWriteResult;

    @Mock
    private List<WriteResult> writeResult;

    @Mock
    private DocumentSnapshot writeResults;

    @InjectMocks
    private MetadataService metadataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDocumentAndIncrementCounter() throws ExecutionException, InterruptedException {
        final String collectionName = "testCollection";
        final String documentId = "123";
        final Object testObject = new Object();

        when(firestore.collection(Metadata.COLLECTION_NAME)).thenReturn(collectionReference);
        when(collectionReference.document(collectionName)).thenReturn(documentReference1);

        when(firestore.collection(collectionName)).thenReturn(collectionReference);
        when(collectionReference.document()).thenReturn(documentReference2);

        when(firestore.batch()).thenReturn(writeBatch);
        when(writeBatch.update(any(DocumentReference.class), any(String.class), any(FieldValue.class)))
                .thenReturn(writeBatch);
        when(writeBatch.set(any(DocumentReference.class), any(Object.class))).thenReturn(writeBatch);
        when(writeBatch.commit()).thenReturn(apiFutureWriteResult);
        when(apiFutureWriteResult.get()).thenReturn(writeResult);
        when(documentReference2.getId()).thenReturn(documentId);

        final Pair<String, List<WriteResult>> result = metadataService.addDocumentAndIncrementCounter(firestore,
                collectionName, testObject);

        assertNotNull(result);
        assertEquals(documentId, result.getFirst());
        assertEquals(writeResult, result.getSecond());
    }

    @Test
    void testGetMetadata() throws ExecutionException, InterruptedException {
        final String collectionName = "testCollection";
        final Metadata expectedMetadata = new Metadata();

        when(firestore.collection(Metadata.COLLECTION_NAME)).thenReturn(collectionReference);
        when(collectionReference.document(collectionName)).thenReturn(documentReference1);
        when(documentReference1.get()).thenReturn(apiFuture);
        when(apiFuture.get()).thenReturn(mock(DocumentSnapshot.class));
        when(apiFuture.get()).thenReturn(writeResults);
        when(writeResults.toObject(Metadata.class)).thenReturn(expectedMetadata);

        final Metadata result = metadataService.getMetadata(firestore, collectionName);

        assertNotNull(result);
        assertEquals(expectedMetadata, result);
    }
}

