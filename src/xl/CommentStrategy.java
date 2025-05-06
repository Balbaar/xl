package xl;

import java.util.Map;

public class CommentStrategy implements CellStrategy {
    @Override
    public void process(Cell cell, Map<String, Cell> cells) {
        cell.setValue(0.0); // Comments have no value
    }
}