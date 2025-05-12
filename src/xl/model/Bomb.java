package xl.model;

import xl.util.XLException;

import java.util.*;

public class Bomb implements CellStrategy {

    @Override
    public void process(Cell cell, Map<String, Cell> cells) {
        if (detectCycle(cell, new HashSet<>(), new HashSet<>())) {
            throw new XLException("Circular dependency detected at cell: " + cell.getName());
        }
    }

    //recursive function to detect circular dependencies
    private boolean detectCycle(Cell current, Set<String> visited, Set<String> recursionStack) {
        String cellName = current.getName();

        if (recursionStack.contains(cellName)) {
            return true; // Cycle detected
        }
        if (visited.contains(cellName)) {
            return false; // no cycle found
        }

        visited.add(cellName);
        recursionStack.add(cellName);

        for (Cell dependency : current.getDependencies()) {
            if (detectCycle(dependency, visited, recursionStack)) {
                return true;
            }
        }

        recursionStack.remove(cellName);
        return false;
    }
}
