package renderer;

import adapter.RequestListAdapter;
import javax.swing.JTable;
import model.RequestLogModel;


public class RequestLogTableStatusColumnCellRenderer extends StatusColumnCellRenderer 
{
    @Override
    protected Boolean getCurrentStatus(JTable table, int row)
    {
        RequestLogModel pingRequest = ((RequestListAdapter)table.getModel()).getPingRequestAt(row);
        return pingRequest.getCurrentStatus();
    }
}