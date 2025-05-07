package xl.model;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class Cell {
    private String name;
    private String expression = "";
    private double value = 0.0;
    private Set<String> dependencies = new HashSet<>();
    private Set<String> dependents = new HashSet<>();
    private CellStrategy strategy;

    public Cell(String name) {
        this.name = name;
    }

    public String getExpression() {
        return expression;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public Set<String> getDependents() {
        return dependents;
    }

    public void addDependent(String cellName) {
        dependents.add(cellName);
    }

    public void removeDependent(String cellName) {
        dependents.remove(cellName);
    }
    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;
        //print dependendencies
        System.out.println("Dependencies for " + name + ": " + dependencies);
        //Print dependents
        System.out.println("Dependents for " + name + ": " + dependents);

        // Choose strategy based on content
        if (expression.startsWith("#")) {
            strategy = new CommentStrategy();
        } else {
            strategy = new ExpressionStrategy();
        }

        // Process using the selected strategy
        strategy.process(this, cells);
    }
}