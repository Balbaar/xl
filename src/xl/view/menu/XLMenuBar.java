package xl.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import xl.controller.SheetController;
import xl.model.Sheet;
import xl.view.logic.SelectionModel;
import xl.view.ui.StatusLabel;
import xl.view.XL;
import xl.view.XLList;

public class XLMenuBar extends JMenuBar {

    public XLMenuBar(XL xl, XLList xlList, StatusLabel statusLabel, SheetController sheetController, SelectionModel selectionModel) {
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        file.add(new SaveMenuItem(xl, statusLabel));
        file.add(new LoadMenuItem(xl, statusLabel));
        file.add(new NewMenuItem(xl));
        file.add(new CloseMenuItem(xl, xlList));
        edit.add(new ClearMenuItem(sheetController));
        edit.add(new ClearAllMenuItem(sheetController));
        add(file);
        add(edit);
        add(new WindowMenu(xlList));
    }
}
