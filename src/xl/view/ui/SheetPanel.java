package xl.view.ui;

import xl.controller.CellController;
import xl.view.logic.SelectionModel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class SheetPanel extends BorderPanel {

    public SheetPanel(int rows, int columns, CellController cellController, SelectionModel selectionModel) {
        add(WEST, new RowLabels(rows));
        add(CENTER, new SlotLabels(rows, columns, cellController, selectionModel));
    }
}
