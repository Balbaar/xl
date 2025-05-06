package xl;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Cell {
    private String name;
    private String expression = "";
    private double value = 0.0;
    private Set<String> dependencies = new HashSet<>();

    public Cell(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public double getValue() {
        return value;
    }

    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;
        dependencies.clear();
        // Parse the expression and update dependencies
        // (Assume a simple parser for demonstration)
        if (expression.matches("[A-Z][0-9]+")) {
            dependencies.add(expression);
        }
        recalculateValue(cells);
    }

    public void recalculateValue(Map<String, Cell> cells) {
        if (dependencies.isEmpty()) {
            try {
                value = Double.parseDouble(expression);
            } catch (NumberFormatException e) {
                value = 0.0; // Default value for invalid expressions
            }
        } else {
            value = dependencies.stream()
                    .mapToDouble(dep -> cells.getOrDefault(dep, new Cell(dep)).getValue())
                    .sum(); // Example: sum of dependent cell values
        }
    }

    public Set<String> getDependencies() {
        return dependencies;
    }
}