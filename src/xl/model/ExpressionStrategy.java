package xl.model;

import xl.expr.Environment;
import xl.expr.Expr;
import xl.expr.ExprParser;
import xl.util.XLException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExpressionStrategy implements CellStrategy {
    @Override
    public void process(Cell cell, Map<String, Cell> cells) {
        cell.getDependencies().clear();
        String expression = cell.getExpression();

        try {
            // Parse the expression
            ExprParser parser = new ExprParser();
            Expr expr = parser.build(expression);

            // Create a special environment to collect dependencies
            Set<String> dependencies = new HashSet<>();
            Environment depCollector = new Environment() {
                @Override
                public double value(String name) {
                    dependencies.add(name);
                    return cells.getOrDefault(name, new Cell(name)).getValue();
                }
            };

            // Calculate value and collect dependencies in one pass
            double value = expr.value(depCollector);

            // Update cell's dependencies and value
            cell.getDependencies().addAll(dependencies);
            cell.setValue(value);

        } catch (IOException | XLException e) {
            cell.setValue(0.0);
        }
    }
}