package xl.view.menu;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;

import xl.model.Cell;
import xl.view.ui.StatusLabel;
import xl.view.XL;
import xl.io.XLBufferedReader;

class LoadMenuItem extends OpenMenuItem {

    public LoadMenuItem(XL xl, StatusLabel statusLabel) {
        super(xl, statusLabel, "Load");
    }

    protected void action(String path) throws FileNotFoundException {
        try {
            XLBufferedReader reader = new XLBufferedReader(path);
            Map<String, Cell> cellData = new HashMap<>();
            reader.load(cellData); // Load data into the map

            // Clear existing cells first
            xl.getCellController().clear(); // Add this method to CellController

            // Load all cells
            for (Map.Entry<String, Cell> entry : cellData.entrySet()) {
                xl.getCellController().setCellExpression(entry.getKey(), entry.getValue().getExpression());
            }
            statusLabel.setText("File loaded successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }

    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(xl);
    }
}
