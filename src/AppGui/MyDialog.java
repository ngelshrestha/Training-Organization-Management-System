package AppGui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by angel on 3/16/17.
 */
public class MyDialog {

    public static String getOutput(JTable table)
    {
        JTableHeader th = table.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        String[] columns = new String[tcm.getColumnCount()];

        for (int i = 0; i<tcm.getColumnCount(); i++)
        {
            TableColumn tableColumn = tcm.getColumn(i);
            columns[i] = tableColumn.getHeaderValue().toString();
        }

        int selectedRow = table.getSelectedRow();

        String output = "";

        for(int i = 0; i<columns.length; i++)
        {
            output += columns[i] + " : " + table.getValueAt(selectedRow,i) + "\n";
        }

        return output;
    }

}
