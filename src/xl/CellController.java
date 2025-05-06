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
        String oldExpr = cell.getExpression();

        if (!expression.equals(oldExpr)) {
            // Store cells that previously depended on this cell
            Set<String> dependentCells = findCellsThatDependOn(name);

            // Set new expression
            cell.setExpression(expression, cells);

            // Notify that this cell changed
            notifyObservers(name);

            // Update cells that previously depended on this cell
            for (String dependent : dependentCells) {
                Cell depCell = getCell(dependent);
                depCell.setExpression(depCell.getExpression(), cells);
                notifyObservers(dependent);
            }

            // Update new dependent cells
            updateDependentCells(name);
        }
    }

    private Set<String> findCellsThatDependOn(String cellName) {
        Set<String> dependentCells = new HashSet<>();
        for (Map.Entry<String, Cell> entry : cells.entrySet()) {
            if (entry.getValue().getDependencies().contains(cellName)) {
                dependentCells.add(entry.getKey());
            }
        }
        return dependentCells;
    }


    private void updateDependentCells(String changedCell) {
        Set<String> processed = new HashSet<>();
        Queue<String> toProcess = new LinkedList<>();

        // Find all cells that directly depend on the changed cell
        Set<String> dependentCells = findCellsThatDependOn(changedCell);
        toProcess.addAll(dependentCells);

        while (!toProcess.isEmpty()) {
            String currentCell = toProcess.poll();
            if (processed.contains(currentCell)) {
                continue;
            }

            Cell cell = getCell(currentCell);
            cell.setExpression(cell.getExpression(), cells);
            notifyObservers(currentCell);

            // Add cells that depend on the current cell
            Set<String> newDependents = findCellsThatDependOn(currentCell);
            toProcess.addAll(newDependents);

            processed.add(currentCell);
        }
    }

    public double getCellValue(String name) {
        return getCell(name).getValue();
    }

    public String getCellExpression(String name) {
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

        //cells.clear();
        notifyAllCells();
    }

    private void notifyAllCells() {
        for (String cellName : cells.keySet()) {
            notifyObservers(cellName);
        }
    }
}