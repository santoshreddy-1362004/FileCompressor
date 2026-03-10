package com.filecompressor.algorithm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HuffmanTree class.
 * Demonstrates testing of the Huffman coding algorithm.
 */
class HuffmanTreeTest {

    private HuffmanTree tree;

    @BeforeEach
    void setUp() {
        tree = new HuffmanTree();
    }

    @Test
    void testBuildTree_SimpleString() {
        // Arrange
        String input = "hello";
        byte[] data = input.getBytes();

        // Act
        tree.buildTree(data);

        // Assert
        assertNotNull(tree.getRoot());
        assertFalse(tree.getEncodingMap().isEmpty());
        assertEquals(4, tree.getEncodingMap().size()); // h, e, l, o
    }

    @Test
    void testBuildTree_SingleCharacter() {
        // Arrange
        byte[] data = "aaaa".getBytes();

        // Act
        tree.buildTree(data);

        // Assert
        assertNotNull(tree.getRoot());
        assertEquals(1, tree.getEncodingMap().size());
        assertTrue(tree.getEncodingMap().containsKey('a'));
    }

    @Test
    void testBuildTree_EmptyData() {
        // Arrange
        byte[] data = new byte[0];

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> tree.buildTree(data));
    }

    @Test
    void testEncode_SimpleString() {
        // Arrange
        String input = "aaa";
        byte[] data = input.getBytes();
        tree.buildTree(data);

        // Act
        String encoded = tree.encode(data);

        // Assert
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());
        assertTrue(encoded.matches("[01]+")); // Only 0s and 1s
    }

    @Test
    void testEncodeDecode_RoundTrip() {
        // Arrange
        String input = "hello world";
        byte[] originalData = input.getBytes();
        tree.buildTree(originalData);

        // Act
        String encoded = tree.encode(originalData);
        byte[] decoded = tree.decode(encoded);

        // Assert
        assertArrayEquals(originalData, decoded);
    }

    @Test
    void testEncodeDecode_LongText() {
        // Arrange
        String input = "The quick brown fox jumps over the lazy dog. " +
                       "The quick brown fox jumps over the lazy dog.";
        byte[] originalData = input.getBytes();
        tree.buildTree(originalData);

        // Act
        String encoded = tree.encode(originalData);
        byte[] decoded = tree.decode(encoded);

        // Assert
        assertArrayEquals(originalData, decoded);
        // Verify compression (encoded should be shorter in bits)
        assertTrue(encoded.length() < originalData.length * 8);
    }

    @Test
    void testGetEncodingMap_ReturnsValidCodes() {
        // Arrange
        byte[] data = "abcabc".getBytes();
        tree.buildTree(data);

        // Act
        var encodingMap = tree.getEncodingMap();

        // Assert
        assertNotNull(encodingMap);
        assertEquals(3, encodingMap.size()); // a, b, c

        // All codes should be binary strings
        for (String code : encodingMap.values()) {
            assertTrue(code.matches("[01]+"));
        }
    }
}
