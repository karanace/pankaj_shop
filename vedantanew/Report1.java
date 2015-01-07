package vedantanew;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boss
 */
public class Report1 extends javax.swing.JFrame {
DefaultTableModel dtm ;
Connection conn;
Statement stmt;
double gtotal;
    /**
     * Creates new form Report
     */
    public Report1() {
        initComponents();
        String col[] = {"Bill Num","Bill Date","Name","City","Total"};
        dtm = new DefaultTableModel(col,0);
        displayTable.setModel(dtm);
        gtotal = 0;
        dateLabel.setVisible(false);
        dateLabel1.setVisible(false);
        
        
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
//    public void showReport(java.util.Date from, java.util.Date to)
//    {
//    try {
//        conn = DBHelper.getConnection();
//        stmt = conn.createStatement();
//        String query = null;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD");
//        SimpleDateFormat displayFormat = new SimpleDateFormat("DD-MM-YYYY");
//        String fromString = simpleDateFormat.format(from);
//        String toString = simpleDateFormat.format(to);
//        String fromDisplay = displayFormat.format(from);
//        String toDisplay = displayFormat.format(to);
//        query = "Select * from transactions where billdate between '"+ fromString +"' and '"+ toString +"'";
//        dateLabel.setText(fromDisplay + "     To   ");
//        dateLabel1.setText(toDisplay);
//        dateLabel.setVisible(true);
//        dateLabel1.setVisible(true);
//        
//        ResultSet executeQuery = stmt.executeQuery(query);
//        String data[] = new String[5];
//        
//        if(executeQuery.first())
//        {
//            data[0] = executeQuery.getString("billNo");
//            data[1] = executeQuery.getDate("billDate")+"";
//            data[2] = executeQuery.getString("pname");
//            data[3] = executeQuery.getString("pcity");
//            data[4] = executeQuery.getString("gtotal");
//            gtotal += Double.parseDouble(data[4]);
//            dtm.insertRow(dtm.getRowCount(), data);
//            
//        }
//        else
//        {
//            //System.out.println("No Data Found");
//        }
//        while(executeQuery.next())
//        {
//            data[0] = executeQuery.getString("billNo");
//            data[1] = executeQuery.getDate("billDate")+"";
//            data[2] = executeQuery.getString("pname");
//            data[3] = executeQuery.getString("pcity");
//            data[4] = executeQuery.getString("gtotal");
//            gtotal += Double.parseDouble(data[4]);
//            dtm.insertRow(dtm.getRowCount(), data);
//            
//        }
//        gTotalLabel.setText(gtotal + "");
//        executeQuery.close();
//    } catch (SQLException ex) {
//        Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
//    }
//        
//    }
//    public void showReport(String type,String from,String to)
//    {
//        
//    try {
//        conn = DBHelper.getConnection();
//        String query="";
//        stmt = conn.createStatement();
//        Calendar calenderInstance = Calendar.getInstance();
//        java.sql.Date date = new java.sql.Date(calenderInstance.getTime().getTime());
//        SimpleDateFormat sdf  = new SimpleDateFormat();
//        sdf.applyPattern("DD-MM-YYYY");
//        if(type.equalsIgnoreCase("today"))
//        {
//            query = "select * from transactions where DATE(billDate) = " + "DATE(NOW())";
//            String format = sdf.format(date);
//            dateLabel.setText("Date :    "+ format + "");
//            dateLabel.setVisible(true);
//        }
//        else if(type.equalsIgnoreCase("monthly"))
//        {
//            query = "select * from transactions where MONTH(billDate) = " + "MONTH(CURDATE())";
//            dateLabel.setText("Month :    "+getMonthForInt(calenderInstance.get(calenderInstance.MONTH)));
//            dateLabel.setVisible(true);
//        }
//        ResultSet executeQuery = stmt.executeQuery(query);
//        String data[] = new String[5];
//        
//        if(executeQuery.first())
//        {
//            data[0] = executeQuery.getString("billNo");
//            data[1] = executeQuery.getDate("billDate")+"";
//            data[2] = executeQuery.getString("pname");
//            data[3] = executeQuery.getString("pcity");
//            data[4] = executeQuery.getString("gtotal");
//            gtotal += Double.parseDouble(data[4]);
//            dtm.insertRow(dtm.getRowCount(), data);
//            
//        }
//        else
//        {
//           // System.out.println("No Data Found");
//        }
//        while(executeQuery.next())
//        {
//            data[0] = executeQuery.getString("billNo");
//            data[1] = executeQuery.getDate("billDate")+"";
//            data[2] = executeQuery.getString("pname");
//            data[3] = executeQuery.getString("pcity");
//            data[4] = executeQuery.getString("gtotal");
//            gtotal += Double.parseDouble(data[4]);
//            dtm.insertRow(dtm.getRowCount(), data);
//            
//        }
//        gTotalLabel.setText(gtotal + "");
//        executeQuery.close();
//    } catch (SQLException ex) {
//        Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        dateLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayTable = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        gTotalLabel = new javax.swing.JLabel();
        viewBill = new javax.swing.JButton();

        jLabel3.setText("jLabel3");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(java.awt.Color.cyan);
        jPanel1.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Vedanta Mobiles Solutions");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(153, 153, 153)
                .addComponent(jLabel1)
                .addContainerGap(184, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel2.setBackground(java.awt.Color.orange);

        jLabel2.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        jLabel2.setText(" Report");

        dateLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        dateLabel.setText("dateLabel");

        dateLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        dateLabel1.setText("dateLabel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(159, 159, 159)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateLabel)
                    .addComponent(dateLabel1))
                .addGap(118, 118, 118))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(dateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateLabel1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(20, 20, 20))))
        );

        displayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Bill Num", "Bill Date", "Name", "City", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(displayTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        jLabel10.setText("Grand Total :");

        gTotalLabel.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        gTotalLabel.setForeground(java.awt.Color.red);
        gTotalLabel.setText("0");

        viewBill.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        viewBill.setText("View Bill");
        viewBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewBillActionPerformed(evt);
            }
        });
        viewBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                viewBillKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap(301, Short.MAX_VALUE)
                .addComponent(viewBill)
                .addGap(63, 63, 63)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(gTotalLabel)
                .addGap(145, 145, 145))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(gTotalLabel)
                    .addComponent(viewBill))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
      //  SalesReportHome srh = new SalesReportHome();
      //  srh.setVisible(true);
       // this.dispose();
    }//GEN-LAST:event_formWindowClosing
void viewBill()
{
        int rowId = displayTable.getSelectedRow();
        Object valueAt = dtm.getValueAt(rowId, 0);
        Report1 bd = new Report1();
       // bd.showBill(Integer.parseInt(valueAt +""));
      //  bd.setVisible(true);
}
    private void viewBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBillActionPerformed
        // TODO add your handling code here:
        viewBill();
    }//GEN-LAST:event_viewBillActionPerformed

    private void viewBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewBillKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        viewBill();
    }//GEN-LAST:event_viewBillKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Report1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Report1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Report1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Report1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Report1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dateLabel1;
    private javax.swing.JTable displayTable;
    private javax.swing.JLabel gTotalLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton viewBill;
    // End of variables declaration//GEN-END:variables
}
