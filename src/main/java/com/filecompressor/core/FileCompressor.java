package com.filecompressor.core;

import com.filecompressor.model.CompressionResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main class for file compression operations.
 * Uses dependency injection to work with different compression strategies.
 *
 * SOLID Principles:
 * - Dependency Inversion: Depends on CompressionStrategy interface, not concrete implementations
 * - Single Responsibility: Handles file I/O and delegates compression to strategy
 */
public class FileCompressor {
    private final CompressionStrategy strategy;

    /**
     * Constructor with dependency injection.
     *
     * @param strategy the compression strategy to use
     */
    public FileCompressor(CompressionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Compression strategy cannot be null");
        }
        this.strategy = strategy;
    }

    /**
     * Compresses a file using the configured strategy.
     *
     * @param inputFilePath path to the file to compress
     * @param outputFilePath path where compressed file will be saved
     * @return CompressionResult with statistics
     * @throws IOException if file operations fail
     */
    public CompressionResult compressFile(String inputFilePath, String outputFilePath) throws IOException {
        Path inputPath = Paths.get(inputFilePath);

        if (!Files.exists(inputPath)) {
            throw new IOException("Input file does not exist: " + inputFilePath);
        }

        // Read input file
        byte[] inputData = Files.readAllBytes(inputPath);
        long startTime = System.currentTimeMillis();

        // Compress using strategy
        CompressionResult result = strategy.compress(inputData);

        // Write compressed data to output file
        Files.write(Paths.get(outputFilePath), result.getCompressedData());

        return new CompressionResult.Builder()
                .compressedData(result.getCompressedData())
                .originalSize(inputData.length)
                .compressedSize(result.getCompressedData().length)
                .algorithmUsed(strategy.getAlgorithmName())
                .compressionTimeMs(System.currentTimeMillis() - startTime)
                .build();
    }

    /**
     * Decompresses a file using the configured strategy.
     *
     * @param inputFilePath path to the compressed file
     * @param outputFilePath path where decompressed file will be saved
     * @throws IOException if file operations fail
     */
    public void decompressFile(String inputFilePath, String outputFilePath) throws IOException {
        Path inputPath = Paths.get(inputFilePath);

        if (!Files.exists(inputPath)) {
            throw new IOException("Input file does not exist: " + inputFilePath);
        }

        // Read compressed file
        byte[] compressedData = Files.readAllBytes(inputPath);

        // Decompress using strategy
        byte[] decompressedData = strategy.decompress(compressedData);

        // Write decompressed data to output file
        Files.write(Paths.get(outputFilePath), decompressedData);
    }

    /**
     * Gets the name of the current compression strategy.
     */
    public String getStrategyName() {
        return strategy.getAlgorithmName();
    }
}
