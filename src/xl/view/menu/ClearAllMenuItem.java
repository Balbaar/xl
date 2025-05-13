package xl.view.menu;
import xl.controller.SheetController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;


class ClearAllMenuItem extends JMenuItem implements ActionListener {

    private final SheetController controller;

    public ClearAllMenuItem(SheetController controller) {
        super("Clear all");
        this.controller = controller;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.clearAllCells();
    }
}
