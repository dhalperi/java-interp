package com.example.coderunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

/**
 * Compiles Java source code at runtime using the Java Compiler API.
 *
 * <p>This class handles the complexity of in-memory compilation, including:
 * <ul>
 *   <li>Creating source file objects from strings
 *   <li>Managing compiled bytecode in memory
 *   <li>Loading compiled classes via a custom ClassLoader
 * </ul>
 */
class RuntimeCompiler {
  private final JavaCompiler compiler;

  public RuntimeCompiler() {
    this.compiler = ToolProvider.getSystemJavaCompiler();
    if (compiler == null) {
      throw new IllegalStateException(
          "Java compiler not available. Ensure you're running with a JDK, not just a JRE.");
    }
  }

  /**
   * Compiles the given source code and returns the compiled class.
   *
   * @param sourceCode the Java source code to compile
   * @return the compiled Class object
   * @throws Exception if compilation fails
   */
  public Class<?> compile(String sourceCode) throws Exception {
    // Extract class name from source code
    String className = extractClassName(sourceCode);

    // Create a source file object
    JavaFileObject sourceFile = new StringSourceJavaFileObject(className, sourceCode);

    // Set up file manager for in-memory compilation
    InMemoryClassFileManager fileManager =
        new InMemoryClassFileManager(compiler.getStandardFileManager(null, null, null));

    // Compile
    JavaCompiler.CompilationTask task =
        compiler.getTask(
            null, // Use default writer for diagnostics
            fileManager,
            null, // Use default diagnostic listener
            null, // No compiler options
            null, // No annotation processing
            Collections.singletonList(sourceFile));

    boolean success = task.call();
    if (!success) {
      throw new RuntimeException("Compilation failed for code: " + sourceCode);
    }

    // Load the compiled class
    byte[] bytecode = fileManager.getCompiledBytes(className);
    return new ByteClassLoader().defineClass(className, bytecode);
  }

  /**
   * Extracts the class name from the source code.
   * Looks for "public class ClassName" or "class ClassName".
   */
  private String extractClassName(String sourceCode) {
    // Simple regex to extract class name
    String pattern = "(?:public\\s+)?class\\s+(\\w+)";
    java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
    java.util.regex.Matcher m = p.matcher(sourceCode);
    if (m.find()) {
      return m.group(1);
    }
    throw new IllegalArgumentException("Could not extract class name from source code");
  }

  /** A JavaFileObject that holds source code in memory. */
  private static class StringSourceJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    StringSourceJavaFileObject(String className, String code) {
      super(
          URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension),
          Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return code;
    }
  }

  /** A JavaFileObject that holds compiled bytecode in memory. */
  private static class ByteJavaFileObject extends SimpleJavaFileObject {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ByteJavaFileObject(String className) {
      super(
          URI.create("bytes:///" + className.replace('.', '/') + Kind.CLASS.extension),
          Kind.CLASS);
    }

    @Override
    public OutputStream openOutputStream() {
      return outputStream;
    }

    byte[] getBytes() {
      return outputStream.toByteArray();
    }
  }

  /** File manager that stores compiled classes in memory. */
  private static class InMemoryClassFileManager extends ForwardingJavaFileManager<JavaFileManager> {
    private ByteJavaFileObject compiledClass;

    InMemoryClassFileManager(JavaFileManager fileManager) {
      super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
        Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
      compiledClass = new ByteJavaFileObject(className);
      return compiledClass;
    }

    byte[] getCompiledBytes(String className) {
      if (compiledClass == null) {
        throw new IllegalStateException("No compiled class available");
      }
      return compiledClass.getBytes();
    }
  }

  /** ClassLoader that loads classes from byte arrays. */
  private static class ByteClassLoader extends ClassLoader {
    Class<?> defineClass(String name, byte[] bytes) {
      return defineClass(name, bytes, 0, bytes.length);
    }
  }
}
