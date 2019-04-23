package renderer;

import adapter.PingListAdapter;
import javax.swing.JTable;
import model.PingLogModel;


public class PingLogTableStatusColumnCellRenderer extends StatusColumnCellRenderer 
{ 
    @Override
    protected Boolean getCurrentStatus(JTable table, int row)
    {
        PingLogModel pingLog = ((PingListAdapter)table.getModel()).getPingLogAt(row);
        return pingLog.getCurrentStatus();
    }
}