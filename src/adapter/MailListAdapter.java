package adapter;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.MailerModel;

/**
 *
 * @author hkaraoglu
 */
public class MailListAdapter extends AbstractTableModel{

    private final List<MailerModel> mailList;
    private final String[] columnNames = 
    { 
        "SMTP Server", 
        "From", 
        "To", 
        "Username", 
        "Password", 
        
    };
    private final Class[] columnClass = 
    { 
        String.class, 
        String.class, 
        String.class,
        String.class,
        String.class,
    };
    
    public MailListAdapter(List<MailerModel> mailList) 
    {
        this.mailList = mailList;
    }

    @Override
    public int getRowCount() {
        return mailList.size();
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
        MailerModel mailModel = mailList.get(rowIndex);
        Object[] columnValues = new Object[]
        {
            mailModel.getSmtp(),
            mailModel.getFrom(),
            mailModel.getTo(),
            mailModel.getUsername(),
            "*****"
        };
        return columnValues[columnIndex];
    }

    @Override
    public Class getColumnClass(int columnIndex) {
      return columnClass[columnIndex];
    }

    public MailerModel getPingRequestAt(int row) {
        return mailList.get(row);
    }
    
    public void insertRow(MailerModel mailModel)
    {
        this.mailList.add(mailModel);
        fireTableRowsInserted(0, 1);
    }
    
    public void deleteRow(int row)
    {
        MailerModel mailModel = this.mailList.get(row);
        mailModel.delete();
        this.mailList.remove(mailModel);
        fireTableDataChanged();
    }

}
