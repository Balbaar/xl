package xl;

import java.util.*;

public class CellController {
    private Map<String, Cell> cells = new HashMap<>();
    private List<CellObserver> observers = new ArrayList<>();

    public Cell getCell(String name) {
        return cells.computeIfAbsent(name, Cell::new);
    }

    public void setCellExpression(String name, String expression) {
        Cell cell = getCell(name);
        cell.setExpression(expression, cells);
        notifyObservers(name);
        recalculateDependencies(name);
    }

    public double getCellValue(String name) {
        return getCell(name).getValue();
    }

    public String getCellExpression(String name) {
        return getCell(name).getExpression();
    }

    private void recalculateDependencies(String name) {
        for (Map.Entry<String, Cell> entry : cells.entrySet()) {
            if (entry.getValue().getDependencies().contains(name)) {
                entry.getValue().recalculateValue(cells);
                notifyObservers(entry.getKey());
            }
        }
    }

    public void addObserver(CellObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String cellName) {
        for (CellObserver observer : observers) {
            observer.cellUpdated(cellName);
        }
    }
}