package xl.controller;

import xl.model.Cell;
import xl.model.CellObserver;

import java.util.*;

public class CellController {
    private Map<String, Cell> cells = new HashMap<>();
    private List<CellObserver> observers = new ArrayList<>();

    public Cell getCell(String name) {
        return cells.get(name);
        //return cells.computeIfAbsent(name, Cell::new);
    }

    public void setCellExpression(String name, String expression) {
        Cell cell = getCell(name);

        // Remove this cell as an observer of its previous dependencies
        cell.deleteObservers();

        // Parse the expression and register dependencies
        cell.setExpression(expression, cells);

        // Register this cell as an observer of its dependencies
        registerDependencies(cell, expression);


        updateDependentCells(name);


        // If the expression is empty, remove the cell
        if(expression.isEmpty()) {
            cells.remove(name);
        }
        notifyObservers(name);
        System.out.println(cells);

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

            // Add dependents to the queue
            for (Observer observer : cell.getObservers()) {
                if (observer instanceof Cell) {
                    toProcess.add(((Cell) observer).getName());
                }
            }
            processed.add(currentCell);

            // Notify observers with the changed cell
            cell.notifyObservers(changedCell);
            notifyAllCells();
        }
    }

    public void createCell(String name) {
        if (!cellExists(name)) {
            cells.put(name, new Cell(name));
        }
    }


    private void registerDependencies(Cell cell, String expression) {
        for (String dependency : parseDependencies(expression)) {
            Cell dependentCell = getCell(dependency);
            dependentCell.addObserver(cell); // Register the dependency
        }
    }

    private List<String> parseDependencies(String expression) {
        // Extract cell references (e.g., A1, B2) from the expression
        List<String> dependencies = new ArrayList<>();
        String[] tokens = expression.split("[^A-Za-z0-9]");
        for (String token : tokens) {
            if (token.matches("[A-Z][0-9]+")) {
                dependencies.add(token);
            }
        }
        System.out.println("Dependencies for expression '" + expression + "': " + dependencies);
        return dependencies;
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
        for (String cellName : cells.keySet()) {
            Cell cell = cells.get(cellName);
            cell.setExpression("", cells);
        }

        notifyAllCells();
        cells.clear();
    }

    public void notifyAllCells() {
        for (String cellName : cells.keySet()) {
            notifyObservers(cellName);
        }
    }
}