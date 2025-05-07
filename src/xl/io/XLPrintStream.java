package xl.io;

import xl.model.Cell;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.Set;

// TODO move to another package
public class XLPrintStream extends PrintStream {

    public XLPrintStream(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    // TODO Change Object to something appropriate
    public void save(Set<Entry<String, Cell>> set) {
        for (Entry<String, Cell> entry : set) {
            print(entry.getKey());
            print('=');
            println(entry.getValue().getExpression());
        }
        flush();
        close();
    }
}
