package xl;

import java.util.Map;

public interface CellStrategy {
    void process(Cell cell, Map<String, Cell> cells);
}