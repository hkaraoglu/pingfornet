package pingnet;

import adapter.PingListAdapter;
import adapter.RequestListAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import model.PingLogModel;
import model.PingModel;
import model.RequestLogModel;
import model.RequestModel;
import renderer.PingLogTableStatusColumnCellRenderer;
import renderer.RequestLogTableStatusColumnCellRenderer;

/**
 *
 * @author hasankaraoglu
 */
public class ConnectivityPanel extends javax.swing.JPanel implements RecordChangesListener, ActionListener
{
    private RequestListAdapter requestLogListAdapter;
    private PingListAdapter pingLogListAdapter;
    private JMenuItem requestTableMenuItemRemove;
    private JMenuItem pingTableMenuItemRemove;
    
    public ConnectivityPanel()
    {
        initComponents();
        initPingLogList();
        initRequestLogList();
        initRequestTableContextMenu();
        initPingTableContextMenu();
    }
    
     private void initRequestLogList()
    {        
        requestLogListAdapter = new RequestListAdapter(RequestModel.getList());
        requestListTable.setModel(requestLogListAdapter);
        requestListTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        requestListTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        requestListTable.getColumnModel().getColumn(2).setPreferredWidth(40);
        requestListTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        requestListTable.getColumnModel().getColumn(4).setPreferredWidth(90);
        requestListTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        requestListTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        requestListTable.getColumnModel().getColumn(RequestListAdapter.STATUS_COLUMN_INDEX).setCellRenderer(new RequestLogTableStatusColumnCellRenderer()); 
    }
    
    private void initPingLogList()
    { 
        pingLogListAdapter = new PingListAdapter(PingModel.getList());
        pingListTable.setModel(pingLogListAdapter);
        pingListTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        pingListTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        pingListTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        pingListTable.getColumnModel().getColumn(PingListAdapter.STATUS_COLUMN_INDEX).setCellRenderer(new PingLogTableStatusColumnCellRenderer()); 
    }
    
    private void initRequestTableContextMenu()
    {
        JPopupMenu popupMenu = new JPopupMenu();
        requestTableMenuItemRemove = new JMenuItem("Delete");
        requestTableMenuItemRemove.addActionListener(this);
        popupMenu.add(requestTableMenuItemRemove);
        requestListTable.setComponentPopupMenu(popupMenu);
    }
    
    private void initPingTableContextMenu()
    {
        JPopupMenu popupMenu = new JPopupMenu();
        pingTableMenuItemRemove = new JMenuItem("Delete");
        pingTableMenuItemRemove.addActionListener(this);
        popupMenu.add(pingTableMenuItemRemove);
        pingListTable.setComponentPopupMenu(popupMenu);
    }
       
    @Override
    public void onNewRequestRecordAdded(RequestModel pingModel)
    {
        addRow(pingModel);
    }
    
    private void addRow(RequestModel pingModel)
    {
        RequestListAdapter model = (RequestListAdapter) requestListTable.getModel();
        RequestLogModel pingRequestModel = new RequestLogModel(pingModel);
        model.insertRow(pingRequestModel);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JMenuItem menu = (JMenuItem) e.getSource();
        if(menu == requestTableMenuItemRemove)
        {
            if(requestListTable.getSelectedRow() != -1)
            {
                requestLogListAdapter.deleteRow(requestListTable.getSelectedRow());
            }
        }
        else if(menu == pingTableMenuItemRemove)
        {
            if(pingListTable.getSelectedRow() != -1)
            {
                pingLogListAdapter.deleteRow(pingListTable.getSelectedRow());
            }
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pingListTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        requestListTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(1024, 584));

        jPanel1.setMaximumSize(new java.awt.Dimension(900, 900));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 42));

        jScrollPane2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        pingListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        pingListTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(pingListTable);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("pingnet/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("ConnectivityPanel.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 992, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

        requestListTable.setAutoCreateRowSorter(true);
        requestListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {

            },
            new String []
            {

            }
        ));
        requestListTable.setRowSorter(null);
        requestListTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(requestListTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 999, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel2.setText(bundle.getString("ConnectivityPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1016, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(44, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable pingListTable;
    private javax.swing.JTable requestListTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onNewPingRecordAdded(PingModel pingModel)
    {
        PingListAdapter model = (PingListAdapter) pingListTable.getModel();
        PingLogModel pingLogModel = new PingLogModel(pingModel);
        model.insertRow(pingLogModel);
    }
}
