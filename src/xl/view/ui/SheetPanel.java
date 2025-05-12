package xl.view.ui;

import xl.model.Sheet;
import xl.view.logic.SelectionModel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class SheetPanel extends BorderPanel {

    public SheetPanel(int rows, int columns, Sheet sheet, SelectionModel selectionModel) {
        add(WEST, new RowLabels(rows));
        add(CENTER, new SlotLabels(rows, columns, sheet, selectionModel));
    }
}
