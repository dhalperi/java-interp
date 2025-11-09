package com.example.coderunner;

import java.util.LinkedHashMap;
import java.util.Map;

/** Predefined examples demonstrating the CodeRunner capabilities. */
public class Examples {

  /** Represents a runnable example with code, inputs, and description. */
  public static class Example {
    public final String description;
    public final String code;
    public final Object key;
    public final Object keyData;

    public Example(String description, String code, Object key, Object keyData) {
      this.description = description;
      this.code = code;
      this.key = key;
      this.keyData = keyData;
    }
  }

  /** Returns a map of example name to Example object. */
  public static Map<String, Example> getExamples() {
    Map<String, Example> examples = new LinkedHashMap<>();

    examples.put("simple", new Example(
        "Simple string concatenation",
        "import java.util.function.BiFunction;\n\n"
            + "public class SimpleFunction implements BiFunction<String, String, String> {\n"
            + "  @Override\n"
            + "  public String apply(String key, String data) {\n"
            + "    return key + \": \" + data;\n"
            + "  }\n"
            + "}",
        "user123",
        "Hello, World!"));

    examples.put("math", new Example(
        "Mathematical operations on integers",
        "import java.util.function.BiFunction;\n\n"
            + "public class MathFunction implements BiFunction<Integer, Integer, Integer> {\n"
            + "  @Override\n"
            + "  public Integer apply(Integer key, Integer data) {\n"
            + "    return key * data + 100;\n"
            + "  }\n"
            + "}",
        5,
        10));

    examples.put("list", new Example(
        "Process a list using standard library",
        "import java.util.function.BiFunction;\n"
            + "import java.util.List;\n"
            + "import java.util.stream.Collectors;\n\n"
            + "public class ListFunction implements BiFunction<String, List<String>, String> {\n"
            + "  @Override\n"
            + "  public String apply(String key, List<String> data) {\n"
            + "    return key + \": \" + data.stream()\n"
            + "        .map(String::toUpperCase)\n"
            + "        .collect(Collectors.joining(\", \"));\n"
            + "  }\n"
            + "}",
        "items",
        java.util.Arrays.asList("apple", "banana", "cherry")));

    examples.put("guava", new Example(
        "Use Guava library for collection operations",
        "import java.util.function.BiFunction;\n"
            + "import java.util.List;\n"
            + "import com.google.common.collect.ImmutableList;\n"
            + "import com.google.common.base.Joiner;\n\n"
            + "public class GuavaFunction implements BiFunction<String, List<String>, String> {\n"
            + "  @Override\n"
            + "  public String apply(String key, List<String> data) {\n"
            + "    ImmutableList<String> immutable = ImmutableList.copyOf(data);\n"
            + "    return key + \" -> [\" + Joiner.on(\" | \").join(immutable) + \"]\";\n"
            + "  }\n"
            + "}",
        "processed",
        java.util.Arrays.asList("first", "second", "third")));

    examples.put("filter", new Example(
        "Filter and transform data",
        "import java.util.function.BiFunction;\n"
            + "import java.util.List;\n"
            + "import java.util.stream.Collectors;\n\n"
            + "public class FilterFunction implements BiFunction<Integer, List<Integer>, List<Integer>> {\n"
            + "  @Override\n"
            + "  public List<Integer> apply(Integer threshold, List<Integer> data) {\n"
            + "    return data.stream()\n"
            + "        .filter(x -> x > threshold)\n"
            + "        .map(x -> x * 2)\n"
            + "        .collect(Collectors.toList());\n"
            + "  }\n"
            + "}",
        5,
        java.util.Arrays.asList(1, 3, 5, 7, 9, 11)));

    examples.put("map", new Example(
        "Process map data structures",
        "import java.util.function.BiFunction;\n"
            + "import java.util.Map;\n"
            + "import java.util.stream.Collectors;\n\n"
            + "public class MapFunction implements BiFunction<String, Map<String, Integer>, Integer> {\n"
            + "  @Override\n"
            + "  public Integer apply(String key, Map<String, Integer> data) {\n"
            + "    return data.values().stream()\n"
            + "        .mapToInt(Integer::intValue)\n"
            + "        .sum();\n"
            + "  }\n"
            + "}",
        "sum",
        java.util.Map.of("a", 10, "b", 20, "c", 30)));

    return examples;
  }
}
