package xl.view.menu;

import java.io.FileNotFoundException;
import java.util.Map;
import javax.swing.JFileChooser;

import xl.model.Cell;
import xl.view.ui.StatusLabel;
import xl.view.XL;
import xl.io.XLPrintStream;

class SaveMenuItem extends OpenMenuItem {

    public SaveMenuItem(XL xl, StatusLabel statusLabel) {
        super(xl, statusLabel, "Save");
    }

    protected void action(String path) throws FileNotFoundException {
        if (!path.endsWith(".xl")) {
            path += ".xl"; // Ensure the file has the correct extension
        }

        try (XLPrintStream printStream = new XLPrintStream(path)) {
            Map<String, Cell> cells = xl.getCellController().getCells();
            printStream.save(cells.entrySet()); // Save the cells
            statusLabel.setText("File saved successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error saving file: " + e.getMessage());
        }
    }

    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showSaveDialog(xl);
    }
}
