package xl.view.ui;

import xl.model.Sheet;
import xl.view.logic.SelectionModel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class StatusPanel extends BorderPanel {

    public StatusPanel(StatusLabel statusLabel, Sheet sheet, SelectionModel selectionModel) {
        CurrentLabel currentLabel = new CurrentLabel();

        selectionModel.addObserver(currentLabel);

        add(WEST, currentLabel);
        add(CENTER, statusLabel);
    }
}
