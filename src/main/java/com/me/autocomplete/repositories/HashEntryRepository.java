package com.me.autocomplete.repositories;

import com.me.autocomplete.models.HashEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HashEntryRepository extends JpaRepository<HashEntry, Long> {
    @Query("SELECT he FROM HashEntry he WHERE he.hash.key = :key")
    List<HashEntry> findByHashTableKey(String key);
}
