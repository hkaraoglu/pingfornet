package adapter;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.HardwareLogModel;
import model.HardwareLogModel.HardwareLogListener;

/**
 *
 * @author hasankaraoglu
 */
public class HardwareLogListAdapter extends AbstractTableModel implements HardwareLogListener
{   
    private final List<HardwareLogModel> hardwareLogList;
    private final String[] columnNames = 
    { 
        "IP Address / Hostname", 
        "CPU Usage",
        "Ram Usage",
        "Disk Usage"
    };
    private final Class[] columnClass = 
    { 
        String.class,  
        String.class,
        String.class,
        String.class
    };
    
    public HardwareLogListAdapter(List<HardwareLogModel> hardwareLogList)
    {
        this.hardwareLogList = hardwareLogList;
        hardwareLogList.forEach((hardwareLog) ->
        {
            hardwareLog.setListener(HardwareLogListAdapter.this);
            hardwareLog.start();
        });
    }
    
    @Override
    public int getRowCount()
    {
        return hardwareLogList.size();
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        HardwareLogModel hardwareLog = hardwareLogList.get(rowIndex);
        Object[] columnValues = new Object[]
        {
            hardwareLog.getHardwareModel().getIpHostname(),
            hardwareLog.getCpuUsage(),
            hardwareLog.getRamUsage(),
            hardwareLog.getDiskUsage(),
        };
       return columnValues[columnIndex];
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
      return columnClass[columnIndex];
    }
    
    @Override
    public String getColumnName(int columnIndex)
    {
        return columnNames[columnIndex]; 
    }

    public HardwareLogModel getHardwareAt(int row) {
        return hardwareLogList.get(row);
    }

    public void insertRow(HardwareLogModel hardwareLog)
    {
        hardwareLog.setListener(this);
        hardwareLog.start();
        this.hardwareLogList.add(hardwareLog);
        fireTableRowsInserted(0, 1);
    }
    
    public void deleteRow(int row)
    {
        HardwareLogModel hardwareLogModel = this.hardwareLogList.get(row);
        hardwareLogModel.kill();
        hardwareLogModel.getHardwareModel().delete();
        this.hardwareLogList.remove(hardwareLogModel);
        fireTableDataChanged();
    }

    @Override
    public void onHardwareLogUpdated(HardwareLogModel hardwareLogModel)
    {
        fireTableDataChanged();
    }
    
}
