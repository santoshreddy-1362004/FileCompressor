package com.filecompressor.algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Builds and manages the Huffman tree for encoding/decoding.
 *
 * The Huffman algorithm:
 * 1. Count frequency of each character
 * 2. Build a min-heap (priority queue) of nodes
 * 3. Repeatedly merge two lowest-frequency nodes until one tree remains
 * 4. Generate binary codes by traversing the tree (left=0, right=1)
 */
public class HuffmanTree {
    private HuffmanNode root;
    private final Map<Character, String> encodingMap;
    private final Map<String, Character> decodingMap;

    public HuffmanTree() {
        this.encodingMap = new HashMap<>();
        this.decodingMap = new HashMap<>();
    }

    /**
     * Builds the Huffman tree from input data.
     * Uses greedy algorithm with priority queue.
     */
    public void buildTree(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Input data cannot be empty");
        }

        // Step 1: Calculate frequency of each character
        Map<Character, Integer> frequencyMap = calculateFrequencies(data);

        // Step 2: Build priority queue with leaf nodes
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Special case: single unique character
        if (priorityQueue.size() == 1) {
            HuffmanNode single = priorityQueue.poll();
            root = new HuffmanNode(single.getFrequency(), single, null);
        } else {
            // Step 3: Build tree by merging nodes
            while (priorityQueue.size() > 1) {
                HuffmanNode left = priorityQueue.poll();
                HuffmanNode right = priorityQueue.poll();

                int combinedFrequency = left.getFrequency() + right.getFrequency();
                HuffmanNode parent = new HuffmanNode(combinedFrequency, left, right);

                priorityQueue.add(parent);
            }

            root = priorityQueue.poll();
        }

        // Step 4: Generate encoding/decoding maps
        generateCodes(root, "");
    }

    /**
     * Calculates character frequencies in the data.
     */
    private Map<Character, Integer> calculateFrequencies(byte[] data) {
        Map<Character, Integer> frequencies = new HashMap<>();
        for (byte b : data) {
            char c = (char) (b & 0xFF); // Convert byte to char (0-255)
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        return frequencies;
    }

    /**
     * Recursively generates Huffman codes for each character.
     * Left = 0, Right = 1
     */
    private void generateCodes(HuffmanNode node, String code) {
        if (node == null) {
            return;
        }

        // Leaf node - store the code
        if (node.isLeaf()) {
            // Handle edge case: single character in file
            String finalCode = code.isEmpty() ? "0" : code;
            encodingMap.put(node.getCharacter(), finalCode);
            decodingMap.put(finalCode, node.getCharacter());
            return;
        }

        // Traverse left and right
        generateCodes(node.getLeft(), code + "0");
        generateCodes(node.getRight(), code + "1");
    }

    /**
     * Encodes data using the Huffman tree.
     */
    public String encode(byte[] data) {
        StringBuilder encoded = new StringBuilder();
        for (byte b : data) {
            char c = (char) (b & 0xFF);
            String code = encodingMap.get(c);
            if (code == null) {
                throw new IllegalStateException("Character not in encoding map: " + c);
            }
            encoded.append(code);
        }
        return encoded.toString();
    }

    /**
     * Decodes a binary string using the Huffman tree.
     */
    public byte[] decode(String encoded) {
        StringBuilder result = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);
            Character decodedChar = decodingMap.get(currentCode.toString());

            if (decodedChar != null) {
                result.append(decodedChar);
                currentCode.setLength(0); // Reset
            }
        }

        // Convert String to byte array
        byte[] output = new byte[result.length()];
        for (int i = 0; i < result.length(); i++) {
            output[i] = (byte) result.charAt(i);
        }

        return output;
    }

    public HuffmanNode getRoot() {
        return root;
    }

    public Map<Character, String> getEncodingMap() {
        return new HashMap<>(encodingMap);
    }

    /**
     * Prints the Huffman codes for debugging.
     */
    public void printCodes() {
        System.out.println("Huffman Codes:");
        encodingMap.forEach((character, code) ->
                System.out.printf("'%c' -> %s%n", character, code));
    }
}
