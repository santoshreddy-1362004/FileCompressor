package com.filecompressor.factory;

import com.filecompressor.core.CompressionStrategy;
import com.filecompressor.algorithm.HuffmanCompressionStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CompressionStrategyFactory.
 * Tests the Factory Pattern implementation.
 */
class CompressionStrategyFactoryTest {

    @Test
    void testCreateStrategy_Huffman() {
        // Act
        CompressionStrategy strategy = CompressionStrategyFactory.createStrategy("huffman");

        // Assert
        assertNotNull(strategy);
        assertTrue(strategy instanceof HuffmanCompressionStrategy);
        assertEquals("Huffman Coding", strategy.getAlgorithmName());
    }

    @Test
    void testCreateStrategy_HuffmanEnum() {
        // Act
        CompressionStrategy strategy = CompressionStrategyFactory.createStrategy(
                CompressionStrategyFactory.Algorithm.HUFFMAN
        );

        // Assert
        assertNotNull(strategy);
        assertTrue(strategy instanceof HuffmanCompressionStrategy);
    }

    @Test
    void testCreateStrategy_CaseInsensitive() {
        // Act
        CompressionStrategy strategy1 = CompressionStrategyFactory.createStrategy("HUFFMAN");
        CompressionStrategy strategy2 = CompressionStrategyFactory.createStrategy("HuFfMaN");

        // Assert
        assertNotNull(strategy1);
        assertNotNull(strategy2);
        assertEquals(strategy1.getAlgorithmName(), strategy2.getAlgorithmName());
    }

    @Test
    void testCreateStrategy_InvalidAlgorithm() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                CompressionStrategyFactory.createStrategy("invalid")
        );
    }

    @Test
    void testGetDefaultStrategy() {
        // Act
        CompressionStrategy strategy = CompressionStrategyFactory.getDefaultStrategy();

        // Assert
        assertNotNull(strategy);
        assertTrue(strategy instanceof HuffmanCompressionStrategy);
    }

    @Test
    void testGetAvailableAlgorithms() {
        // Act
        String[] algorithms = CompressionStrategyFactory.getAvailableAlgorithms();

        // Assert
        assertNotNull(algorithms);
        assertTrue(algorithms.length > 0);
        assertTrue(containsIgnoreCase(algorithms, "huffman"));
    }

    @Test
    void testAlgorithmFromString() {
        // Act
        CompressionStrategyFactory.Algorithm algo =
                CompressionStrategyFactory.Algorithm.fromString("huffman");

        // Assert
        assertEquals(CompressionStrategyFactory.Algorithm.HUFFMAN, algo);
    }

    @Test
    void testAlgorithmFromString_Invalid() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                CompressionStrategyFactory.Algorithm.fromString("invalid")
        );
    }

    // Helper method
    private boolean containsIgnoreCase(String[] array, String value) {
        for (String item : array) {
            if (item.equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
