package vedantanew;


import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
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
public class ProductManage extends javax.swing.JPanel {
    private Connection conn;
    private Statement stmt;

    /**
     * Creates new form ProductManage
     */
    public ProductManage() {
      initComponents();
        availableLabel.setVisible(false);
        initConnection();
        editUpdateButton.setEnabled(false);
        editIdField.setEditable(false);
        errorLabel.setText("No Error at present");
        updateProductList();      
        
        
        
        
    }

    //My Code Starts
    
public void initConnection(){
        conn = DBHelper.getConnection();
}
    
private void updateProductList() {                                                    
        try {
            // TODO add your handling code here:
            String query = "select * from products order by id";
            stmt = conn.createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            String product = "";
            while(executeQuery.next())
            {
                product = executeQuery.getString("id") + " "+executeQuery.getString("name");
                productList.addItem(product);
                editProductList.addItem(product);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
void deleteProduct(){
    if(productList.getSelectedIndex() !=-1)
        {
            String message = "Are you Sure ? Delete Product "+productList.getSelectedItem().toString();
                int dialogButton = JOptionPane.showConfirmDialog(null,message, "Delete", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION)
                {
                    try {
            // TODO add your handling code here:
                        String selectedProduct = productList.getSelectedItem().toString();
                        String id = selectedProduct.substring(0,selectedProduct.indexOf(" "));
                        int index = productList.getSelectedIndex();
                        productList.removeItemAt(index);
                        editProductList.removeItemAt(index);
                        String query = "delete from products where id = '"+ id+"'";
                        stmt = conn.createStatement();
                        int executeUpdate = stmt.executeUpdate(query);
                        errorLabel.setText("No Error at Present");
            
                        } catch (SQLException ex) {
                        Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
       }
        else
        {
            errorLabel.setText("No Product Selected");
        }
}

void editUpdate()    {
        String query = null;
        if(editNameField.getText().isEmpty())
        {
            errorLabel.setText("Name field empty ");
        }
        else
        {
            try {
                String message = "Set Name : "+editNameField.getText()+"\nFor Id :" + editIdField.getText();
                int dialogButton = JOptionPane.showConfirmDialog(null,message, "Update", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION)
                {
                    query = "Update products set name = '" + editNameField.getText() + "' where id = '"+ editIdField.getText()+"'";
                    stmt = conn.createStatement();
                    int executeUpdate = stmt.executeUpdate(query);
                    productList.removeAllItems();
                    editProductList.removeAllItems();
                    updateProductList();
                    
                    editNameField.setText(null);
                    editIdField.setText(null);
                    editUpdateButton.setEnabled(false);
                    editProductList.requestFocus();
                    errorLabel.setText("No Error at present");
               }
                
            } catch (SQLException ex) {
                Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
            }
              
        }
    }
        
void editSelectProduct()    {
        if(editProductList.getSelectedIndex() !=-1)
        {
            try {
            // TODO add your handling code here:
            String selectedProduct = editProductList.getSelectedItem().toString();
            String id = selectedProduct.substring(0,selectedProduct.indexOf(" "));
            //clientList.removeItemAt(clientList.getSelectedIndex());
            String query = "select * from products where id = '"+ id+"'";
            stmt = conn.createStatement();
            ResultSet executeQuery = stmt.executeQuery(query);
            if(executeQuery.first())
            {
                editIdField.setText(executeQuery.getString("id"));
                editNameField.setText(executeQuery.getString("name"));
                editUpdateButton.setEnabled(true);
            }
            else
            editUpdateButton.setEnabled(false);
        } catch (SQLException ex) {
            editUpdateButton.setEnabled(false);
            Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        else
        {
            errorLabel.setText("No Product Selected");
            
        }
    }
void add(){
    if(idField.getText().isEmpty() || nameField.getText().isEmpty())
        {
            
            errorLabel.setText("Enter Both Fields");
            
        }
        else
        {
            try {            
                stmt = conn.createStatement();
                String trimmedString = idField.getText().replaceAll("\\s+","");
                String query = "select * from products where id = '"+ trimmedString + "'";
                ResultSet executeQuery = stmt.executeQuery(query);
                if(executeQuery.first() == false)
                {
                    availableLabel.setVisible(false);
                    query = "insert into products values('"+ trimmedString + "','"+ nameField.getText()+"',0)"; 
                    int executeUpdate = stmt.executeUpdate(query);
                    productList.addItem(trimmedString +" " + nameField.getText());
                    editProductList.addItem(trimmedString +" " + nameField.getText());
                    availableLabel.setVisible(false);
                    errorLabel.setText("No Error at present");
                    idField.setText("");
                    nameField.setText("");
                }
                else
                {
                    availableLabel.setText("Not Available");
                    availableLabel.setForeground(Color.RED);
                    availableLabel.setVisible(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
}
void availability()    {
    if(idField.getText().isEmpty() == false)
    {
        try {        
            // TODO add your handling code here:
            
            stmt = conn.createStatement();
            String trimmedString = idField.getText().replaceAll("\\s+","");
            String query = "select * from products where id = '"+ trimmedString + "'";
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
            Logger.getLogger(ProductManage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    else
    {
        availableLabel.setVisible(false);
        errorLabel.setText("Id Field Empty");
    }
        
    }
    
    //MyCode Ends
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        editProductList = new javax.swing.JComboBox();
        editSelectProduct = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        editNameField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        editIdField = new javax.swing.JTextField();
        editUpdateButton = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        availableLabel = new javax.swing.JLabel();
        availabilityButton = new javax.swing.JButton();
        addButton = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        productList = new javax.swing.JComboBox();
        deleteProductButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        errorPanel = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();

        setMinimumSize(new java.awt.Dimension(1200, 480));
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
        jPanel3.setPreferredSize(new java.awt.Dimension(430, 480));

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("Product :");

        editSelectProduct.setText("Edit Product");
        editSelectProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectProductActionPerformed(evt);
            }
        });
        editSelectProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                editSelectProductKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel7.setForeground(java.awt.Color.white);
        jLabel7.setText("Name :");

        editNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNameFieldActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel10.setForeground(java.awt.Color.white);
        jLabel10.setText("Unique Id :");

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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/editPro.png"))); // NOI18N
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
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(editProductList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editNameField)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(editIdField, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(79, 79, 79))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(editSelectProduct)
                        .addGap(137, 137, 137))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(editUpdateButton)
                        .addGap(149, 149, 149)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(editProductList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(editSelectProduct)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(32, 32, 32)
                .addComponent(editUpdateButton)
                .addContainerGap(90, Short.MAX_VALUE))
        );

        add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel1.setBackground(java.awt.Color.darkGray);
        jPanel1.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jPanel1.setMaximumSize(new java.awt.Dimension(450, 32767));
        jPanel1.setMinimumSize(new java.awt.Dimension(430, 480));
        jPanel1.setPreferredSize(new java.awt.Dimension(430, 480));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Name :");

        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel4.setForeground(java.awt.Color.white);
        jLabel4.setText(" Id :");

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

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/addPro.png"))); // NOI18N
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField))
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(35, 35, 35)
                                .addComponent(idField))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(availableLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(availabilityButton)
                                        .addGap(44, 44, 44)
                                        .addComponent(addButton)))))
                        .addGap(94, 94, 94)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel2))
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel4)))
                .addGap(1, 1, 1)
                .addComponent(availableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availabilityButton)
                    .addComponent(addButton))
                .addGap(185, 185, 185))
        );

        add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(java.awt.Color.darkGray);
        jPanel2.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        jPanel2.setMaximumSize(new java.awt.Dimension(450, 32767));
        jPanel2.setMinimumSize(new java.awt.Dimension(430, 480));
        jPanel2.setPreferredSize(new java.awt.Dimension(430, 480));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel1.setForeground(java.awt.Color.white);
        jLabel1.setText("Product :");

        deleteProductButton.setText("Delete Product");
        deleteProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteProductButtonActionPerformed(evt);
            }
        });
        deleteProductButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deleteProductButtonKeyPressed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/deletePro.png"))); // NOI18N
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
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(productList, 0, 320, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(deleteProductButton)
                                .addGap(119, 119, 119)))
                        .addGap(8, 8, 8))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(productList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(deleteProductButton)
                .addContainerGap(262, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.EAST);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/manageProducts.png"))); // NOI18N
        jButton1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton1FocusGained(evt);
            }
        });
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

    
    private void deleteProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteProductButtonActionPerformed
        deleteProduct();

    }//GEN-LAST:event_deleteProductButtonActionPerformed

    
    private void deleteProductButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deleteProductButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        deleteProduct();
    }//GEN-LAST:event_deleteProductButtonKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void editSelectProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectProductActionPerformed
        // TODO add your handling code here:
        editSelectProduct();
    }//GEN-LAST:event_editSelectProductActionPerformed

    private void editSelectProductKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editSelectProductKeyPressed

        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        editSelectProduct();
    }//GEN-LAST:event_editSelectProductKeyPressed

    private void editUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editUpdateButtonActionPerformed
        // TODO add your handling code here:
        editUpdate();
    }//GEN-LAST:event_editUpdateButtonActionPerformed

    private void editUpdateButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editUpdateButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        editUpdate();
    }//GEN-LAST:event_editUpdateButtonKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void addButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        add();
    }//GEN-LAST:event_addButtonKeyPressed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        add();
    }//GEN-LAST:event_addButtonActionPerformed

    private void availabilityButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_availabilityButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        availability();
    }//GEN-LAST:event_availabilityButtonKeyPressed

    private void availabilityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_availabilityButtonActionPerformed
        availability();
    }//GEN-LAST:event_availabilityButtonActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

    private void editNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editNameFieldActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        // TODO add your handling code here:
        boolean requestFocusInWindow = nameField.requestFocusInWindow(); 
    }//GEN-LAST:event_formFocusGained

    private void jButton1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton1FocusGained
        // TODO add your handling code here:
        //nameField.requestFocusInWindow();
    }//GEN-LAST:event_jButton1FocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton availabilityButton;
    private javax.swing.JLabel availableLabel;
    private javax.swing.JButton deleteProductButton;
    private javax.swing.JTextField editIdField;
    private javax.swing.JTextField editNameField;
    private javax.swing.JComboBox editProductList;
    private javax.swing.JButton editSelectProduct;
    private javax.swing.JButton editUpdateButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel errorPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    protected javax.swing.JTextField nameField;
    private javax.swing.JComboBox productList;
    // End of variables declaration//GEN-END:variables
}
