/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vedantanew;

import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author boss
 */
public class Report extends javax.swing.JPanel {
DefaultTableModel dtm ;
Connection conn;
Statement stmt;
double gtotal;

    /**
     * Creates new form Report
     */
    public Report() {
        initComponents();

        String col[] ={};
        col = new String[] {"Bill Num","Bill Date","Name","City","Total"};
       
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

public String formatDate(String type,Date d)
{
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(type);
    String format = simpleDateFormat.format(d);
    return format;
}
public void showReport(java.util.Date from, java.util.Date to)
    {
    try {
        conn = DBHelper.getConnection();
        stmt = conn.createStatement();
        String query = null;
          query = "Select * from transactions where billdate between '"+ formatDate("yyyy-MM-dd", from) +"' and '"+ formatDate("yyyy-MM-dd", to) +"'";
        dateLabel.setText(formatDate("dd-MM-yyyy", from) + "     To   ");
        dateLabel1.setText(formatDate("dd-MM-yyyy", to));
        dateLabel.setVisible(true);
        dateLabel1.setVisible(true);
        
        ResultSet executeQuery = stmt.executeQuery(query);
        String data[] = new String[5];
        
        while(executeQuery.next())
        {
            data[0] = executeQuery.getString("billNo");
            data[1] = formatDate("dd-MM-yyy",executeQuery.getDate("billDate") );
            data[2] = executeQuery.getString("pname");
            data[3] = executeQuery.getString("pcity");
            data[4] = executeQuery.getString("gtotal");
            gtotal += Double.parseDouble(data[4]);
            dtm.insertRow(dtm.getRowCount(), data);
            
        }
        gTotalLabel.setText(gtotal + "");
        executeQuery.close();
    } catch (SQLException ex) {
        Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }
    public void showReport(String task)
    {
        
    try {
        conn = DBHelper.getConnection();
        String query="";
        stmt = conn.createStatement();
        Calendar calenderInstance = Calendar.getInstance();
        java.sql.Date date = new java.sql.Date(calenderInstance.getTime().getTime());
        SimpleDateFormat sdf  = new SimpleDateFormat();
        sdf.applyPattern("dd-MM-yyyy");
        if(task.equalsIgnoreCase("today"))
        {
            query = "select * from transactions where DATE(billDate) = " + "DATE(NOW())";
            String format = sdf.format(date);
            dateLabel.setText("Date :    "+ format + "");
            dateLabel.setVisible(true);
        }
        if(task.equalsIgnoreCase("monthly"))
        {
            query = "select * from transactions where MONTH(billDate) = MONTH(CURDATE()) and YEAR(billDate) = YEAR(CURDATE())" ;
            dateLabel.setText("Month :    "+getMonthForInt(calenderInstance.get(calenderInstance.MONTH)));
            dateLabel.setVisible(true);
        }
        if(task.equalsIgnoreCase("all"))
        {
            query = "select * from transactions order by billNo asc";
            dateLabel.setText("Complete");
            dateLabel.setVisible(true);
        }
        
        ResultSet executeQuery = stmt.executeQuery(query);
        String data[] = new String[5];
        
        while(executeQuery.next())
        {
            data[0] = executeQuery.getString("billNo");
            data[1] = formatDate("dd-MM-yyy",executeQuery.getDate("billDate") );
            data[2] = executeQuery.getString("pname");
            data[3] = executeQuery.getString("pcity");
            data[4] = executeQuery.getString("gtotal");
            gtotal += Double.parseDouble(data[4]);
            dtm.insertRow(dtm.getRowCount(), data);
            
        }
        gTotalLabel.setText(gtotal + "");
        executeQuery.close();
    } catch (SQLException ex) {
        Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
    void viewBill()
{
        int rowId = displayTable.getSelectedRow();
        if(rowId != -1)
        {
            Object valueAt = dtm.getValueAt(rowId, 0);
            BillDisplay bd = new BillDisplay();
            bd.showBill(Integer.parseInt(valueAt +""));
            this.add("viewBill", bd);
            ((CardLayout)(this.getLayout())).show(this,"viewBill");
        }
        
}
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        datePanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        dateLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayTable = new javax.swing.JTable();
        bottomPanel = new javax.swing.JPanel();
        viewBill = new javax.swing.JButton();
        editBill = new javax.swing.JButton();
        deleteBill = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        gTotalLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1200, 600));
        setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(java.awt.Color.darkGray);
        jPanel2.setLayout(new java.awt.BorderLayout());

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Report.png"))); // NOI18N
        jPanel2.add(jButton1, java.awt.BorderLayout.NORTH);

        datePanel.setBackground(java.awt.Color.darkGray);
        datePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 100, 5));

        dateLabel.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        dateLabel.setForeground(java.awt.Color.white);
        dateLabel.setText("dateLabel");
        dateLabel.setMinimumSize(new java.awt.Dimension(175, 20));
        dateLabel.setPreferredSize(new java.awt.Dimension(175, 20));
        datePanel.add(dateLabel);

        dateLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        dateLabel1.setForeground(java.awt.Color.white);
        dateLabel1.setText("dateLabel");
        dateLabel1.setMinimumSize(new java.awt.Dimension(175, 20));
        dateLabel1.setPreferredSize(new java.awt.Dimension(175, 20));
        datePanel.add(dateLabel1);

        jPanel2.add(datePanel, java.awt.BorderLayout.CENTER);

        mainPanel.add(jPanel2, java.awt.BorderLayout.NORTH);

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

        mainPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        bottomPanel.setBackground(java.awt.Color.darkGray);
        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 100, 5));

        viewBill.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        viewBill.setText("View Bill");
        viewBill.setMinimumSize(new java.awt.Dimension(150, 30));
        viewBill.setPreferredSize(new java.awt.Dimension(150, 30));
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
        bottomPanel.add(viewBill);

        editBill.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        editBill.setText("Edit Bill");
        editBill.setMinimumSize(new java.awt.Dimension(150, 30));
        editBill.setPreferredSize(new java.awt.Dimension(150, 30));
        editBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBillActionPerformed(evt);
            }
        });
        editBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editBillKeyPressed(evt);
            }
        });
        bottomPanel.add(editBill);

        deleteBill.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        deleteBill.setText("Delete Bill");
        deleteBill.setMinimumSize(new java.awt.Dimension(150, 30));
        deleteBill.setPreferredSize(new java.awt.Dimension(150, 30));
        deleteBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBillActionPerformed(evt);
            }
        });
        deleteBill.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deleteBillKeyPressed(evt);
            }
        });
        bottomPanel.add(deleteBill);

        jLabel10.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel10.setForeground(java.awt.Color.white);
        jLabel10.setText("Grand Total :");
        bottomPanel.add(jLabel10);

        gTotalLabel.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
        gTotalLabel.setForeground(java.awt.Color.white);
        gTotalLabel.setText("0");
        bottomPanel.add(gTotalLabel);

        mainPanel.add(bottomPanel, java.awt.BorderLayout.PAGE_END);

        add(mainPanel, "mainCard");
    }// </editor-fold>//GEN-END:initComponents

    private void viewBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewBillActionPerformed
    // TODO add your handling code here:
        viewBill();
    }//GEN-LAST:event_viewBillActionPerformed

    private void viewBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_viewBillKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        viewBill();
    }//GEN-LAST:event_viewBillKeyPressed
void deleteBill()
{
        int rowId = displayTable.getSelectedRow();
        if(rowId != -1)
        {
            Object valueAt = dtm.getValueAt(rowId, 0);
            BillDisplay bd = new BillDisplay();
            String[] options = {"YES","NO"};
            String[] options1 = {"OK"};
            int dialogButton = JOptionPane.showOptionDialog(null,"Sure You Want to delete Bill Num " + valueAt.toString(), "Delete Bill", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[1]);
           if(dialogButton == JOptionPane.YES_OPTION)
           {
              boolean delete = bd.delete(Integer.parseInt(valueAt +""));
                if(delete)
                {
                    Double newTotal = Double.parseDouble(gTotalLabel.getText()) - Double.parseDouble(dtm.getValueAt(rowId, 4)+"");
                    dtm.removeRow(rowId);
                    gTotalLabel.setText(newTotal.toString());
                }
           }
        
            
        }
    
}

void editBill(String billNum)
    {
        String[] options = {"YES","NO"};
        int dialogButton = JOptionPane.showOptionDialog(null,"Sure You Want to Edit Bill Num " + billNum, "Edit Bill", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null,options,options[1]); 
        if(dialogButton == JOptionPane.YES_OPTION)
        {
            try {
                int billNo = Integer.parseInt(billNum);
                conn = DBHelper.initConnection();
                stmt = conn.createStatement();
                String query = "Select * from transactions where billNo ="+ billNo;
                ResultSet executeQuery = stmt.executeQuery(query);
                if(executeQuery.first())
                {
                    Biller biller = new Biller(billNo);
                    this.add("editBill",biller);
                    CardLayout cl = (CardLayout)(this.getLayout());
                    cl.show(this, "editBill");
                }
                else
                {
                    String[] options1 = {"OK"};
                    JOptionPane.showMessageDialog(null,"Bill Number "+ billNum +" Not Exists ", "Bill Not Found", JOptionPane.ERROR_MESSAGE,null);
                }
                
                
            } catch (SQLException ex) {
                Logger.getLogger(BillDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
        }
    }
  

void editBill()
{
    int rowId = displayTable.getSelectedRow();
    if(rowId !=-1)
    {
        Object valueAt = dtm.getValueAt(rowId, 0);
        editBill(valueAt.toString());
    }
    
   
}
    private void editBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBillActionPerformed
        // TODO add your handling code here:
        editBill();
    }//GEN-LAST:event_editBillActionPerformed

    private void editBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editBillKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            editBill();
        }
    }//GEN-LAST:event_editBillKeyPressed

    private void deleteBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBillActionPerformed
        // TODO add your handling code here:
         deleteBill();
    }//GEN-LAST:event_deleteBillActionPerformed

    private void deleteBillKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deleteBillKeyPressed
        // TODO add your handling code here:
            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            {
                deleteBill();
            }
    }//GEN-LAST:event_deleteBillKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dateLabel1;
    private javax.swing.JPanel datePanel;
    private javax.swing.JButton deleteBill;
    private javax.swing.JTable displayTable;
    private javax.swing.JButton editBill;
    private javax.swing.JLabel gTotalLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton viewBill;
    // End of variables declaration//GEN-END:variables
}
