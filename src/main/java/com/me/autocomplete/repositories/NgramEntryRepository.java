package com.me.autocomplete.repositories;

import com.me.autocomplete.models.NgramEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NgramEntryRepository extends JpaRepository<NgramEntry, Long> {
    @Query("SELECT ne FROM NgramEntry ne WHERE ne.ngram.key = :key")
    List<NgramEntry> findByNgramTableKey(String key);
}
