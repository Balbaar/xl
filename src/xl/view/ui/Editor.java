package xl.view.ui;

import xl.controller.SheetController;
import xl.model.Cell;
import xl.model.Sheet;
import xl.view.logic.SelectionModel;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class Editor extends JTextField implements Observer {

    private final SheetController controller;
    private final SelectionModel selectionModel;

    public Editor(SheetController controller, SelectionModel selectionModel) {
        setBackground(Color.WHITE);
        this.controller = controller;
        this.selectionModel = selectionModel;

        // Observe selection changes
        selectionModel.addObserver(this);

        // Keylisten enter
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    updateCellExpression();
                }
            }
        });
    }

    private void updateCellExpression() {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            controller.updateCell(selectedCell, getText()); //Controller handles logic
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String[]) {
            String[] cells = (String[]) arg;
            String selectedCell = cells[1];
            setText(controller.getCellExpression(selectedCell));
        }
    }


}