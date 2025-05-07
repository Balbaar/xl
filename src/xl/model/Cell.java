package xl.model;

import xl.model.CellStrategy;
import xl.model.CommentStrategy;
import xl.model.ExpressionStrategy;

import java.util.*;

public class Cell extends Observable implements Observer {
    private String name;
    private String expression = "";
    private double value = 0.0;
    private CellStrategy strategy;
    private Map<String, Cell> cells;

    public Cell(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        System.out.println("Setting value of " + name + " to " + value);
        setChanged();
        notifyObservers(); // Notify dependents when value changes
    }

    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;
        this.cells = cells;

        if (expression.isEmpty()) {
            return;
        }

        // Clear existing observers
        deleteObservers();

        for (String dependency : parseDependencies(expression)) {
            Cell dependentCell = cells.get(dependency);
            if (dependentCell != null) {
                dependentCell.addObserver(this); // Register this cell as an observer
            }
        }

        // Choose strategy based on content
        if (expression.startsWith("#")) {
            strategy = new CommentStrategy();
        } else {
            strategy = new ExpressionStrategy();
        }

        // Process using the selected strategy
        strategy.process(this, cells);

        // Notify dependents about the change
        setChanged();
        notifyObservers();
    }

    private List<String> parseDependencies(String expression) {
        List<String> dependencies = new ArrayList<>();
        String[] tokens = expression.split("[^A-Za-z0-9]");
        for (String token : tokens) {
            if (token.matches("[A-Z][0-9]+")) {
                dependencies.add(token);
            }
        }
        return dependencies;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Cell " + name + " updated by " + o);
        if (strategy != null) {
            strategy.process(this, cells); // ✅ Just re-evaluate using the strategy
            setChanged();
            notifyObservers(); // Notify cells that depend on this one
        }
    }

}