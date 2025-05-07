package xl.model;

import java.util.Map;

public interface CellStrategy {
    void process(Cell cell, Map<String, Cell> cells);
}