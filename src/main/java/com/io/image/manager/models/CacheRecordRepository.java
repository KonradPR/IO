package com.io.image.manager.models;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CacheRecordRepository extends CrudRepository<CacheRecord, Long> {
    Optional<CacheRecord> findByOriginAndNameHash(String origin, String nameHash);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CacheRecord cr set cr.hits = cr.hits + 1 where cr.origin = :origin and cr.nameHash = :nameHash")
    void incrementImageHit(@Param("origin") String origin, @Param("nameHash") String nameHash);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CacheRecord cr set cr.ttl = :ttl, cr.remoteFetch = current_timestamp where cr.origin = :origin and cr.nameHash = :nameHash")
    void updateTTL(@Param("origin") String origin, @Param("nameHash") String nameHash, @Param("ttl") Long ttl);
}
