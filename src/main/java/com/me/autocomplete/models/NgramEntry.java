package com.me.autocomplete.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ngramentry", schema = "autocomplete")
public class NgramEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    // This will be a foreign key referencing the NgramTable entity
    @ManyToOne
    @JoinColumn(name = "ngram_id")
    private NgramTable ngram;

    public NgramEntry() {
    }

    public NgramEntry(String wordValue, NgramTable ngram) {
        this.value = wordValue;
        this.ngram = ngram;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWordValue() {
        return value;
    }

    public void setWordValue(String wordValue) {
        this.value = wordValue;
    }

    public NgramTable getNgram() {
        return ngram;
    }

    public void setNgram(NgramTable ngram) {
        this.ngram = ngram;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

