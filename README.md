# File Compressor

A file compression utility implemented in Java showcasing **SOLID principles** and **design patterns**. This project demonstrates clean code architecture, algorithmic problem-solving, and software engineering best practices.

## Features

- **Huffman Coding Algorithm**: Optimal prefix-free encoding for lossless compression
- **Multiple Design Patterns**: Strategy, Factory, and Builder patterns
- **SOLID Principles**: Clean, maintainable, and extensible architecture
- **Compression Statistics**: Detailed metrics on file size reduction and performance
- **Command Line Interface**: Easy-to-use CLI for compression operations
- **Comprehensive Testing**: Unit tests with JUnit 5

## Design Patterns Used

### 1. Strategy Pattern
**Location**: `CompressionStrategy` interface

Allows different compression algorithms to be used interchangeably without modifying client code.

```java
// You can easily swap algorithms
CompressionStrategy strategy = new HuffmanCompressionStrategy();
FileCompressor compressor = new FileCompressor(strategy);
```

**Benefits**:
- Open/Closed Principle: Open for extension (new algorithms), closed for modification
- Easy to add new compression algorithms (e.g., Run-Length Encoding, LZW)

### 2. Factory Pattern
**Location**: `CompressionStrategyFactory` class

Centralizes object creation and decouples client code from concrete implementations.

```java
CompressionStrategy strategy = CompressionStrategyFactory.createStrategy("huffman");
```

**Benefits**:
- Single point for creating compression strategies
- Easy to extend with new algorithms
- Hides instantiation complexity

### 3. Builder Pattern
**Location**: `CompressionResult.Builder` class

Provides a fluent API for constructing complex objects.

```java
CompressionResult result = new CompressionResult.Builder()
    .compressedData(data)
    .originalSize(size)
    .algorithmUsed("Huffman")
    .build();
```

**Benefits**:
- Readable object construction
- Immutable result objects
- Flexible parameter setting

## SOLID Principles

### Single Responsibility Principle (SRP)
Each class has one reason to change:
- `HuffmanTree`: Builds and manages Huffman tree structure
- `FileCompressor`: Handles file I/O operations
- `CompressionResult`: Encapsulates compression statistics
- `BitUtil`: Utility for bit manipulation

### Open/Closed Principle (OCP)
The system is open for extension, closed for modification:
- New compression algorithms can be added by implementing `CompressionStrategy`
- No need to modify existing code to add new algorithms

### Liskov Substitution Principle (LSP)
Any `CompressionStrategy` implementation can replace another:
```java
CompressionStrategy strategy1 = new HuffmanCompressionStrategy();
CompressionStrategy strategy2 = new RLECompressionStrategy(); // Future implementation
// Both can be used interchangeably
```

### Interface Segregation Principle (ISP)
Interfaces are focused and minimal:
- `CompressionStrategy` only defines compression/decompression methods
- Clients aren't forced to depend on methods they don't use

### Dependency Inversion Principle (DIP)
High-level modules depend on abstractions:
```java
public class FileCompressor {
    private final CompressionStrategy strategy; // Depends on interface, not implementation
}
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── filecompressor/
│               ├── algorithm/          # Huffman algorithm implementation
│               │   ├── HuffmanNode.java
│               │   ├── HuffmanTree.java
│               │   └── HuffmanCompressionStrategy.java
│               ├── cli/                # Command line interface
│               │   └── FileCompressorCLI.java
│               ├── core/               # Core interfaces and classes
│               │   ├── CompressionStrategy.java
│               │   └── FileCompressor.java
│               ├── factory/            # Factory pattern
│               │   └── CompressionStrategyFactory.java
│               ├── model/              # Data models
│               │   └── CompressionResult.java
│               └── util/               # Utilities
│                   └── BitUtil.java
└── test/
    └── java/
        └── com/
            └── filecompressor/         # Unit tests
                ├── HuffmanTreeTest.java
                ├── HuffmanCompressionStrategyTest.java
                └── CompressionStrategyFactoryTest.java
```

## How Huffman Coding Works

Huffman coding is a greedy algorithm that assigns variable-length codes to characters based on their frequencies:

1. **Count Frequencies**: Calculate how often each character appears
2. **Build Tree**: Create a binary tree using a min-heap (priority queue)
   - Characters with lower frequencies get longer codes
   - Characters with higher frequencies get shorter codes
3. **Generate Codes**: Traverse tree to assign binary codes (left=0, right=1)
4. **Encode**: Replace each character with its Huffman code
5. **Decode**: Use the tree to convert binary codes back to characters

**Example**:
```
Input: "hello"
Frequencies: h=1, e=1, l=2, o=1

Huffman Tree:
        5
       / \
      2   3
     /   / \
    l   h+e o

Codes: l=0, h=10, e=11, o=111
Encoded: "10 11 0 0 111" (compressed from 40 bits to ~15 bits)
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

```bash
# Clone the repository
git clone <repository-url>
cd FileCompression

# Build the project
mvn clean package

# Run tests
mvn test
```

This creates an executable JAR file in `target/file-compressor-1.0.0.jar`

## Usage

### Compress a File

```bash
java -jar target/file-compressor-1.0.0.jar compress input.txt compressed.huff
```

### Decompress a File

```bash
java -jar target/file-compressor-1.0.0.jar decompress compressed.huff output.txt
```

### Specify Algorithm (Optional)

```bash
java -jar target/file-compressor-1.0.0.jar compress input.txt compressed.huff huffman
```

### Help

```bash
java -jar target/file-compressor-1.0.0.jar help
```

## Example Output

```
File Compressor v1.0.0
================================
Operation: Compression
Input File: input.txt
Output File: compressed.huff
Algorithm: huffman

Compressing...

Compression Complete!
================================
Algorithm: Huffman Coding
Original Size: 1,024 bytes
Compressed Size: 612 bytes
Compression Ratio: 40.23%
Time Taken: 15 ms

Compressed file saved to: compressed.huff
```

## Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=HuffmanTreeTest

# Run with verbose output
mvn test -X
```

## Extending the Project

### Adding a New Compression Algorithm

1. Create a new class implementing `CompressionStrategy`:

```java
public class RLECompressionStrategy implements CompressionStrategy {
    @Override
    public CompressionResult compress(byte[] data) throws IOException {
        // Implement Run-Length Encoding
    }

    @Override
    public byte[] decompress(byte[] compressedData) throws IOException {
        // Implement RLE decompression
    }

    @Override
    public String getAlgorithmName() {
        return "Run-Length Encoding";
    }
}
```

2. Add it to the `CompressionStrategyFactory`:

```java
case RLE:
    return new RLECompressionStrategy();
```

That's it! No other code needs to change (Open/Closed Principle).

## Technical Highlights for Interviews

When discussing this project in interviews, emphasize:

1. **Algorithm Knowledge**: Huffman coding demonstrates understanding of:
   - Greedy algorithms
   - Binary trees and tree traversal
   - Priority queues (min-heap)
   - Bit manipulation

2. **Design Patterns**: Practical application of:
   - Strategy pattern for algorithm selection
   - Factory pattern for object creation
   - Builder pattern for complex object construction

3. **SOLID Principles**: Real-world application of all five principles

4. **Clean Code**:
   - Meaningful names
   - Single responsibility
   - Comprehensive documentation
   - Unit testing

5. **Software Engineering**:
   - Maven build system
   - Dependency management
   - CLI design
   - Error handling

## Future Enhancements

- [ ] Add Run-Length Encoding (RLE) algorithm
- [ ] Add LZW (Lempel-Ziv-Welch) algorithm
- [ ] Implement multithreading for large files
- [ ] Add progress bars for long operations
- [ ] Support for directory compression
- [ ] GUI interface using JavaFX
- [ ] Compression strength levels

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

Created as a demonstration project for software engineering interviews.

---

**Note**: This is an educational project designed to showcase software design principles and algorithmic knowledge. For production use, consider established compression libraries like Apache Commons Compress or Java's built-in compression utilities.
