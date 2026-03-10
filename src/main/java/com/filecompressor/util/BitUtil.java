package com.filecompressor.util;

/**
 * Utility class for bit manipulation operations.
 * Used to convert between binary strings and byte arrays.
 */
public class BitUtil {

    private BitUtil() {
        // Utility class, prevent instantiation
    }

    /**
     * Converts a binary string (e.g., "10110101") to a byte array.
     * Pads the last byte with zeros if necessary.
     *
     * @param binaryString the binary string to convert
     * @return byte array representation
     */
    public static byte[] binaryStringToBytes(String binaryString) {
        int length = binaryString.length();
        int numBytes = (length + 7) / 8; // Ceiling division
        byte[] bytes = new byte[numBytes];

        for (int i = 0; i < length; i++) {
            if (binaryString.charAt(i) == '1') {
                int byteIndex = i / 8;
                int bitIndex = 7 - (i % 8); // MSB first
                bytes[byteIndex] |= (1 << bitIndex);
            }
        }

        return bytes;
    }

    /**
     * Converts a byte array to a binary string.
     *
     * @param bytes the byte array to convert
     * @param bitLength the actual number of bits (to avoid padding bits)
     * @return binary string representation
     */
    public static String bytesToBinaryString(byte[] bytes, int bitLength) {
        StringBuilder binaryString = new StringBuilder();

        for (int i = 0; i < bitLength; i++) {
            int byteIndex = i / 8;
            int bitIndex = 7 - (i % 8);

            if (byteIndex >= bytes.length) {
                break;
            }

            int bit = (bytes[byteIndex] >> bitIndex) & 1;
            binaryString.append(bit);
        }

        return binaryString.toString();
    }

    /**
     * Converts a byte to a binary string (8 bits).
     *
     * @param b the byte to convert
     * @return 8-character binary string
     */
    public static String byteToBinaryString(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF))
                .replace(' ', '0');
    }

    /**
     * Prints binary representation of a byte array for debugging.
     */
    public static void printBinary(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(byteToBinaryString(b) + " ");
        }
        System.out.println();
    }
}
