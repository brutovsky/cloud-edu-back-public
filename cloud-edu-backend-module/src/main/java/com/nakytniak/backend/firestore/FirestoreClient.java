package com.nakytniak.backend.firestore;

import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirestoreClient {

    private final Map<String, Firestore> firestoreMap;

    public FirestoreClient(@Qualifier("firestoreMap") final Map<String, Firestore> firestoreMap) {
        this.firestoreMap = firestoreMap;
    }

    public Firestore getClient(final String schoolId) {
        return firestoreMap.get(schoolId);
    }

}
