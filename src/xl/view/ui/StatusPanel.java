package xl.view.ui;

import xl.controller.CellController;
import xl.view.logic.SelectionModel;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class StatusPanel extends BorderPanel {

    public StatusPanel(StatusLabel statusLabel, CellController cellController, SelectionModel selectionModel) {
        CurrentLabel currentLabel = new CurrentLabel();

        selectionModel.addObserver(currentLabel);

        add(WEST, currentLabel);
        add(CENTER, statusLabel);
    }
}
