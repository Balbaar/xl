package xl.view.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import xl.controller.CellController;
import xl.view.logic.SelectionModel;
import xl.view.ui.StatusLabel;
import xl.view.XL;
import xl.view.XLList;

public class XLMenuBar extends JMenuBar {

    public XLMenuBar(XL xl, XLList xlList, StatusLabel statusLabel, CellController cellController, SelectionModel selectionModel) {
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        file.add(new SaveMenuItem(xl, statusLabel));
        file.add(new LoadMenuItem(xl, statusLabel));
        file.add(new NewMenuItem(xl));
        file.add(new CloseMenuItem(xl, xlList));
        edit.add(new ClearMenuItem(cellController, selectionModel));
        edit.add(new ClearAllMenuItem(cellController));
        add(file);
        add(edit);
        add(new WindowMenu(xlList));
    }
}
