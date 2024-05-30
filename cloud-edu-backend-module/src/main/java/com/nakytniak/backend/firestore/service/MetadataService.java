package com.nakytniak.backend.firestore.service;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.nakytniak.backend.model.Metadata;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Service
public class MetadataService {

    public <T> Pair<String, List<WriteResult>> addDocumentAndIncrementCounter(final Firestore firestore,
            final String collectionName, final T object) throws ExecutionException, InterruptedException {
        final DocumentReference metadataRef = firestore.collection(Metadata.COLLECTION_NAME).document(collectionName);
        final DocumentReference newDocRef = firestore.collection(collectionName).document();
        final WriteBatch batch = firestore.batch();
        batch.update(metadataRef, "count", FieldValue.increment(1));
        batch.set(newDocRef, object);
        final List<WriteResult> commitResult = batch.commit().get();
        return Pair.of(newDocRef.getId(), commitResult);
    }

    public Metadata getMetadata(final Firestore firestore, final String collectionName)
            throws ExecutionException, InterruptedException {
        final DocumentReference metadataRef = firestore.collection(Metadata.COLLECTION_NAME).document(collectionName);
        return metadataRef.get().get().toObject(Metadata.class);
    }

}
