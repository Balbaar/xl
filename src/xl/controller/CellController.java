package xl.controller;

import xl.model.Cell;
import xl.model.CellObserver;

import java.util.*;

public class CellController {
    private Map<String, Cell> cells = new HashMap<>();
    private List<CellObserver> observers = new ArrayList<>();

    public Cell getCell(String name) {
        return cells.get(name);
    }

    public void setCellExpression(String name, String expression) {
        // Create the cell if it doesn't exist
        Cell cell = cells.computeIfAbsent(name, Cell::new);

        // Set the expression, which will handle dependencies and notify dependents
        cell.setExpression(expression, cells);
    }

    public void createCell(String name) {
        cells.putIfAbsent(name, new Cell(name));
    }

    public boolean cellExists(String name) {
        return cells.containsKey(name);
    }

    public String getCellExpression(String name) {
        Cell cell = cells.get(name);
        return (cell != null) ? cell.getExpression() : "";
    }

    public double getCellValue(String name) {
        Cell cell = cells.get(name);
        return (cell != null) ? cell.getValue() : 0.0;
    }

    public Map<String, Cell> getCells() {
        return cells;
    }


    public void addObserver(CellObserver observer) {
        observers.add(observer);
    }



}