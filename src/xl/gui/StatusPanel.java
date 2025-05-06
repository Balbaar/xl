package xl.gui;

import xl.CellController;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.WEST;

public class StatusPanel extends BorderPanel {

    protected StatusPanel(StatusLabel statusLabel, CellController cellController, SelectionModel selectionModel) {
        CurrentLabel currentLabel = new CurrentLabel();

        selectionModel.addObserver(currentLabel);

        add(WEST, currentLabel);
        add(CENTER, statusLabel);
    }
}
