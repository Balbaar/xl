package xl.view.ui;

import xl.model.Cell;
import xl.model.Sheet;
import xl.view.logic.SelectionModel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

public class SlotLabel extends ColoredLabel implements Observer {
    private final String cellName;
    private final Sheet sheet;
    private final SelectionModel selectionModel;
    private static final int WIDTH = 100;
    private static final int HEIGHT = 25;

    public SlotLabel(String cellName, Sheet sheet, SelectionModel selectionModel) {
        super("", Color.WHITE, RIGHT);
        this.cellName = cellName;
        this.sheet = sheet;
        this.selectionModel = selectionModel;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));

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
            setText(sheet.getCellExpression(cellName));
        } else {
            setBackground(Color.WHITE);
            String expression = sheet.getCellExpression(cellName);
            if (expression.isEmpty()) {
                setText("");
            } else if (expression.startsWith("#")) {
                setText(expression);
            } else {
                setText(String.valueOf(sheet.getCellValue(cellName)));
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
    }
}