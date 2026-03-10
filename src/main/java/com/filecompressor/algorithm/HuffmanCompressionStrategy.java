package com.filecompressor.algorithm;

import com.filecompressor.core.CompressionStrategy;
import com.filecompressor.model.CompressionResult;
import com.filecompressor.util.BitUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * Huffman Coding compression strategy implementation.
 * Implements the Strategy pattern for pluggable compression algorithms.
 *
 * Compressed file format:
 * [Encoding Map Size][Encoding Map][Encoded Data Length][Encoded Data]
 */
public class HuffmanCompressionStrategy implements CompressionStrategy {

    @Override
    public CompressionResult compress(byte[] data) throws IOException {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be empty");
        }

        // Build Huffman tree and get encoding map
        HuffmanTree tree = new HuffmanTree();
        tree.buildTree(data);

        // Encode the data
        String encodedString = tree.encode(data);

        // Convert binary string to bytes
        byte[] encodedBytes = BitUtil.binaryStringToBytes(encodedString);

        // Serialize the encoding map and combine with encoded data
        byte[] compressedData = createCompressedFormat(
                tree.getEncodingMap(),
                encodedBytes,
                encodedString.length()
        );

        return new CompressionResult.Builder()
                .compressedData(compressedData)
                .originalSize(data.length)
                .compressedSize(compressedData.length)
                .algorithmUsed(getAlgorithmName())
                .compressionTimeMs(0) // Will be set by FileCompressor
                .build();
    }

    @Override
    public byte[] decompress(byte[] compressedData) throws IOException {
        if (compressedData == null || compressedData.length == 0) {
            throw new IllegalArgumentException("Compressed data cannot be empty");
        }

        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            // Read encoding map
            @SuppressWarnings("unchecked")
            Map<Character, String> encodingMap = (Map<Character, String>) ois.readObject();

            // Read encoded data length (in bits)
            int encodedLength = ois.readInt();

            // Read encoded bytes
            byte[] encodedBytes = ois.readAllBytes();

            // Convert bytes back to binary string
            String encodedString = BitUtil.bytesToBinaryString(encodedBytes, encodedLength);

            // Create reverse map for decoding
            HuffmanTree tree = new HuffmanTree();
            // We need to rebuild the tree or use the encoding map
            // For simplicity, we'll decode using the map directly
            return decodeWithMap(encodedString, encodingMap);

        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize encoding map", e);
        }
    }

    /**
     * Creates the compressed file format with metadata.
     */
    private byte[] createCompressedFormat(Map<Character, String> encodingMap,
                                          byte[] encodedData,
                                          int encodedLength) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            // Write encoding map
            oos.writeObject(encodingMap);

            // Write the length of encoded data in bits
            oos.writeInt(encodedLength);

            // Write encoded data
            oos.write(encodedData);

            oos.flush();
            return baos.toByteArray();
        }
    }

    /**
     * Decodes binary string using the encoding map.
     */
    private byte[] decodeWithMap(String encoded, Map<Character, String> encodingMap) {
        // Create reverse map
        Map<String, Character> reverseMap = new java.util.HashMap<>();
        for (Map.Entry<Character, String> entry : encodingMap.entrySet()) {
            reverseMap.put(entry.getValue(), entry.getKey());
        }

        StringBuilder result = new StringBuilder();
        StringBuilder currentCode = new StringBuilder();

        for (char bit : encoded.toCharArray()) {
            currentCode.append(bit);
            Character decodedChar = reverseMap.get(currentCode.toString());

            if (decodedChar != null) {
                result.append(decodedChar);
                currentCode.setLength(0);
            }
        }

        // Convert to byte array
        byte[] output = new byte[result.length()];
        for (int i = 0; i < result.length(); i++) {
            output[i] = (byte) result.charAt(i);
        }

        return output;
    }

    @Override
    public String getAlgorithmName() {
        return "Huffman Coding";
    }
}
