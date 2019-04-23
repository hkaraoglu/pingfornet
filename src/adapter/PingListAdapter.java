package adapter;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.PingLogModel;
import model.PingLogModel.PingLogListener;

/**
 *
 * @author hasankaraoglu
 */
public class PingListAdapter extends AbstractTableModel implements PingLogListener
{
    
    private final List<PingLogModel> pingList;
    private final String[] columnNames = 
    { 
        "IP Address / Hostname", 
        "Port",
        "Interval(s)",
        "Date Added",
        "Date Modified",
        "Success",
        "Failure",
        "Status"
    };
    public static int STATUS_COLUMN_INDEX = 7;
    private final Class[] columnClass = 
    { 
        String.class,  
        Integer.class,
        Integer.class,
        String.class,
        String.class,
        Integer.class,
        Integer.class,
        String.class
    };
    
    public PingListAdapter(List<PingLogModel> pingList)
    {
        this.pingList = pingList;
        pingList.forEach((ping) ->
        {
            ping.setListener(PingListAdapter.this);
            ping.start();
        });
    }
    
    @Override
    public int getRowCount()
    {
        return pingList.size();
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex]; 
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        PingLogModel pingLog = pingList.get(rowIndex);
        Object[] columnValues = new Object[]
        {
            pingLog.getPingModel().getIpHostname(),
            pingLog.getPingModel().getPort(),
            pingLog.getPingModel().getInterval(),
            pingLog.getDateAdded(),
            pingLog.getDateModified(),
            pingLog.getSuccessCount(),
            pingLog.getFailCount(),
            ""
        };
        return columnValues[columnIndex];
    }
    public PingLogModel getPingLogAt(int row) {
        return pingList.get(row);
    }   
     public void insertRow(PingLogModel pingLog)
    {
        pingLog.setListener(this);
        this.pingList.add(pingLog);
        fireTableRowsInserted(0, 1);
        pingLog.start();
    }
    
    public void deleteRow(int row)
    {
        PingLogModel pingLogModel = this.pingList.get(row);
        pingLogModel.kill();
        pingLogModel.getPingModel().delete();
        this.pingList.remove(pingLogModel);
        fireTableDataChanged();
    }

    @Override
    public void onPingLogUpdated(PingLogModel pingLogModel)
    {
        fireTableDataChanged();
    }
    
}
