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

        // Only update if expression has changed
        if (!expression.equals(oldExpr)) {
            // Clear old dependencies first
            for (Map.Entry<String, Cell> entry : cells.entrySet()) {
                entry.getValue().getDependencies().remove(name);
            }

            // Set new expression which will trigger the appropriate strategy
            cell.setExpression(expression, cells);

            // Notify observers for this cell
            notifyObservers(name);

            // Update all cells that depend on this cell
            updateDependentCells(name);
        }
    }

    public double getCellValue(String name) {
        return getCell(name).getValue();
    }

    public String getCellExpression(String name) {
        return getCell(name).getExpression();
    }

    private void updateDependentCells(String changedCell) {
        Set<String> processed = new HashSet<>();
        Queue<String> toProcess = new LinkedList<>();
        toProcess.add(changedCell);

        while (!toProcess.isEmpty()) {
            String currentCell = toProcess.poll();
            if (processed.contains(currentCell)) {
                continue;
            }

            // Find all cells that depend on the current cell
            for (Map.Entry<String, Cell> entry : cells.entrySet()) {
                String cellName = entry.getKey();
                Cell cell = entry.getValue();

                if (cell.getDependencies().contains(currentCell)) {
                    // Reprocess the cell using its strategy
                    cell.setExpression(cell.getExpression(), cells);
                    notifyObservers(cellName);
                    toProcess.add(cellName);
                }
            }

            processed.add(currentCell);
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