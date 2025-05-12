package xl.view.ui;

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

    private final Sheet sheet;
    private final SelectionModel selectionModel;

    public Editor(Sheet sheet, SelectionModel selectionModel) {
        setBackground(Color.WHITE);
        this.sheet = sheet;
        this.selectionModel = selectionModel;

        // Observe selection changes
        selectionModel.addObserver(this);

        // Add a KeyListener to detect ENTER key press
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Enter pressed in editor");
                    updateCellExpression();
                }
            }
        });
    }

    private void updateCellExpression() {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            if(!sheet.cellExists(selectedCell)) {
                // Create a new cell if it doesn't exist
                sheet.createCell(selectedCell);
            }
            // Update the cell expression in the controller
            sheet.setCellExpression(selectedCell, getText());
            System.out.println("Updated cell " + selectedCell + " with expression: " + getText());

            selectionModel.setSelectedCell(selectedCell);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String[]) {
            String[] cells = (String[]) arg;
            String selectedCell = cells[1]; // The new selected cell
            Cell cell = sheet.getCell(selectedCell);
            setText(sheet.getCellExpression(selectedCell));
        }
    }


}