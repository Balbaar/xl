package xl.controller;

import xl.model.Cell;
import xl.model.CellObserver;

import java.util.*;

public class CellController {
    private Map<String, Cell> cells = new HashMap<>();
    private List<CellObserver> observers = new ArrayList<>();

    public Cell getCell(String name) {
        return cells.computeIfAbsent(name, Cell::new);
    }

    public void setCellExpression(String name, String expression) {
        Cell cell = getCell(name);
        String oldExpr = cell.getExpression();

        //If the expression is empty remove the cell
        if (expression.isEmpty()) {
            for (String dep : cell.getDependencies()) {
                Cell depCell = getCell(dep);
                depCell.removeDependent(name);
            }
            cells.remove(name);

        }

        if (!expression.equals(oldExpr)) {
            // Remove this cell from the dependents of its old dependencies
            for (String oldDep : cell.getDependencies()) {
                Cell depCell = getCell(oldDep);
                depCell.removeDependent(name);
            }

            // Set new expression and collect new dependencies
            cell.setExpression(expression, cells);

            // Add this cell to the dependents of its new dependencies
            for (String newDep : cell.getDependencies()) {
                Cell depCell = getCell(newDep);
                depCell.addDependent(name);
            }

            // Notify dependents of this cell
            updateDependentCells(name);
        }
    }

    private void updateDependentCells(String changedCell) {
        Queue<String> toProcess = new LinkedList<>();
        Set<String> processed = new HashSet<>();

        toProcess.add(changedCell);

        while (!toProcess.isEmpty()) {
            String currentCell = toProcess.poll();
            if (processed.contains(currentCell)) {
                continue;
            }

            Cell cell = getCell(currentCell);
            if (cell == null) {
                continue;
            }

            // Update the cell's value
            cell.setExpression(cell.getExpression(), cells);

            // Notify observers
            notifyObservers(currentCell);

            // Add dependents to the queue
            toProcess.addAll(cell.getDependents());
            processed.add(currentCell);
        }
    }

    public double getCellValue(String name) {
        return getCell(name).getValue();
    }

    public boolean cellExists(String name) {
        return cells.containsKey(name);
    }

    public Map<String, Cell> getCells() {
        return cells;
    }

    public String getCellExpression(String name) {
        if (!cellExists(name)) {
            return "";
        }

        return getCell(name).getExpression();
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
        for(String cellName : cells.keySet()) {
            Cell cell = cells.get(cellName);
            cell.setExpression("", cells);
        }

        notifyAllCells();
        cells.clear();
        //observers.clear();
    }

    private void notifyAllCells() {
        for (String cellName : cells.keySet()) {
            notifyObservers(cellName);
        }
    }
}