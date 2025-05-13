package xl.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import xl.controller.SheetController;

class ClearMenuItem extends JMenuItem implements ActionListener {

    private final SheetController controller;

    public ClearMenuItem(SheetController controller) {
        super("Clear");
        this.controller = controller;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.clearSelectedCell();
    }
}
