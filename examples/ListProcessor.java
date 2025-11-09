import java.util.function.BiFunction;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import com.google.common.base.Joiner;

/**
 * Example: Processes comma-separated values using Guava.
 *
 * Run with:
 *   bazel run //:cli -- file examples/ListProcessor.java mylist "apple,banana,cherry,date"
 */
public class ListProcessor implements BiFunction<String, String, String> {
  @Override
  public String apply(String key, String data) {
    List<String> items = Arrays.stream(data.split(","))
        .map(String::trim)
        .map(String::toUpperCase)
        .sorted()
        .collect(Collectors.toList());

    return key + " processed " + items.size() + " items: "
        + Joiner.on(" -> ").join(items);
  }
}
