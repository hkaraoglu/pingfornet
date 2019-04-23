package pingnet;

import adapter.HardwareLogListAdapter;
import model.HardwareLogModel;
import model.HardwareModel;

/**
 *
 * @author hasankaraoglu
 */
public class SystemPanel extends javax.swing.JPanel implements SystemChangesListener
{
    private final HardwareLogListAdapter hardwareLogListAdapter;
    
    public SystemPanel()
    {
        initComponents();
        hardwareLogListAdapter = new HardwareLogListAdapter(HardwareModel.getList());
        systemLogsTable.setModel(hardwareLogListAdapter);
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        systemLogsTable = new javax.swing.JTable();

        systemLogsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        jScrollPane1.setViewportView(systemLogsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 590, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable systemLogsTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onNewSystemRecordAdded(HardwareModel hardwareModel)
    {
        HardwareLogModel hardwareLogModel = new  HardwareLogModel(hardwareModel);
        hardwareLogListAdapter.insertRow(hardwareLogModel);
    }
}
