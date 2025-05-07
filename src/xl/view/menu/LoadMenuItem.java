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

            System.out.println(cellData);

            // Clear existing cells first
            //xl.getCellController().clear(); // Add this method to CellController

            // Add all the cells, onlt its name
            for (Map.Entry<String, Cell> entry : cellData.entrySet()) {
                System.out.println(entry);
                String cellName = entry.getKey();
                xl.getCellController().createCell(cellName); // create a new cell
            }

            // Set the expression for each cell
            for (Map.Entry<String, Cell> entry : cellData.entrySet()) {
                System.out.println(entry.getValue());
                String cellName = entry.getKey();
                String expression = entry.getValue().getExpression();
                xl.getCellController().setCellExpression(cellName, expression); // Set the expression
            }

            //xl.getCellController().notifyAllCells();


            statusLabel.setText("File loaded successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }

    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(xl);
    }
}
