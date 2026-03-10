package com.filecompressor.algorithm;

/**
 * Represents a node in the Huffman tree.
 * This is a fundamental data structure for the Huffman coding algorithm.
 *
 * Each node contains:
 * - A character (for leaf nodes) or null (for internal nodes)
 * - A frequency count
 * - Left and right children
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    private final Character character;
    private final int frequency;
    private final HuffmanNode left;
    private final HuffmanNode right;

    /**
     * Constructor for leaf nodes (contains a character).
     */
    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructor for internal nodes (no character, only frequency).
     */
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.character = null;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public Character getCharacter() {
        return character;
    }

    public int getFrequency() {
        return frequency;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    /**
     * Checks if this is a leaf node (contains a character).
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }

    /**
     * Compares nodes by frequency for priority queue ordering.
     * Lower frequency = higher priority (comes first).
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.frequency, other.frequency);
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return String.format("Leaf(%c:%d)", character, frequency);
        }
        return String.format("Internal(%d)", frequency);
    }
}
