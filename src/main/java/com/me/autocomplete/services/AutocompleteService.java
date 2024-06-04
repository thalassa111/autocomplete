package com.me.autocomplete.services;

import com.me.autocomplete.hashtable.HashTable;
import com.me.autocomplete.ngram.Ngram;
import com.me.autocomplete.trie.Trie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutocompleteService {

    private Trie trie;
    private HashTable hashTable;
    private Ngram ngram;

    @Autowired
    public AutocompleteService(Trie trie,
                               HashTable hashTable,
                               Ngram ngram){
        this.trie = trie;
        this.hashTable = hashTable;
        this.ngram = ngram;
    }

    //trie
    public List<String> searchTrie(String input){
        return trie.searchTrie(input);
    }

    public void LoadTrie(){
        trie.loadFromDB();
    }

    public void insertWordTrie(String word) {
        trie.insertWord(word);
    }

    public void deleteWordTrie(String word) {
        trie.deleteWord(word);
    }

    //hash-table
    public List<String> searchHash(String input) {
        return hashTable.searchHash(input);
    }

    public void LoadHash(){
        hashTable.loadFromDB();
    }

    public void insertWordHash(String word) {
        hashTable.insertWord(word);
    }

    public void deleteWordHash(String word) {
        hashTable.deleteWord(word);
    }

    //ngram
    public List<String> searchNgram(String input) {
       return ngram.searchNgram(input);
    }

    public void LoadNgram(){
        ngram.loadFromDB();
    }

    public void insertWordNgram(String word) {
        ngram.insertWord(word);
    }

    public void deleteWordNgram(String word) {
        ngram.deleteWord(word);
    }
}
