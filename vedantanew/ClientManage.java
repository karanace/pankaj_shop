package vedantanew;


import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boss
 */
public class ClientManage extends javax.swing.JPanel {
    java.sql.Connection conn;
    Statement stmt;
    /**
     * Creates new form ProductManage
     */
    public ClientManage() {
        initComponents();
        availableLabel.setVisible(false);
        errorLabel.setText("No Error At Present");
        editIdField.setEditable(false);
        editUpdateButton.setEnabled(false);
        initConnection();
        updateClientList();
        
               
        
        
    }
    
    //My Code Starts
    public void initConnection()    {
        conn = DBHelper.getConnection();
    }
    //My Code Ends
    
    
    
    void editUpdate()
    {
        String query = null;
        if(editNameField.getText().isEmpty() || editCityField.getText().isEmpty())
        {
            errorLabel.setText("Name or City empty");
        }
        else
        {
            try {
                String message = "Set Name : "+editNameField.getText()+"\nSet City : "+ editCityField.getText()+"\nFor Id :" + editIdField.getText();
                int dialogButton = JOptionPane.showConfirmDialog(null,message, "Update", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION)
                {
                    query = "Update client set name = '" + editNameField.getText() + "', city = '"+editCityField.getText()+"' where id = '"+ editIdField.getText()+"'";
                    stmt = conn.createStatement();
                    int executeUpdate = stmt.executeUpdate(query);
                    editClientList.removeAllItems();
                    clientList.removeAllItems();
                    updateClientList();
                    editCityField.setText(null);
                    editNameField.setText(null);
                    editIdField.setText(null);
                    editUpdateButton.setEnabled(false);
                    editClientList.requestFocus();
                    errorLabel.setText("No Error at present");
               }
                
            } catch (SQLException ex) {
                Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
            }
              
        }
    }
    private void updateClientList() {                                                    
        try {
            // TODO add your handling code here:
            String query = "select * from client order by name";
            stmt = conn.createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            String client = "";
            while(executeQuery.next())
            {
                client  = executeQuery.getString("name")+" ("+executeQuery.getString("city")+") "+executeQuery.getString("id");
                clientList.addItem(client);
                editClientList.addItem(client);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    void editSelectClient()
    {
        if(editClientList.getSelectedIndex() !=-1)
        {
            try {
                  String selectedClient = editClientList.getSelectedItem().toString();
                  String id = selectedClient.substring(selectedClient.lastIndexOf(")")+2);
            
            String query = "select * from client where id = '"+ id+"'";
            stmt = conn.createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            if(executeQuery.first())
            {
                editIdField.setText(executeQuery.getString("id"));
                editNameField.setText(executeQuery.getString("name"));
                editCityField.setText(executeQuery.getString("city"));
                errorLabel.setText("No Error at present");
                editUpdateButton.setEnabled(true);
            }
            else
            editUpdateButton.setEnabled(false);
        } catch (SQLException ex) {
            editUpdateButton.setEnabled(false);
            Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
        }
        else
        {
            errorLabel.setText("No Client Selected");
            
        }
    }
    void deleteClient()
    {
        if(clientList.getSelectedIndex() !=-1)
        {
            
            String message = "Are You Sure, Delete Client \n "+ clientList.getSelectedItem().toString();
                int dialogButton = JOptionPane.showConfirmDialog(null,message, "Update", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION)
                {
                    try {
            // TODO add your handling code here:
            String selectedClient = clientList.getSelectedItem().toString();
            String id = selectedClient.substring(selectedClient.lastIndexOf(")")+2);
            int index = clientList.getSelectedIndex();
            clientList.removeItemAt(index);
            editClientList.removeItemAt(index);
            String query = "delete from client where id = '"+ id+"'";
            stmt = conn.createStatement();
            int executeUpdate = stmt.executeUpdate(query);
            errorLabel.setText("No Error At Present");
            updateClientList();
        } catch (SQLException ex) {
            Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
        }
                }
        }
        else
        {
            errorLabel.setText("No Client Selected");
            
        }
    }
void availabilty()
{
    if(idField.getText().isEmpty()==false)
    {
        try {
            // TODO add your handling code here:
            stmt = conn.createStatement();
            String trimmedString = idField.getText().replaceAll("\\s+","");
            String query = "select * from client where id = '"+ trimmedString + "'";
            ResultSet executeQuery = stmt.executeQuery(query);
            //System.out.println(executeQuery.first());

            if(executeQuery.first() == false)
            {
                availableLabel.setText("Available");
                availableLabel.setForeground(Color.GREEN);
                availableLabel.setVisible(true);
            }
            else
            {
                availableLabel.setText("Not Available");
                availableLabel.setForeground(Color.RED);
                availableLabel.setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else
    {
        availableLabel.setVisible(false);
        errorLabel.setText("Id Field Empty");
    }
}
void add()
{
    if(idField.getText().isEmpty() || nameField.getText().isEmpty() || cityField.getText().isEmpty())
        {
            errorLabel.setText("Enter All 3 Fields");
        }
        else
        {
            try {
                stmt = conn.createStatement();
                String trimmedString = idField.getText().replaceAll("\\s+","");
                String query = "select * from client where id = '"+ trimmedString + "'";
                ResultSet executeQuery = stmt.executeQuery(query);
                if(executeQuery.first() == false)
                {
                    availableLabel.setVisible(false);
                    query = "insert into client values('"+ trimmedString + "','"+ nameField.getText()+"','"+cityField.getText()+"')";
                    int executeUpdate = stmt.executeUpdate(query);
                    clientList.addItem(nameField.getText()+" ("+cityField.getText()+") "+trimmedString);
                    editClientList.addItem(nameField.getText()+" ("+cityField.getText()+") "+trimmedString);
                    errorLabel.setText("No Error At Present");
                    idField.setText("");
                    nameField.setText("");
                    cityField.setText("");
                    

                }
                else
                {
                    availableLabel.setText("Not Available");
                    availableLabel.setForeground(Color.RED);
                    availableLabel.setVisible(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientManage.class.getName()).log(Level.SEVERE, null, ex);
            }
        }}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        clientList = new javax.swing.JComboBox();
        deleteClientButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        availableLabel = new javax.swing.JLabel();
        availabilityButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        editClientList = new javax.swing.JComboBox();
        editSelectClient = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        editNameField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        editCityField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        editIdField = new javax.swing.JTextField();
        editUpdateButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        errorPanel = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1260, 480));
        setPreferredSize(new java.awt.Dimension(300, 300));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(java.awt.Color.darkGray);
        jPanel3.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jPanel3.setMaximumSize(new java.awt.Dimension(450, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(430, 480));
        jPanel3.setPreferredSize(new java.awt.Dimension(450, 480));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Client :");

        deleteClientButton.setText("Delete Client");
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });
        deleteClientButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deleteClientButtonKeyPressed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/deleteClient.png"))); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deleteClientButton)
                        .addGap(125, 125, 125))
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(clientList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(clientList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(deleteClientButton)
                .addContainerGap(276, Short.MAX_VALUE))
        );

        add(jPanel3, java.awt.BorderLayout.EAST);

        jPanel1.setBackground(java.awt.Color.darkGray);
        jPanel1.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jPanel1.setMaximumSize(new java.awt.Dimension(450, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(430, 480));
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 480));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Client Name :");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Client City :");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel4.setForeground(java.awt.Color.white);
        jLabel4.setText("Unique Id :");

        availableLabel.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        availableLabel.setForeground(java.awt.Color.white);
        availableLabel.setText("Available");

        availabilityButton.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        availabilityButton.setText("Check Availability");
        availabilityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                availabilityButtonActionPerformed(evt);
            }
        });
        availabilityButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                availabilityButtonKeyPressed(evt);
            }
        });

        addButton.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        addButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addButtonKeyPressed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addClient.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameField)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cityField)
                                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(availableLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(119, 119, 119)
                                .addComponent(availabilityButton)
                                .addGap(18, 18, 18)
                                .addComponent(addButton)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availabilityButton)
                    .addComponent(addButton))
                .addGap(80, 80, 80))
        );

        add(jPanel1, java.awt.BorderLayout.LINE_START);

        jPanel2.setBackground(java.awt.Color.darkGray);
        jPanel2.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jPanel2.setMaximumSize(new java.awt.Dimension(450, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(430, 480));
        jPanel2.setPreferredSize(new java.awt.Dimension(450, 480));

        jLabel6.setForeground(java.awt.Color.white);
        jLabel6.setText("Client :");

        editClientList.setMinimumSize(new java.awt.Dimension(300, 27));
        editClientList.setPreferredSize(new java.awt.Dimension(300, 27));

        editSelectClient.setText("Edit Client");
        editSelectClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectClientActionPerformed(evt);
            }
        });
        editSelectClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editSelectClientKeyPressed(evt);
            }
        });

        jLabel7.setForeground(java.awt.Color.white);
        jLabel7.setText("Name :");

        jLabel8.setForeground(java.awt.Color.white);
        jLabel8.setText("Client City :");

        jLabel9.setForeground(java.awt.Color.white);
        jLabel9.setText("Unique Id :");

        editUpdateButton.setText("Update");
        editUpdateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editUpdateButtonActionPerformed(evt);
            }
        });
        editUpdateButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editUpdateButtonKeyPressed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editClient.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(editClientList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(editSelectClient)
                                .addGap(137, 137, 137))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(editUpdateButton)
                                .addGap(147, 147, 147))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(editNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(editCityField)
                            .addComponent(editIdField)))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 390, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(editClientList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(editSelectClient)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(editNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(editCityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(editIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(editUpdateButton)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manageClient.png"))); // NOI18N
        add(jButton1, java.awt.BorderLayout.PAGE_START);

        errorPanel.setBackground(java.awt.Color.darkGray);
        errorPanel.setMinimumSize(new java.awt.Dimension(1200, 30));
        errorPanel.setPreferredSize(new java.awt.Dimension(1200, 30));
        errorPanel.setLayout(new javax.swing.BoxLayout(errorPanel, javax.swing.BoxLayout.LINE_AXIS));

        errorLabel.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        errorLabel.setForeground(java.awt.Color.white);
        errorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        errorLabel.setText("Error Label");
        errorLabel.setMaximumSize(new java.awt.Dimension(1200, 20));
        errorLabel.setMinimumSize(new java.awt.Dimension(1200, 20));
        errorLabel.setPreferredSize(new java.awt.Dimension(1200, 20));
        errorPanel.add(errorLabel);

        add(errorPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    private void availabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availabilityButtonActionPerformed
        availabilty();
    }//GEN-LAST:event_availabilityButtonActionPerformed

    private void availabilityButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_availabilityButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        availabilty();
    }//GEN-LAST:event_availabilityButtonKeyPressed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:

        // TODO add your handling code here:
        add();

    }//GEN-LAST:event_addButtonActionPerformed

    private void addButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        add();
    }//GEN-LAST:event_addButtonKeyPressed

    private void editSelectClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectClientActionPerformed
        // TODO add your handling code here:
        editSelectClient();
    }//GEN-LAST:event_editSelectClientActionPerformed

    private void editSelectClientKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editSelectClientKeyPressed

        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        editSelectClient();
    }//GEN-LAST:event_editSelectClientKeyPressed

    private void editUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUpdateButtonActionPerformed
        // TODO add your handling code here:
        editUpdate();

    }//GEN-LAST:event_editUpdateButtonActionPerformed

    private void editUpdateButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editUpdateButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        editUpdate();
    }//GEN-LAST:event_editUpdateButtonKeyPressed

    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        deleteClient();

    }//GEN-LAST:event_deleteClientButtonActionPerformed

    private void deleteClientButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deleteClientButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        deleteClient();
    }//GEN-LAST:event_deleteClientButtonKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        nameField.requestFocus();
    }//GEN-LAST:event_formFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton availabilityButton;
    private javax.swing.JLabel availableLabel;
    private javax.swing.JTextField cityField;
    private javax.swing.JComboBox clientList;
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JTextField editCityField;
    private javax.swing.JComboBox editClientList;
    private javax.swing.JTextField editIdField;
    private javax.swing.JTextField editNameField;
    private javax.swing.JButton editSelectClient;
    private javax.swing.JButton editUpdateButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel errorPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    protected javax.swing.JTextField nameField;
    // End of variables declaration//GEN-END:variables
}
