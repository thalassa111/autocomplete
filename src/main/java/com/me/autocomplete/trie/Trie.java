package com.me.autocomplete.trie;

import com.me.autocomplete.models.Node;
import com.me.autocomplete.repositories.TrieNodeRepository;

import com.me.autocomplete.utilities.Constants;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.scanner.Constant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Component
public class Trie {
    private Node root;
    private final TrieNodeRepository trieNodeRepository;

    public Trie(TrieNodeRepository trieNodeRepository) {
        this.root = new Node('*', "/");
        this.trieNodeRepository = trieNodeRepository;
    }

    public void insert(String word) {
        Node current = root;
        String materializedPath = "/";

        for (char ch : word.toCharArray()) {
            if (current.getChildren().stream().noneMatch(child -> child.getCharacter() == ch)) {
                Node newNode = new Node(ch, materializedPath + ch);
                current.addChild(newNode);
            }
            current = current.getChildren().stream()
                    .filter(child -> child.getCharacter() == ch)
                    .findFirst()
                    .orElseThrow(); // Assuming node already exists
            materializedPath += ch; // Update materialized path for the next iteration
        }

        // Mark the last node as the end of the word only if it's not already marked
        if (!current.isEndOfWord()) {
            current.setEndOfWord(true);
        }
    }

    public List<String> searchTrie(String prefix) {
        if (prefix.equals("/") || prefix.equals("*")) {
            System.out.println("returned null");
            return null;
        }
        List<String> matchingWords = new ArrayList<>();
        prefix = prefix.toLowerCase();
        // Retrieve nodes from the database with materialized paths starting with the prefix
        Optional<Node> optionalNode = trieNodeRepository.findByMaterializedPath(prefix);

        // Check if the optionalNode has a value
        if (optionalNode.isPresent()) {
            Node singleNode = optionalNode.get();
            System.out.println("single node: " + singleNode.getCharacter());
            // Traverse the tree downward from the matching node and collect all words in the subtree
            collectWordsFromSubtree(singleNode, new StringBuilder(prefix), matchingWords);
        }
        return matchingWords;
    }

    private void collectWordsFromSubtree(Node node, StringBuilder prefixBuilder, List<String> matchingWords) {
        // If it's the end of a word, add the constructed word to the list
        if (node.isEndOfWord() && matchingWords.size() < Constants.RESULT_SIZE) {
            matchingWords.add(prefixBuilder.toString());
        }

        // Traverse child nodes
        for (Node child : node.getChildren()) {
            if (child != null && matchingWords.size() < Constants.RESULT_SIZE) {
                // Append the character of the current node to the prefix
                char childCharacter = child.getCharacter();
                StringBuilder childPrefixBuilder = new StringBuilder(prefixBuilder);
                childPrefixBuilder.append(childCharacter);
                // Recursively traverse child nodes with the new prefix
                collectWordsFromSubtree(child, childPrefixBuilder, matchingWords);
            }
        }
    }

    public void loadFromDB() {
        parseFile();
        putInDB();
    }

    private void parseFile() {
        String fileName = "trie-words.txt";
        List<String> wordsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Trie wordList size: " + wordsList.size());
        for (String word : wordsList) {
            insert(word);
        }
    }

    private void putInDB() {
        System.out.println("putInDB");
        List<Node> nodesToSave = new ArrayList<>();
        putInDBHelper(root, nodesToSave, null);

        // Save nodes in batches
        int batchSize = 10000; // Adjust batch size as needed
        for (int i = 0; i < nodesToSave.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, nodesToSave.size());
            List<Node> batch = nodesToSave.subList(i, endIndex);
            trieNodeRepository.saveAll(batch);
        }
    }

    private void putInDBHelper(Node node, List<Node> nodesToSave, Node parent) {
        if (node == null) {
            return;
        }

        // Create a new Node entity
        Node newNode = new Node();
        newNode.setCharacter(node.getCharacter());
        newNode.setEndOfWord(node.isEndOfWord());
        newNode.setMaterializedPath(node.getMaterializedPath());
        newNode.setParent(parent);
        nodesToSave.add(newNode); // Add node to the list

        // Recursively insert children nodes
        for (Node child : node.getChildren()) {
            putInDBHelper(child, nodesToSave, newNode);
        }
    }

    @Transactional
    public void insertWord(String word) {
        Node currentNode = findRootNode();
        int counter = 0;

        // Traverse the trie to insert the word
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            Node childNode = currentNode.getChildren().stream()
                    .filter(node -> node.getCharacter() == currentChar)
                    .findFirst()
                    .orElse(null);

            // If the child node doesn't exist, create a new one
            if (childNode == null) {
                counter++;
                System.out.println("counter: " + counter);
                String materializedPath = currentNode.getMaterializedPath() + currentChar;
                childNode = new Node(currentChar, materializedPath);
                childNode.setParent(currentNode); // Set the parent node
                currentNode.addChild(childNode);
                trieNodeRepository.save(currentNode); // Save the parent node
                trieNodeRepository.save(childNode); // Save the new child node
            }

            // Move to the next level in the trie
            currentNode = childNode;
        }

        // Mark the last character node as the end of the word
        if (!currentNode.isEndOfWord()) {
            currentNode.setEndOfWord(true);
            trieNodeRepository.save(currentNode); // Save the last node (end of word) to the database
        }
    }

    private Node findRootNode() {
        return trieNodeRepository.findByParentIsNull()
                .orElseThrow(() -> new IllegalStateException("Root node not found"));
    }

    @Transactional
    public void deleteWord(String word) {
        Node currentNode = findRootNode();
        Stack<Node> nodeStack = new Stack<>();

        // Traverse the trie to find the node corresponding to the last character of the word
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            currentNode = currentNode.getChildren().stream()
                    .filter(node -> node.getCharacter() == currentChar)
                    .findFirst()
                    .orElse(null);

            if (currentNode == null) {
                // The word does not exist in the trie
                return;
            }

            nodeStack.push(currentNode);
        }

        Node nodeToDelete = nodeStack.pop();

        if (nodeToDelete.isEndOfWord()) {
            nodeToDelete.setEndOfWord(false); // Unmark the node as end of word

            while (!nodeStack.isEmpty() && nodeToDelete.getChildren().isEmpty() && !nodeToDelete.isEndOfWord()) {
                Node parent = nodeStack.pop();
                parent.removeChild(nodeToDelete);
                trieNodeRepository.delete(nodeToDelete);
                nodeToDelete = parent;
            }

            // Check the root node separately to ensure correct cleanup
            if (nodeToDelete.getChildren().isEmpty() && !nodeToDelete.isEndOfWord()) {
                Node parent = !nodeStack.isEmpty() ? nodeStack.pop() : findRootNode();
                parent.removeChild(nodeToDelete);
                trieNodeRepository.delete(nodeToDelete);
            } else {
                trieNodeRepository.save(nodeToDelete);
            }
        }
    }
}