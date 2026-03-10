package com.filecompressor.factory;

import com.filecompressor.algorithm.HuffmanCompressionStrategy;
import com.filecompressor.core.CompressionStrategy;

/**
 * Factory Pattern: Creates compression strategy instances.
 *
 * SOLID Principles:
 * - Open/Closed: Can add new algorithms without modifying existing code
 * - Single Responsibility: Only responsible for creating strategies
 *
 * This makes it easy to extend the system with new compression algorithms.
 */
public class CompressionStrategyFactory {

    /**
     * Enum defining available compression algorithms.
     */
    public enum Algorithm {
        HUFFMAN("huffman"),
        RLE("rle"); // Run-Length Encoding (for future implementation)

        private final String name;

        Algorithm(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        /**
         * Converts string to Algorithm enum.
         */
        public static Algorithm fromString(String algorithmName) {
            for (Algorithm algo : Algorithm.values()) {
                if (algo.name.equalsIgnoreCase(algorithmName)) {
                    return algo;
                }
            }
            throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }
    }

    /**
     * Creates a compression strategy based on the algorithm type.
     *
     * @param algorithm the algorithm to use
     * @return CompressionStrategy instance
     */
    public static CompressionStrategy createStrategy(Algorithm algorithm) {
        switch (algorithm) {
            case HUFFMAN:
                return new HuffmanCompressionStrategy();
            case RLE:
                // TODO: Implement RLE strategy
                throw new UnsupportedOperationException("RLE not yet implemented");
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
        }
    }

    /**
     * Creates a compression strategy from string name.
     *
     * @param algorithmName the algorithm name (e.g., "huffman")
     * @return CompressionStrategy instance
     */
    public static CompressionStrategy createStrategy(String algorithmName) {
        Algorithm algorithm = Algorithm.fromString(algorithmName);
        return createStrategy(algorithm);
    }

    /**
     * Gets the default compression strategy.
     */
    public static CompressionStrategy getDefaultStrategy() {
        return createStrategy(Algorithm.HUFFMAN);
    }

    /**
     * Lists all available algorithms.
     */
    public static String[] getAvailableAlgorithms() {
        Algorithm[] algorithms = Algorithm.values();
        String[] names = new String[algorithms.length];
        for (int i = 0; i < algorithms.length; i++) {
            names[i] = algorithms[i].getName();
        }
        return names;
    }
}
