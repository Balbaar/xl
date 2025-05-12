package xl.view.ui;

import xl.model.Sheet;
import xl.view.logic.SelectionModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SlotLabels extends GridPanel {
    private List<SlotLabel> labelList;

    public SlotLabels(int rows, int cols, Sheet sheet, SelectionModel selectionModel) {
        super(rows + 1, cols);
        labelList = new ArrayList<>(rows * cols);

        // Add the column labels
        for (char ch = 'A'; ch < 'A' + cols; ch++) {
            add(new ColoredLabel(Character.toString(ch), Color.LIGHT_GRAY, SwingConstants.CENTER));
        }

        for (int row = 1; row <= rows; row++) {
            for (char ch = 'A'; ch < 'A' + cols; ch++) {
                String cellName = ch + String.valueOf(row);
                SlotLabel label = new SlotLabel(cellName, sheet, selectionModel);
                add(label);
                selectionModel.addObserver(label);

                labelList.add(label);
            }
        }
    }
}