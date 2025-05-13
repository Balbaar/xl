package xl.view.menu;

import java.io.FileNotFoundException;
import javax.swing.JFileChooser;

import xl.view.XL;
import xl.view.ui.StatusLabel;

class SaveMenuItem extends OpenMenuItem {

    public SaveMenuItem(XL xl, StatusLabel statusLabel) {
        super(xl, statusLabel, "Save");
    }

    @Override
    protected void action(String path) throws FileNotFoundException {
        xl.getSheetController().saveFile(path);
    }

    @Override
    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showSaveDialog(xl);
    }
}
