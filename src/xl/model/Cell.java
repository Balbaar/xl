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

    public void setExpression(String expression, Map<String, Cell> cells) {
        String oldExpression = this.expression;
        double oldValue = this.value;
        List<Cell> oldDependencies = new ArrayList<>(this.dependencies);

        this.expression = expression;
        this.cells = cells;
        clearDependencies();

        if (expression.isEmpty()) {
            return;
        }

        try {
            // Parse and register dependencies
            List<String> dependencyNames = parseDependencies(expression);
            for (String dependencyName : dependencyNames) {
                Cell dependency = cells.computeIfAbsent(dependencyName, Cell::new);
                dependency.addObserver(this);
                dependencies.add(dependency);
            }

            // Check for circular dependencies
            new Bomb().process(this, cells);

            //Choose strategy based on input
            if (expression.startsWith("#")) {
                strategy = new CommentStrategy();
            } else {
                strategy = new ExpressionStrategy();
            }

            strategy.process(this, cells);
            setChanged();
            notifyObservers();

        } catch (XLException e) {
            // Rollback
            this.expression = oldExpression;
            this.value = oldValue;
            clearDependencies();
            this.dependencies.addAll(oldDependencies);

            for (Cell dep : oldDependencies) {
                dep.addObserver(this);
            }

            setChanged();
            notifyObservers();
            System.err.println("Failed to set expression for " + name + ": " + e.getMessage());
        }
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