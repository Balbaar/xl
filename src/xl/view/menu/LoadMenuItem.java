package xl.view.menu;

import java.io.FileNotFoundException;
import java.util.*;
import javax.swing.JFileChooser;

import xl.model.Cell;
import xl.model.Sheet;
import xl.view.ui.StatusLabel;
import xl.view.XL;
import xl.io.XLBufferedReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LoadMenuItem extends OpenMenuItem {

    public LoadMenuItem(XL xl, StatusLabel statusLabel) {
        super(xl, statusLabel, "Load");
    }

    protected void action(String path) throws FileNotFoundException {
        try {
            XLBufferedReader reader = new XLBufferedReader(path);
            Map<String, Cell> cellData = new HashMap<>();
            reader.load(cellData);

            Sheet sheet = xl.getSheet();
            sheet.clear();

            // Create all cells by name
            for (String cellName : cellData.keySet()) {
                sheet.createCell(cellName);
            }

            // Build Dependency Graph
            Map<String, List<String>> graph = new HashMap<>();
            Map<String, Integer> inDegree = new HashMap<>();

            for (String cellName : cellData.keySet()) {
                String expr = cellData.get(cellName).getExpression();
                List<String> deps = Cell.parseDependencies(expr);

                graph.put(cellName, deps);
                inDegree.put(cellName, deps.size());
            }

            // Topological Sort so they are evaluated in a safe order - Kahns Algorithm
            Queue<String> queue = new LinkedList<>();
            for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
                if (entry.getValue() == 0) {
                    queue.add(entry.getKey());
                }
            }

            while (!queue.isEmpty()) {
                String current = queue.poll();
                String expression = cellData.get(current).getExpression();
                sheet.setCellExpression(current, expression);

                for (String dependent : graph.keySet()) {
                    if (graph.get(dependent).contains(current)) {
                        inDegree.put(dependent, inDegree.get(dependent) - 1);
                        if (inDegree.get(dependent) == 0) {
                            queue.add(dependent);
                        }
                    }
                }
            }

            sheet.notifyAllCells();
            statusLabel.setText("File loaded successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }




    protected int openDialog(JFileChooser fileChooser) {
        return fileChooser.showOpenDialog(xl);
    }
}
