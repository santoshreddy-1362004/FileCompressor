package com.filecompressor.cli;

import com.filecompressor.core.CompressionStrategy;
import com.filecompressor.core.FileCompressor;
import com.filecompressor.factory.CompressionStrategyFactory;
import com.filecompressor.model.CompressionResult;

import java.io.IOException;
import java.util.Arrays;

/**
 * Command Line Interface for the File Compressor application.
 * Provides user-friendly interface for compression and decompression operations.
 */
public class FileCompressorCLI {

    private static final String VERSION = "1.0.0";

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            String command = args[0].toLowerCase();

            switch (command) {
                case "compress":
                case "-c":
                    handleCompress(args);
                    break;
                case "decompress":
                case "-d":
                    handleDecompress(args);
                    break;
                case "help":
                case "-h":
                case "--help":
                    printUsage();
                    break;
                case "version":
                case "-v":
                case "--version":
                    printVersion();
                    break;
                default:
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Handles compression command.
     * Usage: compress <input_file> <output_file> [algorithm]
     */
    private static void handleCompress(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Error: Missing required arguments for compress");
            System.err.println("Usage: compress <input_file> <output_file> [algorithm]");
            System.exit(1);
        }

        String inputFile = args[1];
        String outputFile = args[2];
        String algorithmName = args.length > 3 ? args[3] : "huffman";

        System.out.println("File Compressor v" + VERSION);
        System.out.println("================================");
        System.out.println("Operation: Compression");
        System.out.println("Input File: " + inputFile);
        System.out.println("Output File: " + outputFile);
        System.out.println("Algorithm: " + algorithmName);
        System.out.println();

        // Create strategy and compressor
        CompressionStrategy strategy = CompressionStrategyFactory.createStrategy(algorithmName);
        FileCompressor compressor = new FileCompressor(strategy);

        // Compress the file
        System.out.println("Compressing...");
        long startTime = System.currentTimeMillis();

        CompressionResult result = compressor.compressFile(inputFile, outputFile);

        long endTime = System.currentTimeMillis();

        // Display results
        System.out.println("\nCompression Complete!");
        System.out.println("================================");
        System.out.println(result.getStatistics());
        System.out.println("Total Time: " + (endTime - startTime) + " ms");
        System.out.println("\nCompressed file saved to: " + outputFile);
    }

    /**
     * Handles decompression command.
     * Usage: decompress <input_file> <output_file> [algorithm]
     */
    private static void handleDecompress(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Error: Missing required arguments for decompress");
            System.err.println("Usage: decompress <input_file> <output_file> [algorithm]");
            System.exit(1);
        }

        String inputFile = args[1];
        String outputFile = args[2];
        String algorithmName = args.length > 3 ? args[3] : "huffman";

        System.out.println("File Compressor v" + VERSION);
        System.out.println("================================");
        System.out.println("Operation: Decompression");
        System.out.println("Input File: " + inputFile);
        System.out.println("Output File: " + outputFile);
        System.out.println("Algorithm: " + algorithmName);
        System.out.println();

        // Create strategy and compressor
        CompressionStrategy strategy = CompressionStrategyFactory.createStrategy(algorithmName);
        FileCompressor compressor = new FileCompressor(strategy);

        // Decompress the file
        System.out.println("Decompressing...");
        long startTime = System.currentTimeMillis();

        compressor.decompressFile(inputFile, outputFile);

        long endTime = System.currentTimeMillis();

        // Display results
        System.out.println("\nDecompression Complete!");
        System.out.println("Time Taken: " + (endTime - startTime) + " ms");
        System.out.println("Decompressed file saved to: " + outputFile);
    }

    /**
     * Prints usage information.
     */
    private static void printUsage() {
        System.out.println("File Compressor v" + VERSION);
        System.out.println("================================");
        System.out.println("\nUSAGE:");
        System.out.println("  java -jar file-compressor.jar <command> [arguments]");
        System.out.println("\nCOMMANDS:");
        System.out.println("  compress, -c     Compress a file");
        System.out.println("  decompress, -d   Decompress a file");
        System.out.println("  help, -h         Show this help message");
        System.out.println("  version, -v      Show version information");
        System.out.println("\nCOMPRESS USAGE:");
        System.out.println("  compress <input_file> <output_file> [algorithm]");
        System.out.println("\nDECOMPRESS USAGE:");
        System.out.println("  decompress <input_file> <output_file> [algorithm]");
        System.out.println("\nAVAILABLE ALGORITHMS:");
        String[] algorithms = CompressionStrategyFactory.getAvailableAlgorithms();
        for (String algo : algorithms) {
            System.out.println("  - " + algo);
        }
        System.out.println("\nEXAMPLES:");
        System.out.println("  # Compress a file using default (Huffman) algorithm");
        System.out.println("  java -jar file-compressor.jar compress input.txt compressed.huff");
        System.out.println("\n  # Compress with specific algorithm");
        System.out.println("  java -jar file-compressor.jar compress input.txt compressed.huff huffman");
        System.out.println("\n  # Decompress a file");
        System.out.println("  java -jar file-compressor.jar decompress compressed.huff output.txt huffman");
    }

    /**
     * Prints version information.
     */
    private static void printVersion() {
        System.out.println("File Compressor v" + VERSION);
        System.out.println("Compression algorithms: " + Arrays.toString(CompressionStrategyFactory.getAvailableAlgorithms()));
    }
}
