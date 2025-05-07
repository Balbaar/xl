package xl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Cell extends Observable implements Observer {
    private String name;
    private String expression = "";
    private double value = 0.0;
    private CellStrategy strategy;
    private final List<Observer> customObservers = new ArrayList<>();

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
        notifyObservers(); // Notify dependents when value changes
    }

    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;

        if (expression.isEmpty()) {
            return;
        }

        // Remove this cell as an observer of previous dependencies
        //deleteObservers();
        //customObservers.clear();

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

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        customObservers.add(o);
        System.out.println("Observer added to cell " + name + ": " + o);
    }

    @Override
    public void deleteObserver(Observer o) {
        super.deleteObserver(o);
        customObservers.remove(o);
    }

    public List<Observer> getObservers() {
        return new ArrayList<>(customObservers);
    }

    @Override
    public void update(Observable o, Object arg) {
        // React to updates from cells this cell depends on
        setExpression(expression, (Map<String, Cell>) arg);
        setChanged();
        notifyObservers(); // Notify dependents
        System.out.println("Cell " + name + " updated due to dependency change.");
    }
}