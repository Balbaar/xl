package xl.model;

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

    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;
        this.cells = cells;

        // Clear old dependencies
        clearDependencies();

        if (expression.isEmpty()) {
            return;
        }

        // Parse dependencies and register them
        List<String> dependencyNames = parseDependencies(expression);
        for (String dependencyName : dependencyNames) {
            Cell dependency = cells.computeIfAbsent(dependencyName, Cell::new);
            dependency.addObserver(this); // Register as an observer
            dependencies.add(dependency);
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

    private void clearDependencies() {
        for (Cell dependency : dependencies) {
            dependency.deleteObserver(this); // Remove as an observer
        }
        dependencies.clear();
    }

    private List<String> parseDependencies(String expression) {
        List<String> dependencies = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Z][0-9]+");
        Matcher matcher = pattern.matcher(expression);
        while (matcher.find()) {
            dependencies.add(matcher.group());
        }
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