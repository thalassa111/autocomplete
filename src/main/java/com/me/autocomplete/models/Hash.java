package com.me.autocomplete.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hashtable", schema = "autocomplete")
public class Hash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key")
    private String key;

    @OneToMany(mappedBy = "hash", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HashEntry> entries = new HashSet<>();

    public Hash() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Set<HashEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<HashEntry> entries) {
        this.entries = entries;
    }

    // Convenience methods
    public void addEntry(HashEntry entry) {
        entries.add(entry);
        entry.setHash(this);
    }

    public void removeEntry(HashEntry entry) {
        entries.remove(entry);
        entry.setHash(null);
    }
}
