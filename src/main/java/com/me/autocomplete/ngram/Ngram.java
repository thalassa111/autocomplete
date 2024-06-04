package com.me.autocomplete.ngram;

import com.me.autocomplete.models.NgramEntry;
import com.me.autocomplete.models.NgramTable;
import com.me.autocomplete.repositories.NgramEntryRepository;
import com.me.autocomplete.repositories.NgramTableRepository;
import com.me.autocomplete.utilities.Constants;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Component
public class Ngram {
    private Map<String, List<String>> ngram;
    private final NgramTableRepository ngramTableRepository;
    private final NgramEntryRepository ngramEntryRepository;

    public Ngram(NgramTableRepository ngramTableRepository, NgramEntryRepository ngramEntryRepository) {
        this.ngram = new HashMap<>();
        this.ngramTableRepository = ngramTableRepository;
        this.ngramEntryRepository = ngramEntryRepository;
    }

    // Method to generate n-grams from unigrams up to the given length from a given word
    public List<String> generateNgrams(String word, int maxLength) {
        List<String> ngrams = new ArrayList<>();
        for (int n = 1; n <= maxLength && n <= word.length(); n++) {
            for (int i = 0; i <= word.length() - n; i++) {
                String ngram = word.substring(i, i + n);
                ngrams.add(ngram);
            }
        }
        return ngrams;
    }

    public List<String> searchNgram(String prefix) {
        // use the provided key to search for matching NgramEntry entities
        prefix = prefix.toLowerCase();
        System.out.println("input: " + prefix);
        List<NgramEntry> ngramEntriesEntries = ngramEntryRepository.findByNgramTableKey(prefix);
        if (!ngramEntriesEntries.isEmpty()) {
            // extract values from the matching NgramEntry entities
            List<String> values = new ArrayList<>();
            for (NgramEntry ngramEntry : ngramEntriesEntries) {
                if (values.size() >= Constants.RESULT_SIZE) {
                    break; // Exit the loop if the limit is reached
                }
                values.add(ngramEntry.getValue());
            }
            return values; // return the list of values
        } else {
            System.out.println("No matching key found in NgramEntry.");
            return new ArrayList<>(); // Return an empty list if key doesn't exist
        }
    }

    private void parseFile() {
        String fileName = "ngram-words.txt";
        List<String> wordsList = new ArrayList<>();

        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                wordsList.add(line);
                addWord(line); // Add the entire word to the ngram hashmap
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ngram wordsList size: " + wordsList.size());
        System.out.println("ngram hashmap size: " + ngram.size());
    }

    // Method to add a word and its n-grams to the ngram map
    public void addWord(String word) {
        List<String> ngrams = generateNgrams(word, word.length()); // Generate n-grams up to the entire word length
        for (String ngramFor : ngrams) {
            if (!ngram.containsKey(ngramFor)) { // Check if the ngram exists as a key in the hashmap
                ngram.put(ngramFor, new ArrayList<>()); // If not, create a new entry in the hashmap
            }
            ngram.get(ngramFor).add(word); // Add the word to the list associated with the ngram
        }
    }

    public void loadFromDB( ) {
        parseFile();
        populateDatabase();
    }

    @Async
    @Transactional
    public void populateDatabase( ) {
        List<NgramTable> ngramBatch = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : ngram.entrySet()) {
            String ngramKey = entry.getKey();
            List<String> words = entry.getValue();

            // Create Ngram entity
            NgramTable ngramTable = new NgramTable();
            ngramTable.setKey(ngramKey);

            // Create and associate Word entities
            for (String word : words) {
                NgramEntry ngramEntry = new NgramEntry();
                ngramEntry.setValue(word);
                // Associate Word with Ngram
                ngramTable.addEntry(ngramEntry);
            }

            // Add to batch
            ngramBatch.add(ngramTable);

            // If batch size is reached, save the batch
            if (ngramBatch.size() >= Constants.BATCH_SIZE) {
                ngramTableRepository.saveAll(ngramBatch);
                ngramBatch.clear(); // Clear the batch after saving
            }
        }

        // Save any remaining entities in the last batch
        if (!ngramBatch.isEmpty()) {
            ngramTableRepository.saveAll(ngramBatch);
        }
    }

    @Transactional
    public void insertWord(String word) {
        List<NgramTable> ngramBatch = new ArrayList<>();

        // Generate n-grams of the entire word length
        for (int n = 1; n <= word.length(); n++) {
            for (int i = 0; i <= word.length() - n; i++) {
                String ngramKey = word.substring(i, i + n);
                NgramTable ngramTable = ngramTableRepository.findByKey(ngramKey);
                if (ngramTable == null) {
                    ngramTable = new NgramTable();
                    ngramTable.setKey(ngramKey);
                }

                NgramEntry ngramEntry = new NgramEntry();
                ngramEntry.setValue(word);
                ngramTable.addEntry(ngramEntry);

                ngramBatch.add(ngramTable);

                if (ngramBatch.size() >= Constants.BATCH_SIZE_SMALL) {
                    ngramTableRepository.saveAll(ngramBatch);
                    ngramBatch.clear();
                }
            }
        }

        if (!ngramBatch.isEmpty()) {
            ngramTableRepository.saveAll(ngramBatch);
        }
    }

    @Transactional
    public void deleteWord(String word) {
        // Generate n-grams of the entire word length
        for (int n = 1; n <= word.length(); n++) {
            for (int i = 0; i <= word.length() - n; i++) {
                String ngramKey = word.substring(i, i + n);
                NgramTable ngramTable = ngramTableRepository.findByKey(ngramKey);

                if (ngramTable != null) {
                    Set<NgramEntry> entriesToRemove = new HashSet<>();
                    for (NgramEntry entry : ngramTable.getEntries()) {
                        if (entry.getValue().equals(word)) {
                            entriesToRemove.add(entry);
                        }
                    }

                    for (NgramEntry entry : entriesToRemove) {
                        ngramTable.removeEntry(entry);
                    }

                    if (ngramTable.getEntries().isEmpty()) {
                        ngramTableRepository.delete(ngramTable);
                    } else {
                        ngramTableRepository.save(ngramTable);
                    }
                }
            }
        }
    }
}