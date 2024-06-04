package com.me.autocomplete.repositories;

import com.me.autocomplete.models.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrieNodeRepository extends JpaRepository<Node, Integer> {
    @Query("SELECT n FROM Node n WHERE SUBSTRING(n.materializedPath, 2) = :prefix")
    Optional<Node> findByMaterializedPath(String prefix);
    Optional<Node> findByParentIsNull();
}
