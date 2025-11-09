import java.util.function.BiFunction;

/**
 * Example: Counts words in a string.
 *
 * Run with:
 *   bazel run //:cli -- file examples/WordCounter.java doc1 "The quick brown fox jumps over the lazy dog"
 */
public class WordCounter implements BiFunction<String, String, Integer> {
  @Override
  public Integer apply(String key, String data) {
    if (data == null || data.trim().isEmpty()) {
      return 0;
    }
    return data.trim().split("\\s+").length;
  }
}
