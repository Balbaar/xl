package xl.util;

import xl.Cell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

// TODO move to another package
public class XLBufferedReader extends BufferedReader {

    public XLBufferedReader(String name) throws FileNotFoundException {
        super(new FileReader(name));
    }

    public void load(Map<String, Cell> map) {
        try {
            while (ready()) {
                String string = readLine();
                int i = string.indexOf('=');
                if (i > 0) {
                    String key = string.substring(0, i).trim(); // Extract key
                    String value = string.substring(i + 1).trim(); // Extract value
                    Cell cell = map.computeIfAbsent(key, Cell::new); // Get or create Cell
                    cell.setExpression(value, map); // Set the expression
                }
            }
        } catch (Exception e) {
            throw new XLException("Error loading file: " + e.getMessage());
        }
    }
}
