package xl.view.ui;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

public class CurrentLabel extends ColoredLabel implements Observer {

    public CurrentLabel() {
        super("A1", Color.WHITE);
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg instanceof String[]) {
            String[] cells = (String[]) arg;
            String selectedCell = cells[1]; // The new selected cell
            setText(selectedCell);
            System.out.println("Current cell updated to: " + selectedCell);
        } else {
            setText("");
            System.out.println("Current cell cleared");
        }
    }
}