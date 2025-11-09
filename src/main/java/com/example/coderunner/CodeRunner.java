package com.example.coderunner;

import java.util.function.BiFunction;

/**
 * Main API for running dynamically compiled code.
 *
 * <p>WARNING: This API executes arbitrary code without sandboxing. It is NOT safe for production
 * use with untrusted input. Use only in controlled environments for prototyping and development.
 *
 * @param <U> the type of the key
 * @param <D> the type of the key data
 * @param <T> the type of the result
 */
public class CodeRunner<U, D, T> {
  private final RuntimeCompiler compiler;

  public CodeRunner() {
    this.compiler = new RuntimeCompiler();
  }

  /**
   * Compiles and runs the provided code with the given key and keyData.
   *
   * <p>The code must define a class that implements BiFunction&lt;U, D, T&gt; with a no-arg
   * constructor. The code can use:
   * <ul>
   *   <li>java.util.*
   *   <li>com.google.common.* (Guava)
   *   <li>java.util.function.*
   * </ul>
   *
   * @param key the key value to pass to the function
   * @param keyData the data associated with the key
   * @param code the Java source code as a string
   * @return the result of executing the function
   * @throws Exception if compilation or execution fails
   */
  public T runCode(U key, D keyData, String code) throws Exception {
    // Compile the code
    Class<?> compiledClass = compiler.compile(code);

    // Create an instance and cast to BiFunction
    Object instance = compiledClass.getDeclaredConstructor().newInstance();

    @SuppressWarnings("unchecked")
    BiFunction<U, D, T> function = (BiFunction<U, D, T>) instance;

    // Execute the function
    return function.apply(key, keyData);
  }

  /**
   * Variant that allows reusing a compiled class for multiple invocations.
   *
   * @param key the key value
   * @param keyData the data associated with the key
   * @param compiledClass the previously compiled class
   * @return the result of execution
   * @throws Exception if instantiation or execution fails
   */
  @SuppressWarnings("unchecked")
  public T runCompiledCode(U key, D keyData, Class<?> compiledClass) throws Exception {
    Object instance = compiledClass.getDeclaredConstructor().newInstance();
    BiFunction<U, D, T> function = (BiFunction<U, D, T>) instance;
    return function.apply(key, keyData);
  }

  /**
   * Compiles code without executing it.
   *
   * @param code the Java source code
   * @return the compiled class
   * @throws Exception if compilation fails
   */
  public Class<?> compile(String code) throws Exception {
    return compiler.compile(code);
  }
}
