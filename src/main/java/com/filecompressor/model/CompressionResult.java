package com.filecompressor.model;

/**
 * Model class to encapsulate compression results and statistics.
 * Follows the Single Responsibility Principle.
 */
public class CompressionResult {
    private final byte[] compressedData;
    private final long originalSize;
    private final long compressedSize;
    private final String algorithmUsed;
    private final long compressionTimeMs;

    private CompressionResult(Builder builder) {
        this.compressedData = builder.compressedData;
        this.originalSize = builder.originalSize;
        this.compressedSize = builder.compressedSize;
        this.algorithmUsed = builder.algorithmUsed;
        this.compressionTimeMs = builder.compressionTimeMs;
    }

    public byte[] getCompressedData() {
        return compressedData;
    }

    public long getOriginalSize() {
        return originalSize;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public String getAlgorithmUsed() {
        return algorithmUsed;
    }

    public long getCompressionTimeMs() {
        return compressionTimeMs;
    }

    /**
     * Calculates the compression ratio as a percentage.
     *
     * @return compression ratio (0-100)
     */
    public double getCompressionRatio() {
        if (originalSize == 0) {
            return 0.0;
        }
        return (1 - ((double) compressedSize / originalSize)) * 100;
    }

    /**
     * Returns a formatted string with compression statistics.
     */
    public String getStatistics() {
        return String.format(
                "Algorithm: %s%n" +
                "Original Size: %,d bytes%n" +
                "Compressed Size: %,d bytes%n" +
                "Compression Ratio: %.2f%%%n" +
                "Time Taken: %d ms",
                algorithmUsed,
                originalSize,
                compressedSize,
                getCompressionRatio(),
                compressionTimeMs
        );
    }

    /**
     * Builder Pattern for creating CompressionResult instances.
     * Makes the object creation more readable and flexible.
     */
    public static class Builder {
        private byte[] compressedData;
        private long originalSize;
        private long compressedSize;
        private String algorithmUsed;
        private long compressionTimeMs;

        public Builder compressedData(byte[] data) {
            this.compressedData = data;
            return this;
        }

        public Builder originalSize(long size) {
            this.originalSize = size;
            return this;
        }

        public Builder compressedSize(long size) {
            this.compressedSize = size;
            return this;
        }

        public Builder algorithmUsed(String algorithm) {
            this.algorithmUsed = algorithm;
            return this;
        }

        public Builder compressionTimeMs(long time) {
            this.compressionTimeMs = time;
            return this;
        }

        public CompressionResult build() {
            return new CompressionResult(this);
        }
    }
}
