package com.example.coderunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Command-line interface for the Java Code Runner.
 *
 * <p>Usage:
 * <pre>
 *   # Run a predefined example
 *   bazel run //:cli -- example &lt;example-name&gt;
 *
 *   # Run code from a file
 *   bazel run //:cli -- file &lt;path-to-java-file&gt; &lt;key&gt; &lt;keyData&gt;
 *
 *   # List available examples
 *   bazel run //:cli -- list
 * </pre>
 *
 * <p>WARNING: This tool executes arbitrary Java code without sandboxing. Do not use with untrusted
 * input.
 */
public class CLI {

  public static void main(String[] args) {
    System.out.println("═══════════════════════════════════════════════════════════════");
    System.out.println("  Java Code Runner - Runtime Compilation & Execution");
    System.out.println("═══════════════════════════════════════════════════════════════");
    System.out.println();
    System.out.println("⚠️  WARNING: This tool executes arbitrary code without sandboxing.");
    System.out.println("   Do NOT use with untrusted input. For prototyping only.");
    System.out.println();
    System.out.println("═══════════════════════════════════════════════════════════════");
    System.out.println();

    if (args.length == 0) {
      printUsage();
      System.exit(1);
    }

    String command = args[0];

    try {
      switch (command) {
        case "list":
          listExamples();
          break;
        case "example":
          if (args.length < 2) {
            System.err.println("Error: example name required");
            printUsage();
            System.exit(1);
          }
          runExample(args[1]);
          break;
        case "file":
          if (args.length < 4) {
            System.err.println("Error: file path, key, and keyData required");
            printUsage();
            System.exit(1);
          }
          runFile(args[1], args[2], args[3]);
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

  private static void printUsage() {
    System.out.println("Usage:");
    System.out.println("  bazel run //:cli -- list");
    System.out.println("  bazel run //:cli -- example <example-name>");
    System.out.println("  bazel run //:cli -- file <path> <key> <keyData>");
    System.out.println();
    System.out.println("Commands:");
    System.out.println("  list     - List all available examples");
    System.out.println("  example  - Run a predefined example");
    System.out.println("  file     - Run code from a file");
  }

  private static void listExamples() {
    System.out.println("Available examples:");
    System.out.println();
    Examples.getExamples().forEach((name, example) -> {
      System.out.println("  • " + name);
      System.out.println("    " + example.description);
      System.out.println();
    });
  }

  private static void runExample(String exampleName) throws Exception {
    Examples.Example example = Examples.getExamples().get(exampleName);
    if (example == null) {
      System.err.println("Unknown example: " + exampleName);
      System.err.println("Run 'bazel run //:cli -- list' to see available examples");
      System.exit(1);
    }

    System.out.println("Running example: " + exampleName);
    System.out.println("Description: " + example.description);
    System.out.println();
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println("Code:");
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println(example.code);
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println();

    CodeRunner<Object, Object, Object> runner = new CodeRunner<>();
    Object result = runner.runCode(example.key, example.keyData, example.code);

    System.out.println("Result: " + result);
    System.out.println("Result type: " + result.getClass().getName());
  }

  private static void runFile(String filePath, String key, String keyData) throws Exception {
    Path path = Paths.get(filePath);
    if (!Files.exists(path)) {
      throw new IOException("File not found: " + filePath);
    }

    String code = Files.readString(path);

    System.out.println("Running code from file: " + filePath);
    System.out.println("Key: " + key);
    System.out.println("Key Data: " + keyData);
    System.out.println();
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println("Code:");
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println(code);
    System.out.println("────────────────────────────────────────────────────────────────");
    System.out.println();

    CodeRunner<String, String, Object> runner = new CodeRunner<>();
    Object result = runner.runCode(key, keyData, code);

    System.out.println("Result: " + result);
    System.out.println("Result type: " + result.getClass().getName());
  }
}
