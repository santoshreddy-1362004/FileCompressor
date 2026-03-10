package com.filecompressor.core;

import com.filecompressor.model.CompressionResult;
import java.io.IOException;

/**
 * Strategy Pattern: Defines the interface for compression algorithms.
 * This allows different compression algorithms to be used interchangeably.
 *
 * SOLID Principles:
 * - Single Responsibility: Only defines compression/decompression contract
 * - Open/Closed: Open for extension (new algorithms), closed for modification
 * - Interface Segregation: Simple, focused interface
 */
public interface CompressionStrategy {

    /**
     * Compresses the input data using the specific algorithm.
     *
     * @param data the data to compress
     * @return CompressionResult containing compressed data and statistics
     * @throws IOException if compression fails
     */
    CompressionResult compress(byte[] data) throws IOException;

    /**
     * Decompresses the input data using the specific algorithm.
     *
     * @param compressedData the data to decompress
     * @return the original decompressed data
     * @throws IOException if decompression fails
     */
    byte[] decompress(byte[] compressedData) throws IOException;

    /**
     * Returns the name of the compression algorithm.
     *
     * @return algorithm name
     */
    String getAlgorithmName();
}
