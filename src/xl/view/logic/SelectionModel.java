package xl.view.logic;

import java.util.Observable;

public class SelectionModel extends Observable {
    private String selectedCell;

    public String getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(String cell) {
        if (!cell.equals(selectedCell)) {
            String oldCell = selectedCell;
            this.selectedCell = cell;
            setChanged();
            notifyObservers(new String[]{oldCell, cell}); // Notify with old and new cells
            System.out.println("Selected cell changed from: " + oldCell + " to: " + cell);
        }
    }

    public void updateSlotLabels() {
        setChanged();
        notifyObservers(new String[]{selectedCell, selectedCell}); // Notify with the same cell
        System.out.println("Slot labels updated for cell: " + selectedCell);
    }
}