package xl.model;

import java.util.*;

public class Sheet {
    private final Map<String, Cell> cells = new HashMap<>();
    private final List<CellObserver> observers = new ArrayList<>();

    public Cell getCell(String name) {
        return cells.get(name);
    }

    public void setCellExpression(String name, String expression) {
        Cell cell = cells.computeIfAbsent(name, Cell::new);

        // Update the cell's expression
        cell.setExpression(expression, cells);

        // If the expression is empty, remove the cell
        if (expression.isEmpty()) {
            cells.remove(name);
        }

        // Notify observers about the change
        notifyObservers(name);
    }

    public void createCell(String name) {
        cells.putIfAbsent(name, new Cell(name));
    }

    public double getCellValue(String name) {
        Cell cell = getCell(name);
        return cell != null ? cell.getValue() : 0.0;
    }

    public boolean cellExists(String name) {
        return cells.containsKey(name);
    }

    public Map<String, Cell> getCells() {
        return cells;
    }

    public String getCellExpression(String name) {
        Cell cell = getCell(name);
        return cell != null ? cell.getExpression() : "";
    }

    public void addObserver(CellObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String cellName) {
        for (CellObserver observer : observers) {
            observer.cellUpdated(cellName);
        }
    }

    public void clear() {
        for (Cell cell : cells.values()) {
            cell.setExpression("", cells);
        }
        cells.clear();
        notifyAllCells();
    }

    public void notifyAllCells() {
        for (String cellName : cells.keySet()) {
            notifyObservers(cellName);
        }
    }
}