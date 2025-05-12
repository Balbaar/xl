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
        String expression = cell.getExpression();

        try {
            // Parse the expression
            ExprParser parser = new ExprParser();
            Expr expr = parser.build(expression);

            // Create a special environment
            Environment env = new Environment() {
                @Override
                public double value(String name) {
                    Cell dependentCell = cells.get(name);
                    if (dependentCell != null) {
                        return dependentCell.getValue();
                    } else {
                        throw new XLException("Cell " + name + " does not exist.");
                    }
                }
            };
            // Evaluate the expression
            double value = expr.value(env);
            cell.setValue(value);


    } catch (IOException e) {
            throw new XLException("Error parsing expression: " + expression);
        }
    }
}