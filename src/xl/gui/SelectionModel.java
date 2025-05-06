package xl.gui;

import java.util.Observable;

public class SelectionModel extends Observable {
    private String selectedCell;

    public String getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(String cell) {
        if (!cell.equals(selectedCell)) {
            this.selectedCell = cell;
            setChanged();
            notifyObservers(cell);
            System.out.println("Selected cell: " + cell);
        }
    }
}