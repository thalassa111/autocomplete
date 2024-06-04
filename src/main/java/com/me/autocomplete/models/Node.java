package com.me.autocomplete.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nodes", schema = "autocomplete")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Node parent;

    @OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Node> children = new ArrayList<>();

    @Column(name = "character", length = 1)
    private char character;

    @Column(name = "is_end_of_word")
    private boolean endOfWord;

    @Column(name = "materialized_path")
    private String materializedPath;

    public Node() {
    }

    public Node(char character, String materializedPath) {
        this.character = character;
        this.materializedPath = materializedPath;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }

    public void setEndOfWord(boolean endOfWord) {
        this.endOfWord = endOfWord;
    }

    public String getMaterializedPath() {
        return materializedPath;
    }

    public void setMaterializedPath(String materializedPath) {
        this.materializedPath = materializedPath;
    }

    // add a child node
    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    // remove a child node
    public void removeChild(Node child) {
        children.remove(child);
        child.setParent(null);
    }

    public Node getChild(char currentChar) {
        for (Node child : children) {
            if (child.getCharacter() == currentChar) {
                System.out.println("getcharacter: " + child.getCharacter());
                return child;
            }
        }
        return null;
    }

}

