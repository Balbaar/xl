package xl.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import xl.view.logic.SelectionModel;
import xl.model.Sheet;

import javax.swing.*;

class ClearMenuItem extends JMenuItem implements ActionListener {

    private final Sheet sheet;
    private final SelectionModel selectionModel;

    public ClearMenuItem(Sheet sheet, SelectionModel selectionModel) {
        super("Clear");
        this.sheet = sheet;
        this.selectionModel = selectionModel;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            sheet.setCellExpression(selectedCell, ""); // Clear the cell's expression
        }
    }
}