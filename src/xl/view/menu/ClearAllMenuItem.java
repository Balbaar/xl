package xl.view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import xl.model.Sheet;

class ClearAllMenuItem extends JMenuItem implements ActionListener {

    private final Sheet sheet;

    public ClearAllMenuItem(Sheet sheet) {
        super("Clear all");
        this.sheet = sheet;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sheet.clear();
    }
}