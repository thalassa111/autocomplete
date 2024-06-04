package com.me.autocomplete.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ngramtable", schema = "autocomplete")
public class NgramTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key")
    private String key;

    @OneToMany(mappedBy = "ngram", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<NgramEntry> entries = new HashSet<>();

    public NgramTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNgramKey() {
        return key;
    }

    public void setNgramKey(String key) {
        this.key = key;
    }

    public Set<NgramEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<NgramEntry> entries) {
        this.entries = entries;
    }

    public void setKey(String ngramKey) {
        this.key = ngramKey;
    }

    public void addEntry(NgramEntry entry) {
        entries.add(entry);
        entry.setNgram(this);
    }

    public void removeEntry(NgramEntry entry){
        entries.remove(entry);
        entry.setNgram(null);
    }
}
