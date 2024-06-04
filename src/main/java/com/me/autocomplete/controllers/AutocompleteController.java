package com.me.autocomplete.controllers;

import com.me.autocomplete.services.AutocompleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AutocompleteController {

    private AutocompleteService autocompleteService;
    @Autowired
    public AutocompleteController(AutocompleteService autocompleteService){
        this.autocompleteService = autocompleteService;
    }

    @GetMapping("/algoritm/trie")
    public ResponseEntity<List<String>> trie(@RequestParam("input") String input){
        long startTime = System.currentTimeMillis();
        List<String> suggestions = autocompleteService.searchTrie(input);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Trie -> Elapsed Time: " + elapsedTime + " milliseconds");
        return ResponseEntity.ok().body(suggestions);
    }

    @GetMapping("/algoritm/loadDBTrie")
    public void LoadTrie(){
        autocompleteService.LoadTrie();
    }

    @PostMapping("algoritm/insertWordTrie")
    public void insertWordTrie(@RequestParam("word") String word){
        autocompleteService.insertWordTrie(word);
    }

    @DeleteMapping("/algoritm/deleteWordTrie")
    public void deleteWordTrie(@RequestParam("word") String word) {
        autocompleteService.deleteWordTrie(word);
    }

    @GetMapping("/algoritm/hash")
    public ResponseEntity<List<String>> hash(@RequestParam("input") String input){
        long startTime = System.currentTimeMillis();
        List<String> suggestions = autocompleteService.searchHash(input);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Hash -> Elapsed Time: " + elapsedTime + " milliseconds");

        return ResponseEntity.ok().body(suggestions);
    }

    @GetMapping("/algoritm/loadDBHash")
    public void LoadHash(){
        autocompleteService.LoadHash();
    }

    @PostMapping("algoritm/insertWordHash")
    public void insertWordHash(@RequestParam("word") String word){
        autocompleteService.insertWordHash(word);
    }

    @DeleteMapping("/algoritm/deleteWordHash")
    public void deleteWordHash(@RequestParam("word") String word) {
        autocompleteService.deleteWordHash(word);
    }

    @GetMapping("/algoritm/ngram")
    public ResponseEntity<List<String>> ngram(@RequestParam("input") String input){
        long startTime = System.currentTimeMillis();
        List<String> suggestions = autocompleteService.searchNgram(input);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Ngram -> Elapsed Time: " + elapsedTime + " milliseconds");
        return ResponseEntity.ok().body(suggestions);
    }

    @GetMapping("/algoritm/loadDBNgram")
    public void LoadNgram(){
        System.out.println("loadNgram in controller");
        autocompleteService.LoadNgram();
        System.out.println("loading ngram done");
    }

    @PostMapping("algoritm/insertWordNgram")
    public void insertWordNgram(@RequestParam("word") String word){
        System.out.println("insertWordNgram in controller, word: " + word);
        autocompleteService.insertWordNgram(word);
    }

    @DeleteMapping("/algoritm/deleteWordNgram")
    public void deleteWordNgram(@RequestParam("word") String word) {
        System.out.println("deleteWordNgram in controller");
        autocompleteService.deleteWordNgram(word);
    }

}
