package xl.view.ui;

import xl.controller.CellController;
import xl.model.CellObserver;
import xl.view.logic.SelectionModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class SlotLabel extends ColoredLabel implements Observer {
    private final String cellName;
    private final CellController cellController;
    private final SelectionModel selectionModel;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 25;

    public SlotLabel(String cellName, CellController cellController, SelectionModel selectionModel) {
        super("", Color.WHITE, RIGHT);
        this.cellName = cellName;
        this.cellController = cellController;
        this.selectionModel = selectionModel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

        cellController.addObserver(new CellObserver() {
            @Override
            public void cellUpdated(String updatedCellName) {
                if (cellName.equals(updatedCellName)) {
                    updateDisplay();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectionModel.setSelectedCell(cellName);
            }
        });

        updateDisplay();
    }

    private void updateDisplay() {
        boolean isSelected = cellName.equals(selectionModel.getSelectedCell());
        if (isSelected) {
            setBackground(Color.YELLOW);
            setText(cellController.getCellExpression(cellName));
        } else {
            setBackground(Color.WHITE);
            String expression = cellController.getCellExpression(cellName);
            if (expression.isEmpty()) {
                setText("");
            } else if (expression.startsWith("#")) {
                setText(expression);
            } else {
                setText(String.valueOf(cellController.getCellValue(cellName)));
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SelectionModel) {
            updateDisplay();
        }
    }
}