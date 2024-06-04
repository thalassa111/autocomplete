package com.me.autocomplete.hashtable;

import com.me.autocomplete.models.Hash;
import com.me.autocomplete.models.HashEntry;
import com.me.autocomplete.repositories.HashEntryRepository;
import com.me.autocomplete.repositories.HashTableRepository;
import com.me.autocomplete.utilities.Constants;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class HashTable {
    private Map<String, List<String>> hashMap;
    private final HashTableRepository hashTableRepository;
    private final HashEntryRepository hashEntryRepository;

    public HashTable(HashTableRepository hashTableRepository, HashEntryRepository hashEntryRepository) {
        this.hashMap = new HashMap<>();
        this.hashTableRepository = hashTableRepository;
        this.hashEntryRepository = hashEntryRepository;
    }

    public List<String> searchHash(String prefix) {
        // use the provided key to search for matching HashEntry entities
        prefix = prefix.toLowerCase();
        System.out.println("input: " + prefix);
        List<HashEntry> hashEntries = hashEntryRepository.findByHashTableKey(prefix);
        if (!hashEntries.isEmpty()) {
            // extract values from the matching HashEntry entities
            List<String> values = new ArrayList<>();
            for (HashEntry hashEntry : hashEntries) {
                if (values.size() >= Constants.RESULT_SIZE) {
                    break; // exit the loop if the limit is reached
                }
                values.add(hashEntry.getValue());
            }
            return values; // return the list of values
        } else {
            System.out.println("No matching key found in HashEntry.");
            return new ArrayList<>(); // Return an empty list if key doesn't exist
        }
    }

    private void parseFile() {
        String fileName = "hash-words.txt";
        List<String> wordsList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                wordsList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a temporary map to store prefixes and associated words
        Map<String, List<String>> tempMap = new HashMap<>();

        // Split words into prefixes and store in the temporary map
        for (String word : wordsList) {
            for (int i = 0; i < word.length(); i++) {
                String prefix = word.substring(0, i + 1);
                tempMap.computeIfAbsent(prefix, k -> new ArrayList<>()).add(word);
            }
        }

        // Update the main hashMap with entries from the temporary map
        tempMap.forEach((key, value) -> {
            if (!hashMap.containsKey(key)) {
                hashMap.put(key, value);
            } else {
                hashMap.get(key).addAll(value);
            }
        });

        System.out.println("Hashtable wordsList size: " + wordsList.size());
        System.out.println("hashmap size: " + hashMap.size());
    }

    public void loadFromDB() {
        parseFile();
        populateDatabase();
    }

    @Async
    @Transactional
    public void populateDatabase() {
        List<Hash> hashBatch = new ArrayList<>();
        int batchCounter = 0; // Initialize the batch counter

        for (Map.Entry<String, List<String>> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            // Create Hash entity
            Hash hash = new Hash();
            hash.setKey(key);

            // Create and associate HashEntry entities
            for (String value : values) {
                HashEntry hashEntry = new HashEntry();
                hashEntry.setValue(value);
                // Associate HashEntry with Hash
                hash.addEntry(hashEntry);
            }

            // Add to batch
            hashBatch.add(hash);

            // If batch size is reached, save the batch
            if (hashBatch.size() >= Constants.BATCH_SIZE) {
                hashTableRepository.saveAll(hashBatch);
                hashBatch.clear(); // Clear the batch after saving
            }
        }

        // Save any remaining entities in the last batch
        if (!hashBatch.isEmpty()) {
            hashTableRepository.saveAll(hashBatch);
        }
    }

    @Transactional
    public void insertWord(String word) {
        Map<String, Set<String>> hashMap = new HashMap<>();

        // Populate the hashMap with key-value pairs from the word
        for (int i = 0; i < word.length(); i++) {
            String key = word.substring(0, i + 1);
            hashMap.computeIfAbsent(key, k -> new HashSet<>()).add(word);
        }

        List<Hash> hashBatch = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            Set<String> values = entry.getValue();

            Hash hash = hashTableRepository.findByKey(key);
            if (hash == null) {
                hash = new Hash();
                hash.setKey(key);
            }

            for (String value : values) {
                HashEntry hashEntry = new HashEntry();
                hashEntry.setValue(value);
                hash.addEntry(hashEntry);
            }

            hashBatch.add(hash);
            if (hashBatch.size() >= Constants.BATCH_SIZE_SMALL) {
                hashTableRepository.saveAll(hashBatch);
                hashBatch.clear();
            }
        }

        if (!hashBatch.isEmpty()) {
            hashTableRepository.saveAll(hashBatch);
        }
    }

    @Transactional
    public void deleteWord(String word) {
        for (int i = 0; i < word.length(); i++) {
            String key = word.substring(0, i + 1);
            Hash hash = hashTableRepository.findByKey(key);

            if (hash != null) {
                Set<HashEntry> entriesToRemove = new HashSet<>();

                for (HashEntry entry : hash.getEntries()) {
                    if (entry.getValue().equals(word)) {
                        entriesToRemove.add(entry);
                    }
                }

                for (HashEntry entry : entriesToRemove) {
                    hash.removeEntry(entry);
                }

                if (hash.getEntries().isEmpty()) {
                    hashTableRepository.delete(hash);
                } else {
                    hashTableRepository.save(hash);
                }
            }
        }
    }
}