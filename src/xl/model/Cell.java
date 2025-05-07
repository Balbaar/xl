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
    //!not good to keep a reference to all cells
    //!Kom inte på något anant sätt
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
        notifyObservers(cells); // Notify dependents when value changes
    }

    public void setExpression(String expression, Map<String, Cell> cells) {
        this.expression = expression;
        this.cells = cells;

        if (expression.isEmpty()) {
            return;
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
        notifyObservers(cells);
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
        if (arg instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Cell> cells = (Map<String, Cell>) arg;
            setExpression(expression, cells);
            setChanged();
            notifyObservers(cells); // Pass the cells map to dependents
            System.out.println("Cell " + name + " updated due to dependency change.");
        } else {
            throw new IllegalArgumentException("Expected a Map<String, Cell> as argument.");
        }
    }
}