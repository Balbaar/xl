package xl.gui;

import xl.CellController;
import xl.CellObserver;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class SlotLabel extends ColoredLabel implements Observer {
    private final String cellName;
    private final CellController cellController;

    public SlotLabel(String cellName, CellController cellController, SelectionModel selectionModel) {
        super("                    ", Color.WHITE, RIGHT);
        this.cellName = cellName;
        this.cellController = cellController;

        cellController.addObserver(new CellObserver() {
            @Override
            public void cellUpdated(String updatedCellName) {
                if (cellName.equals(updatedCellName)) {
                    setText(String.valueOf(cellController.getCellExpression(cellName)));
                    //setText(String.valueOf(cellController.getCellValue(cellName)));
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cellController.setCellExpression(cellName, ""); // Example: Clear expression on click
                selectionModel.setSelectedCell(cellName);
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SelectionModel) {
            String selectedCell = (String) arg;
            if (cellName.equals(selectedCell)) {
                setBackground(Color.YELLOW); // Highlight selected cell
            } else {
                setBackground(Color.WHITE); // Reset background for non-selected cells
            }
        }
    }
}