package xl.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import xl.gui.SelectionModel;
import xl.CellController;

import javax.swing.*;

class ClearMenuItem extends JMenuItem implements ActionListener {

    private final CellController cellController;
    private final SelectionModel selectionModel;

    public ClearMenuItem(CellController cellController, SelectionModel selectionModel) {
        super("Clear");
        this.cellController = cellController;
        this.selectionModel = selectionModel;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            cellController.setCellExpression(selectedCell, ""); // Clear the cell's expression
        }
    }
}