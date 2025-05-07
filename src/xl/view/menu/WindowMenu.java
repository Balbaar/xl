package xl.view.menu;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JMenu;
import xl.view.XL;
import xl.view.XLList;

public class WindowMenu extends JMenu implements Observer {

    private XLList xlList;

    public WindowMenu(XLList xlList) {
        super("Window");
        this.xlList = xlList;
        xlList.addObserver(this);
        update(null, null);
    }

    public void update(Observable observable, Object object) {
        removeAll();
        for (XL xl : xlList) {
            add(new WindowMenuItem(xl));
        }
    }
}
