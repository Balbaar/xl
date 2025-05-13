package xl.view.menu;

import java.io.FileNotFoundException;
import javax.swing.JFileChooser;

import xl.view.XL;
import xl.view.ui.StatusLabel;

class LoadMenuItem extends OpenMenuItem {

    public LoadMenuItem(XL xl, StatusLabel statusLabel) {
        super(xl, statusLabel, "Load");
    }

    @Override
    protected void action(String path) throws FileNotFoundException {
        xl.getSheetController().loadFile(path);
    }

    @Override
    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(xl);
    }
}
