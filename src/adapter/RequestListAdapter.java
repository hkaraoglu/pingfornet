package adapter;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.RequestLogModel;
import model.RequestLogModel.PingRequestListener;

/**
 *
 * @author hkaraoglu
 */
public class RequestListAdapter extends AbstractTableModel implements PingRequestListener {

    private final List<RequestLogModel> pingRequestList;
    public static final int STATUS_COLUMN_INDEX = 9;
    private final String[] columnNames = 
    { 
        "Protocol", 
        "IP Address / Hostname", 
        "Port",  
        "Interval(s)", 
        "Timeout(ms)",
        "Date Added",
        "Date Modified",
        "Success",
        "Failure",
        "Status"
    };
    private final Class[] columnClass = 
    { 
        String.class, 
        String.class, 
        Integer.class,
        String.class,
        String.class,
        String.class,
        String.class,
        Integer.class,
        Integer.class,
        String.class
    };

    public RequestListAdapter(List<RequestLogModel> pingRequestList) 
    {
        this.pingRequestList = pingRequestList;
        pingRequestList.forEach((pingRequest) ->
        {
            pingRequest.setListener(RequestListAdapter.this);
            pingRequest.start();
        });
    }

    @Override
    public int getRowCount() {
        return pingRequestList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
      return columnNames[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) 
    {   
        RequestLogModel pingRequest = pingRequestList.get(rowIndex);
        Object[] columnValues = new Object[]
        {
            pingRequest.getPingModel().getProtocol(),
            pingRequest.getPingModel().getIpHost(),
            pingRequest.getPingModel().getPort(),
            pingRequest.getPingModel().getInterval(),
            pingRequest.getPingModel().getTimeout(),
            pingRequest.getDateAdded(),
            pingRequest.getDateUpdated(),
            pingRequest.getSuccessCount(),
            pingRequest.getFailCount(),
            ""
        };
        return columnValues[columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
      return columnClass[columnIndex];
    }

    public RequestLogModel getPingRequestAt(int row) {
        return pingRequestList.get(row);
    }
    
    
    
    public void insertRow(RequestLogModel requestLog)
    {
        requestLog.setListener(this);
        requestLog.start();
        this.pingRequestList.add(requestLog);
        fireTableRowsInserted(0, 1);
    }
    
    public void deleteRow(int row)
    {
        RequestLogModel pingRequestModel = this.pingRequestList.get(row);
        pingRequestModel.kill();
        pingRequestModel.getPingModel().delete();
        this.pingRequestList.remove(pingRequestModel);
        fireTableDataChanged();
    }
    
    @Override
    public void onRequestUpdated(RequestLogModel pingRequest)
    {
        fireTableDataChanged();
    }

    
}
