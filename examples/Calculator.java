import java.util.function.BiFunction;

/**
 * Example: Performs calculation based on key operation.
 *
 * Run with:
 *   bazel run //:cli -- file examples/Calculator.java "add" "10,20"
 *   bazel run //:cli -- file examples/Calculator.java "multiply" "5,7"
 */
public class Calculator implements BiFunction<String, String, Double> {
  @Override
  public Double apply(String operation, String numbers) {
    String[] parts = numbers.split(",");
    double a = Double.parseDouble(parts[0].trim());
    double b = Double.parseDouble(parts[1].trim());

    switch (operation.toLowerCase()) {
      case "add":
        return a + b;
      case "subtract":
        return a - b;
      case "multiply":
        return a * b;
      case "divide":
        if (b == 0) {
          throw new ArithmeticException("Division by zero");
        }
        return a / b;
      default:
        throw new IllegalArgumentException("Unknown operation: " + operation);
    }
  }
}
