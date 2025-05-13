package xl.model;

import xl.util.XLException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cell extends Observable implements Observer {
    private String name;
    private String expression = "";
    private double value = 0.0;
    private CellStrategy strategy;
    private final List<Cell> dependencies = new ArrayList<>();
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
        setChanged();
        notifyObservers();
    }

    public void setExpression(String expression, Map<String, Cell> cells) throws XLException {
        String oldExpression = this.expression;
        double oldValue = this.value;
        List<Cell> oldDependencies = new ArrayList<>(this.dependencies);

        this.expression = expression;
        this.cells = cells;
        clearDependencies();

        if (expression.isEmpty()) return;

        // Parse and register dependencies
        List<String> dependencyNames = parseDependencies(expression);
        for (String dependencyName : dependencyNames) {
            Cell dependency = cells.computeIfAbsent(dependencyName, Cell::new);
            dependency.addObserver(this);
            dependencies.add(dependency);
        }

        // Check for circular dependencies
        new BombStrategy().process(this, cells);  // Can bubble up exception

        // Choose strategy based on input
        if (expression.startsWith("#")) {
            strategy = new CommentStrategy();
        } else {
            strategy = new ExpressionStrategy();
        }

        // Evaluate expression
        strategy.process(this, cells); // Can bubble up exception

        setChanged();
        notifyObservers();
    }





    private void clearDependencies() {
        for (Cell dependency : dependencies) {
            dependency.deleteObserver(this); // Remove as an observer
        }
        dependencies.clear();
    }

    public static List<String> parseDependencies(String expression) {
        List<String> dependencies = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][0-9]+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            dependencies.add(matcher.group());
        }
        return dependencies;
    }

    public List<Cell> getDependencies() {
        return dependencies;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Cell " + name + " updated by " + o);
        if (strategy != null) {
            strategy.process(this, cells);
        }
        setChanged();
        notifyObservers();
    }
}