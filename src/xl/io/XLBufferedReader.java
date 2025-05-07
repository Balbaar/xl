package xl.io;

import xl.model.Cell;
import xl.util.XLException;

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
            String line;
            while ((line = readLine()) != null) {
                // Split the line into cell name and expression
                String[] parts = line.split("=", 2);
                if (parts.length != 2) {
                    throw new XLException("Invalid line format: " + line);
                }

                String cellName = parts[0].trim();
                String expression = parts[1].trim();

                // Create or update the cell in the map
                Cell cell = map.computeIfAbsent(cellName, Cell::new);
                cell.setExpression(expression, map);
            }
        } catch (Exception e) {

        }
    }
}
