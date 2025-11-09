import java.util.function.BiFunction;

/**
 * Example: Reverses a string and prepends it with the key.
 *
 * Run with:
 *   bazel run //:cli -- file examples/StringReverser.java mykey "hello world"
 */
public class StringReverser implements BiFunction<String, String, String> {
  @Override
  public String apply(String key, String data) {
    return key + ": " + new StringBuilder(data).reverse().toString();
  }
}
