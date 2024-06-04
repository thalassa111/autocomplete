package com.me.autocomplete.repositories;

import com.me.autocomplete.models.Hash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTableRepository extends JpaRepository<Hash, Long> {
    Hash findByKey(String key);
}
