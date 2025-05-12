package xl.view;

import xl.model.Sheet;
import xl.view.logic.SelectionModel;
import xl.view.logic.XLCounter;
import xl.view.menu.XLMenuBar;
import xl.view.ui.Editor;
import xl.view.ui.SheetPanel;
import xl.view.ui.StatusLabel;
import xl.view.ui.StatusPanel;

import javax.swing.*;

import static java.awt.BorderLayout.*;

public class XL extends JFrame {

    private static final int ROWS = 10, COLUMNS = 8;
    private XLCounter counter;
    private StatusLabel statusLabel = new StatusLabel();
    private XLList xlList;

    private Sheet sheet = new Sheet();
    private SelectionModel selectionModel = new SelectionModel();

    public XL(XL oldXL) {
        this(oldXL.xlList, oldXL.counter);
    }

    public XL(XLList xlList, XLCounter counter) {
        super("Untitled-" + counter);
        this.xlList = xlList;
        this.counter = counter;
        xlList.add(this);
        counter.increment();
        JPanel statusPanel = new StatusPanel(statusLabel, sheet, selectionModel);
        JPanel sheetPanel = new SheetPanel(ROWS, COLUMNS, sheet, selectionModel);
        Editor editor = new Editor(sheet, selectionModel);
        add(NORTH, statusPanel);
        add(CENTER, editor);
        add(SOUTH, sheetPanel);
        setJMenuBar(new XLMenuBar(this, xlList, statusLabel, sheet, selectionModel));
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void rename(String title) {
        setTitle(title);
        xlList.setChanged();
    }

    public static void main(String[] args) {
        new XL(new XLList(), new XLCounter());
    }
}
