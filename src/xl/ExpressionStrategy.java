package xl;

import xl.expr.Environment;
import xl.expr.Expr;
import xl.expr.ExprParser;
import xl.util.XLException;

import java.io.IOException;
import java.util.Map;

public class ExpressionStrategy implements CellStrategy {
    @Override
    public void process(Cell cell, Map<String, Cell> cells) {
        cell.getDependencies().clear();
        String expression = cell.getExpression();

        try {
            // Parse the expression
            ExprParser parser = new ExprParser();
            Expr expr = parser.build(expression);


            // Collect dependencies
            expr.toString().chars()
                    .filter(Character::isLetterOrDigit)
                    .mapToObj(c -> String.valueOf((char) c))
                    .filter(name -> name.matches("[A-Z][0-9]+"))
                    .forEach(cell.getDependencies()::add);



            // Calculate the value using the environment
            double value = expr.value(new Environment() {
                @Override
                public double value(String name) {
                    return cells.getOrDefault(name, new Cell(name)).getValue();
                }
            });

            cell.setValue(value);
        } catch (IOException | XLException e) {
            cell.setValue(0.0); // Default value for invalid expressions
        }
    }
}