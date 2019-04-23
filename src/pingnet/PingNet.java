/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingnet;

import javax.swing.ImageIcon;
import util.ImageUtils;

/**
 *
 * @author hkaraoglu
 */
public class PingNet extends javax.swing.JFrame 
{    
    private ConnectivityPanel connectivityPanel;
    private SystemPanel systemPanel;
    
    public PingNet()
    {
        initComponents();
        initTabs();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        tabs = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        pingMenuItem = new javax.swing.JMenuItem();
        requestMenuItem = new javax.swing.JMenuItem();
        systemMenuItem = new javax.swing.JMenuItem();
        mailListMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ping for Net");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(1024, 768));

        jMenu1.setText("Options");

        jMenu3.setText("New...");

        pingMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        pingMenuItem.setText("Ping");
        pingMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                pingMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(pingMenuItem);

        requestMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        requestMenuItem.setText("Request");
        requestMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                requestMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(requestMenuItem);

        systemMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        systemMenuItem.setText("System");
        systemMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                systemMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(systemMenuItem);

        jMenu1.add(jMenu3);

        mailListMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mailListMenuItem.setText("Mail List");
        mailListMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                mailListMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(mailListMenuItem);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs, javax.swing.GroupLayout.DEFAULT_SIZE, 1321, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabs)
                .addContainerGap(665, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pingMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_pingMenuItemActionPerformed
    {//GEN-HEADEREND:event_pingMenuItemActionPerformed
        NewPingForm newPingForm = new NewPingForm(this, rootPaneCheckingEnabled, connectivityPanel);
        newPingForm.setVisible(true);
    }//GEN-LAST:event_pingMenuItemActionPerformed

    private void requestMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_requestMenuItemActionPerformed
    {//GEN-HEADEREND:event_requestMenuItemActionPerformed
        NewRequestForm pingForm = new NewRequestForm(connectivityPanel);
        pingForm.setVisible(true);
    }//GEN-LAST:event_requestMenuItemActionPerformed

    private void mailListMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_mailListMenuItemActionPerformed
    {//GEN-HEADEREND:event_mailListMenuItemActionPerformed
        MailListForm mailListForm = new MailListForm(this, rootPaneCheckingEnabled);
        mailListForm.setVisible(true);
    }//GEN-LAST:event_mailListMenuItemActionPerformed

    private void systemMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_systemMenuItemActionPerformed
    {//GEN-HEADEREND:event_systemMenuItemActionPerformed
        NewSystemForm newSystemForm = new NewSystemForm(this, rootPaneCheckingEnabled, systemPanel);
        newSystemForm.setVisible(true);
    }//GEN-LAST:event_systemMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(PingNet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PingNet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PingNet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PingNet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new PingNet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem mailListMenuItem;
    private javax.swing.JMenuItem pingMenuItem;
    private javax.swing.JMenuItem requestMenuItem;
    private javax.swing.JMenuItem systemMenuItem;
    private javax.swing.JTabbedPane tabs;
    // End of variables declaration//GEN-END:variables

    

    private void initTabs()
    {
        ImageIcon icon = ImageUtils.createImageIcon(getClass(), "/resources/pulse.png", "System", 14, 14);
        connectivityPanel = new ConnectivityPanel();
        tabs.addTab("Connectivity ", icon, connectivityPanel);
        
        icon = ImageUtils.createImageIcon(getClass(), "/resources/cpu.png", "System", 14, 14);
        systemPanel = new SystemPanel();
        tabs.addTab("System ", icon, systemPanel);
    }

}
