package com.me.autocomplete.repositories;

import com.me.autocomplete.models.NgramTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NgramTableRepository extends JpaRepository<NgramTable, Long> {
    NgramTable findByKey(String key);
}
