package xl.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import xl.controller.CellController;

class ClearAllMenuItem extends JMenuItem implements ActionListener {

    private final CellController cellController;

    public ClearAllMenuItem(CellController cellController) {
        super("Clear all");
        this.cellController = cellController;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //cellController.clear();
    }
}