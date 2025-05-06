package xl.gui;

import xl.Cell;
import xl.CellController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

public class Editor extends JTextField implements Observer {

    private final CellController cellController;
    private final SelectionModel selectionModel;

    public Editor(CellController cellController, SelectionModel selectionModel) {
        setBackground(Color.WHITE);
        this.cellController = cellController;
        this.selectionModel = selectionModel;

        // Observe selection changes
        selectionModel.addObserver(this);

        // Listen for text changes in the editor
        getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateCellExpression();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateCellExpression();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateCellExpression();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        // Update the editor's text when the selected cell changes
        String selectedCell = (String) arg;
        Cell cell = cellController.getCell(selectedCell);
        setText(cellController.getCellExpression(selectedCell));
    }

    private void updateCellExpression() {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            cellController.setCellExpression(selectedCell, getText());
            System.out.println("Updated cell " + selectedCell + " with expression: " + getText());
        }
    }
}