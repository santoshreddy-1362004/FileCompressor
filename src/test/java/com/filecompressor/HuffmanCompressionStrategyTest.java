package com.filecompressor.algorithm;

import com.filecompressor.model.CompressionResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HuffmanCompressionStrategy.
 */
class HuffmanCompressionStrategyTest {

    private HuffmanCompressionStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new HuffmanCompressionStrategy();
    }

    @Test
    void testGetAlgorithmName() {
        assertEquals("Huffman Coding", strategy.getAlgorithmName());
    }

    @Test
    void testCompress_ValidData() throws IOException {
        // Arrange
        String input = "hello world";
        byte[] data = input.getBytes();

        // Act
        CompressionResult result = strategy.compress(data);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getCompressedData());
        assertTrue(result.getCompressedData().length > 0);
        assertEquals("Huffman Coding", result.getAlgorithmUsed());
    }

    @Test
    void testCompress_EmptyData() {
        // Arrange
        byte[] data = new byte[0];

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> strategy.compress(data));
    }

    @Test
    void testCompress_NullData() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> strategy.compress(null));
    }

    @Test
    void testCompressDecompress_RoundTrip() throws IOException {
        // Arrange
        String input = "This is a test string for compression!";
        byte[] originalData = input.getBytes();

        // Act
        CompressionResult compressResult = strategy.compress(originalData);
        byte[] decompressed = strategy.decompress(compressResult.getCompressedData());

        // Assert
        assertArrayEquals(originalData, decompressed);
        assertEquals(input, new String(decompressed));
    }

    @Test
    void testCompressDecompress_LargeText() throws IOException {
        // Arrange
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("The quick brown fox jumps over the lazy dog. ");
        }
        byte[] originalData = sb.toString().getBytes();

        // Act
        CompressionResult compressResult = strategy.compress(originalData);
        byte[] decompressed = strategy.decompress(compressResult.getCompressedData());

        // Assert
        assertArrayEquals(originalData, decompressed);
        // Verify actual compression occurred
        assertTrue(compressResult.getCompressedSize() < originalData.length);
    }

    @Test
    void testCompressDecompress_SingleCharacter() throws IOException {
        // Arrange
        byte[] originalData = "aaaaaaa".getBytes();

        // Act
        CompressionResult compressResult = strategy.compress(originalData);
        byte[] decompressed = strategy.decompress(compressResult.getCompressedData());

        // Assert
        assertArrayEquals(originalData, decompressed);
    }

    @Test
    void testCompressionResult_Statistics() throws IOException {
        // Arrange
        String input = "compress this text";
        byte[] data = input.getBytes();

        // Act
        CompressionResult result = strategy.compress(data);

        // Assert
        assertEquals(data.length, result.getOriginalSize());
        assertTrue(result.getCompressedSize() > 0);
        assertTrue(result.getCompressionRatio() >= 0);
    }
}
