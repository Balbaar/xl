package xl.controller;

import xl.io.XLBufferedReader;
import xl.io.XLPrintStream;
import xl.model.Cell;
import xl.model.Sheet;
import xl.util.XLException;
import xl.view.logic.SelectionModel;
import xl.view.ui.StatusLabel;

import java.util.*;

public class SheetController {
    private final Sheet sheet;
    private final SelectionModel selectionModel;
    private final StatusLabel statusLabel;

    public SheetController(Sheet sheet, SelectionModel selectionModel, StatusLabel statusLabel) {
        this.sheet = sheet;
        this.selectionModel = selectionModel;
        this.statusLabel = statusLabel;
    }

    public void updateCell(String cellName, String expression) {
        String oldExpression = "";

        try {
            if (!sheet.cellExists(cellName)) {
                sheet.createCell(cellName);
            }

            Cell cell = sheet.getCell(cellName);
            oldExpression = cell.getExpression();
            cell.setExpression(expression, sheet.getCells());

            selectionModel.setSelectedCell(cellName);
            selectionModel.updateSlotLabels();
            statusLabel.setText(" Cell " + cellName + " updated.");

        }
        //Rollback and catch error for statuslabel
        catch (XLException e) {
            Cell cell = sheet.getCell(cellName);
            cell.setExpression(oldExpression, sheet.getCells());
            statusLabel.setText(" Error: " + e.getMessage());
        }
    }


    public void clearAllCells() {
        sheet.clear();
        selectionModel.updateSlotLabels();  // Refresh the view after clearing
        statusLabel.setText("All cells cleared.");
    }

    public void clearSelectedCell() {
        String selectedCell = selectionModel.getSelectedCell();
        if (selectedCell != null) {
            sheet.setCellExpression(selectedCell, "");
            statusLabel.setText("Cell " + selectedCell + " cleared.");
            selectionModel.updateSlotLabels();
        }
    }

    public void saveFile(String path) {
        if (!path.endsWith(".xl")) {
            path += ".xl";
        }

        try (XLPrintStream printStream = new XLPrintStream(path)) {
            Map<String, Cell> cells = sheet.getCells();
            printStream.save(cells.entrySet());
            statusLabel.setText("File saved successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error saving file: " + e.getMessage());
        }
    }

    public void loadFile(String path) {
        try {
            XLBufferedReader reader = new XLBufferedReader(path);
            Map<String, Cell> cellData = new HashMap<>();
            reader.load(cellData);

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

            // Topological Sort (Kahn's Algorithm)
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
            selectionModel.updateSlotLabels();
            statusLabel.setText("File loaded successfully.");
        } catch (Exception e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
        }
    }


    public String getCellExpression(String cellName) {
        return sheet.getCellExpression(cellName);
    }
}
