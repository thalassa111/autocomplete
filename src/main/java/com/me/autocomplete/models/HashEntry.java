package com.me.autocomplete.models;

import jakarta.persistence.*;

@Entity
@Table(name = "hashentry", schema = "autocomplete")
public class HashEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    // This will be a foreign key referencing the Hash entity
    @ManyToOne
    @JoinColumn(name = "hash_id")
    private Hash hash;

    public HashEntry() {
    }

    public HashEntry(String value, Hash hash) {
        this.value = value;
        this.hash = hash;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Hash getHash() {
        return hash;
    }

    public void setHash(Hash hash) {
        this.hash = hash;
    }
}