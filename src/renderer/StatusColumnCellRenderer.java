package renderer;

import adapter.RequestListAdapter;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import model.RequestLogModel;


public abstract class StatusColumnCellRenderer extends DefaultTableCellRenderer 
{
  @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) 
    {
        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
        Boolean currentStatus = getCurrentStatus(table, row);
        if (currentStatus == null) 
        {
            l.setBackground(Color.WHITE);
        } 
        else if(!currentStatus)
        {
            l.setBackground(Color.RED);
        }
        else
        {
            l.setBackground(Color.GREEN);
        }
        return l;
    }

    protected abstract  Boolean getCurrentStatus(JTable table, int row);
}