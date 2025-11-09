# Quick Start Guide

## ⚠️ Security Warning
This tool executes arbitrary code without sandboxing. **For prototyping only!**

## Run Your First Example

1. **List available examples:**
   ```bash
   bazel run //:cli -- list
   ```

2. **Run a simple example:**
   ```bash
   bazel run //:cli -- example simple
   ```

3. **Try the Guava example:**
   ```bash
   bazel run //:cli -- example guava
   ```

## Run Code from a File

1. **Create a simple Java file** (`MyCode.java`):
   ```java
   import java.util.function.BiFunction;

   public class MyCode implements BiFunction<String, String, String> {
       @Override
       public String apply(String key, String data) {
           return key + " processed: " + data;
       }
   }
   ```

2. **Run it:**
   ```bash
   bazel run //:cli -- file /path/to/MyCode.java mykey "test data"
   ```

## Use the API in Your Code

```java
import com.example.coderunner.CodeRunner;

public class Example {
    public static void main(String[] args) throws Exception {
        CodeRunner<String, String, String> runner = new CodeRunner<>();

        String code = """
            import java.util.function.BiFunction;

            public class MyFunction implements BiFunction<String, String, String> {
                public String apply(String key, String data) {
                    return key + ": " + data.toUpperCase();
                }
            }
            """;

        String result = runner.runCode("test", "hello world", code);
        System.out.println(result);  // Prints: test: HELLO WORLD
    }
}
```

## Try the Examples

All examples are in the `examples/` directory. Run them using absolute paths:

```bash
# Word counter
bazel run //:cli -- file $PWD/examples/WordCounter.java doc "hello world test"

# Calculator
bazel run //:cli -- file $PWD/examples/Calculator.java add "10,20"

# List processor (uses Guava)
bazel run //:cli -- file $PWD/examples/ListProcessor.java items "a,b,c,d"
```

## What Can Your Code Do?

Your code can use:
- ✅ Java standard library
- ✅ Guava library
- ✅ Collections and Streams
- ✅ Any Java language features
- ⚠️ **BUT: No sandboxing! Full system access!**

## Next Steps

- Read the [full README](README.md) for details
- Check the [examples directory](examples/) for more samples
- Review [CodeRunner.java](src/main/java/com/example/coderunner/CodeRunner.java) for API details
