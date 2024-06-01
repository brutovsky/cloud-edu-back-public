/*
 * Copyright myayo.world, Inc 2024-Present. All Rights Reserved.
 * No unauthorized use of this software.
 */

package com.nakytniak.repository;

import com.nakytniak.model.SchoolRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRequestRepository extends JpaRepository<SchoolRequestEntity, Integer> {
}
