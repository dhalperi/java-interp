# Java Code Runner

A Bazel-based Java project demonstrating runtime code compilation and execution. This project provides an API for compiling and running Java code provided as strings, allowing dynamic code execution at runtime.

## ⚠️ Security Warning

**THIS PROJECT IS FOR PROTOTYPING AND EDUCATIONAL PURPOSES ONLY**

This tool executes arbitrary Java code without any sandboxing or security restrictions. It is **NOT SAFE** for production use with untrusted input.

**DO NOT:**
- Use this with untrusted user input
- Deploy this in production environments
- Use this to execute code from unknown sources
- Expose this API to the internet

**Potential security risks include:**
- Arbitrary code execution
- File system access
- Network access
- Resource exhaustion
- System compromise

This project is designed for controlled development and prototyping environments only.

## Overview

The Java Code Runner provides an API that:
- Takes Java source code as a string
- Compiles it at runtime using the Java Compiler API
- Executes the compiled code with provided inputs
- Returns the output

The code must implement `BiFunction<U, D, T>` where:
- `U` is the type of the key
- `D` is the type of the key data
- `T` is the type of the result

## Features

- **Runtime compilation**: Compile Java code on-the-fly
- **Library access**: Code can use Java standard library and Guava
- **CLI interface**: Run code from command line or files
- **Built-in examples**: Several working examples demonstrating different use cases
- **Bazel build system**: Modern, scalable build configuration

## Prerequisites

- Bazel 8+ (configured for WORKSPACE)
- JDK 11 or later (must be a full JDK, not just JRE)

## Building

Build the project:

```bash
bazel build //:cli
```

## Usage

### List Available Examples

```bash
bazel run //:cli -- list
```

### Run a Built-in Example

```bash
bazel run //:cli -- example <example-name>
```

Examples:
- `simple` - String concatenation
- `math` - Mathematical operations
- `list` - List processing with streams
- `guava` - Guava library usage
- `filter` - Filtering and transforming data
- `map` - Map data structure processing

### Run Code from a File

```bash
bazel run //:cli -- file <absolute-path> <key> <keyData>
```

Example:
```bash
bazel run //:cli -- file /path/to/java-interp/examples/Calculator.java add "15,25"
```

**Note:** Use absolute paths when running code from files.

## API Usage

### Basic Example

```java
import com.example.coderunner.CodeRunner;

// Create a runner
CodeRunner<String, String, String> runner = new CodeRunner<>();

// Define code as a string
String code = """
    import java.util.function.BiFunction;

    public class Example implements BiFunction<String, String, String> {
        @Override
        public String apply(String key, String data) {
            return key + ": " + data.toUpperCase();
        }
    }
    """;

// Run the code
String result = runner.runCode("greeting", "hello world", code);
System.out.println(result); // Outputs: greeting: HELLO WORLD
```

### Compile Once, Run Multiple Times

```java
// Compile the code once
Class<?> compiledClass = runner.compile(code);

// Run it multiple times with different inputs
String result1 = runner.runCompiledCode("key1", "data1", compiledClass);
String result2 = runner.runCompiledCode("key2", "data2", compiledClass);
```

## Code Requirements

Code provided to the CodeRunner must:

1. Define a class (can be public or package-private)
2. Implement `BiFunction<U, D, T>`
3. Have a no-argument constructor (default or explicit)
4. Not rely on external files or resources

## Available Libraries

Code executed by the runner can access:

- **Java Standard Library**: All `java.*` and `javax.*` packages
- **Guava**: Google's core libraries (`com.google.common.*`)
- **Collections Framework**: Full access to Java Collections
- **Streams API**: Functional-style operations on collections

## Example Code Files

The `examples/` directory contains several working examples:

### StringReverser.java
Reverses a string and prepends it with the key.

```bash
bazel run //:cli -- file $PWD/examples/StringReverser.java mykey "hello world"
```

### WordCounter.java
Counts words in a string.

```bash
bazel run //:cli -- file $PWD/examples/WordCounter.java doc1 "The quick brown fox"
```

### Calculator.java
Performs mathematical operations.

```bash
bazel run //:cli -- file $PWD/examples/Calculator.java multiply "5,7"
```

### ListProcessor.java
Processes comma-separated values using Guava.

```bash
bazel run //:cli -- file $PWD/examples/ListProcessor.java mylist "apple,banana,cherry"
```

## Project Structure

```
java-interp/
├── BUILD.bazel                     # Bazel build configuration
├── WORKSPACE.bazel                 # Bazel workspace configuration
├── .bazelrc                        # Bazel configuration options
├── README.md                       # This file
├── maven_install.json              # Pinned Maven dependencies
├── examples/                       # Example code files
│   ├── Calculator.java
│   ├── ListProcessor.java
│   ├── StringReverser.java
│   └── WordCounter.java
└── src/main/java/com/example/coderunner/
    ├── CodeRunner.java             # Main API
    ├── RuntimeCompiler.java        # Compilation engine
    ├── CLI.java                    # Command-line interface
    └── Examples.java               # Built-in examples
```

## Implementation Details

### Runtime Compilation

The project uses the Java Compiler API (`javax.tools.JavaCompiler`) to compile source code at runtime:

1. Source code is wrapped in a `JavaFileObject`
2. Compilation is performed in-memory
3. Compiled bytecode is stored in memory
4. Classes are loaded via a custom `ClassLoader`

### ClassLoader Isolation

Each compiled class is loaded in its own `ClassLoader`, providing basic isolation between different code executions.

## Limitations

- **No sandboxing**: Code has full access to the JVM and system
- **No resource limits**: Code can consume unlimited CPU/memory
- **No network restrictions**: Code can make network calls
- **No file system restrictions**: Code can read/write files
- **Classpath limitations**: Only pre-configured libraries are available

## Future Enhancements (Not Implemented)

For a production system, consider:

- Security Manager or sandboxing (e.g., Seccomp, containers)
- Resource limits (CPU time, memory, threads)
- Network isolation
- File system restrictions
- Code signing and verification
- Rate limiting
- Audit logging

## Contributing

This is a prototype project for demonstration purposes. When sharing or using this code:

1. Always include the security warnings
2. Never deploy to production without proper sandboxing
3. Document any modifications
4. Test thoroughly in isolated environments

## License

This project is provided as-is for educational and prototyping purposes.

## References

- [Java Compiler API Documentation](https://docs.oracle.com/javase/8/docs/api/javax/tools/JavaCompiler.html)
- [Bazel Build System](https://bazel.build/)
- [Guava Libraries](https://github.com/google/guava)
