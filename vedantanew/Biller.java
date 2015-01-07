/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vedantanew;

import java.awt.CardLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author boss
 */
public class Biller extends javax.swing.JPanel {
DefaultTableModel dtm ;
ReportGenNew rg;
DBHelper dbHelper;
Statement stmt ;
int billNo;
Connection conn;
int type;
boolean oldProduct ;
boolean existingClient;
boolean updateCase;
int billNum;
String cid = null;


    /**
     * Creates new form Biller
     */
    public Biller() {
        initComponents();
        resetBiller();
        conn = DBHelper.initConnection();
    try {
        stmt = conn.createStatement();
    } catch (SQLException ex) {
        Logger.getLogger(Biller.class.getName()).log(Level.SEVERE, null, ex);
    }
        setTableModel();
        loadClients();
        loadProducts();
    }
    
    void setTableModel()
    {
        String col[] = {"Id","Product","Qty","Rate","Total"};
        dtm = new DefaultTableModel(col,0);
        displayTable.setModel(dtm);
       
    }
    void loadClients()
    {
        ResultSet executeQuery = null;
    try {
        String query = "Select * from client order by name";
        executeQuery = stmt.executeQuery(query);
        if(executeQuery.first())
            clientList.addItem(executeQuery.getString("name")+" ("+executeQuery.getString("city")+") "+executeQuery.getString("id"));
        while(executeQuery.next())
        {
            clientList.addItem(executeQuery.getString("name")+" ("+executeQuery.getString("city")+") "+executeQuery.getString("id"));
        }
    } catch (SQLException ex) {
        Logger.getLogger(Biller.class.getName()).log(Level.SEVERE, null, ex);
    }
    finally
    {
            try {
                executeQuery.close();
            } catch (SQLException ex) {
                Logger.getLogger(Biller.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    }
    void loadProducts()
    {
    try {
        String query = "Select * from products order by id";
        ResultSet executeQuery = stmt.executeQuery(query);
        if(executeQuery.first())
            productList.addItem(executeQuery.getString("id")+" "+executeQuery.getString("name"));
        while(executeQuery.next())
        {
            productList.addItem(executeQuery.getString("id")+" "+executeQuery.getString("name"));
        } 
    } catch (SQLException ex) {
        Logger.getLogger(Biller.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    public Biller(int billNum)
    {
    try {
        initComponents();
        resetBiller();
        String query = "Select * from billEntry where billno = "+billNum;
       // System.out.println(query);
        conn = DBHelper.initConnection();
        stmt = conn.createStatement();
        setTableModel();
        loadClients();
        loadProducts();
        ResultSet executeQuery = stmt.executeQuery(query);
        Object[] oneRow = new Object[5];

        while(executeQuery.next())
        {
            oneRow[0] = executeQuery.getString("pid");
            oneRow[1] = executeQuery.getString("pname");
            oneRow[2] = executeQuery.getInt("qty");
            oneRow[3] = executeQuery.getDouble("rate");
            oneRow[4] = executeQuery.getDouble("total");
            dtm.addRow(oneRow);
        }
        query = "Select * from transactions where billNo ="+ billNum;
        executeQuery = stmt.executeQuery(query);
        if(executeQuery.first())
        {
            partyDisplay.setText(executeQuery.getString("pname"));
            cityDisplay.setText(executeQuery.getString("pcity"));
            gqtyLabel.setText(executeQuery.getInt("gqty")+"");
            gTotalLabel.setText(executeQuery.getDouble("gtotal")+"");
            if(executeQuery.getString("pid") == null){
                type = 2;
            }
            else{
                type = 1;
                cid = executeQuery.getString("pid");
            }
            CardLayout cl = (CardLayout)(clientCardPanel.getLayout());
            cl.show(clientCardPanel, "displayCard");
            updateCase = true;
            this.billNum = billNum;
        }
        else
        {
            String[] options1 = {"OK"};
            JOptionPane.showMessageDialog(null,"Problem in Updating Bill Num " + billNum , "Error", JOptionPane.ERROR_MESSAGE,null);
        }
        
    } catch (SQLException ex) {
        Logger.getLogger(Biller.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
        private static boolean isInteger(String n) {
            try {
                    Integer.parseInt(n);
                    return true;
            } catch (NumberFormatException nfe) {
                    return false;
            }
        }
        
        private static boolean isDouble(String n) {
		try {
			Double.parseDouble(n);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
        
    void add(){
            String data[] = new String[5];
            Double tot = 0.0;

            String selectedItem="";
            if(oldProduct)
            {
                selectedItem = productList.getSelectedItem().toString();
                data[0] = selectedItem.substring(0,selectedItem.indexOf(" "));
                data[1] = selectedItem.substring(selectedItem.indexOf(" ") +1);
            }
            else
            {
                data[0] = "";
                data[1] = productField.getText();
            }    
            if(data[1].isEmpty())
            productError.setVisible(true);
            qtyError.setVisible(false);
            rateError.setVisible(false);
            data[2] = qtyField.getText();
            data[3] = rateField.getText();
            
            if(isInteger(data[2]) && Integer.parseInt(data[2]) > 0)
            {
                if(isDouble(data[3]) && Double.parseDouble(data[3]) > 0.0)
                {
                    tot = (Integer.parseInt(data[2]) * Double.parseDouble(data[3]));
                    DecimalFormat df = new DecimalFormat("#.00");
                    tot = Math.rint(tot);
                    data[4] = df.format(tot) ;
                    dtm.insertRow(dtm.getRowCount(), data);
                    displayTable.scrollRectToVisible(new Rectangle(displayTable.getCellRect(dtm.getRowCount()-1, 0, true)));
                    gTotalLabel.setText(df.format(Double.parseDouble(gTotalLabel.getText()) + tot) );
                    gqtyLabel.setText(Integer.parseInt(gqtyLabel.getText()) + Integer.parseInt(qtyField.getText()) + "" );
                    productList.requestFocus();
                    productField.setText(null);
                    qtyField.setText(null);
                    rateField.setText(null);
                    oldProduct = true;
                    ((CardLayout)(addProductCardPanel.getLayout())).show(addProductCardPanel,"old");
                    qtyError.setVisible(false);
                    rateError.setVisible(false);
                    errorLabel.setText("No Error At Present");
                    
                }
                else
                {
                    qtyError.setVisible(false);
                    rateError.setVisible(true);
                    
                }
            }    
            else
            {
                qtyError.setVisible(true);
                if(isDouble(data[3]) && Double.parseDouble(data[3]) > 0.0)
                {
                    rateError.setVisible(false);
                    
                }
                    
                else
                {
                    
                    rateError.setVisible(true);
                }
                
            }

        }
    
    
    void deleteEntry()    {
        if(displayTable.getSelectedRow() !=-1)
        {
            DecimalFormat df = new DecimalFormat("0.00"); 
            gqtyLabel.setText(Integer.parseInt(gqtyLabel.getText()) - Integer.parseInt(dtm.getValueAt(displayTable.getSelectedRow(),2).toString()) + "" );
            gTotalLabel.setText(df.format(Double.parseDouble(gTotalLabel.getText()) - Double.parseDouble(dtm.getValueAt(displayTable.getSelectedRow(),4).toString())));
            dtm.removeRow(displayTable.getSelectedRow());
            errorLabel.setText("No Error At Present");
            productList.requestFocus();
        }
        else
        {
            errorLabel.setText("No Row Selected");
        }
        
        
    }
    
    void print(){
            HashMap map = new HashMap();
            Object[] param = new Object[6];
            
            if(existingClient)
            {
                map.put("clientName",partyDisplay.getText());
                map.put("clientCity",cityDisplay.getText());
               if(clientList.getSelectedIndex() != -1)
               {
                    String selectedClient = clientList.getSelectedItem().toString();
                    param[0] = selectedClient.substring(selectedClient.lastIndexOf(")")+2);
                    param[1] = partyDisplay.getText();
                    param[2] = cityDisplay.getText();
                    param[3] = gqtyLabel.getText();
                    param[4] = gTotalLabel.getText();
                    param[5] = type;
               }
               else
               {
                    errorLabel.setText("No Client Selected");
               }
               
            }
            else
            {
                    map.put("clientName",partyDisplay.getText());
                    map.put("clientCity",cityDisplay.getText());
                    param[0] = "";
                    param[1] = partyDisplay.getText();
                    param[2] = cityDisplay.getText();
                    param[3] = gqtyLabel.getText();
                    param[4] = gTotalLabel.getText();
                    param[5] = type;
                
            }
                
                if(updateCase){
                    rg = new ReportGenNew(billNum);
                    param[0] = cid;
                }
                else
                rg = new ReportGenNew();
                rg.showReport(dtm,map,conn,param);
                resetBiller();
}
    
    void resetBiller()
    {
        updateCase = false;
        rateError.setVisible(false);
        qtyError.setVisible(false);
        productError.setVisible(false);
        nameError.setVisible(false);
        cityError.setVisible(false);
        partyDisplay.setText("");
        cityDisplay.setText("");
        errorLabel.setText("No Error At Present");
        if(dtm !=null)
        dtm.setRowCount(0);
        ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"existingCard");
        ((CardLayout)(addProductCardPanel.getLayout())).show(addProductCardPanel,"old");
        oldProduct = true;
        type = 0;
        clientList.requestFocus();
        existingClient = true;
        
        gTotalLabel.setText("0");
        gqtyLabel.setText("0");

    }
    void existingOk()
    {
        if(clientList.getSelectedIndex() !=-1)
        {
            String selectedClient = clientList.getSelectedItem().toString();
            String partyName =  selectedClient.substring(0,selectedClient.lastIndexOf("("));
            String cityName = selectedClient.substring(selectedClient.lastIndexOf("(")+1,selectedClient.lastIndexOf(")"));
            cid = selectedClient.substring(selectedClient.lastIndexOf(")")+2);
            partyDisplay.setText(partyName);
            cityDisplay.setText(cityName);
            type = 1;
           ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"displayCard");
            productList.requestFocus();
         
        }
        else
        {
            errorLabel.setText("No Client Selected");
            type = 0;
        }
    }
    
    
void toggle(){
            qtyError.setVisible(false);
            rateError.setVisible(false);
            productError.setVisible(false);
        if(oldProduct)
        {
            oldProduct = false;
            toggleButton.setText("Old Product");
            
           ((CardLayout)(addProductCardPanel.getLayout())).show(addProductCardPanel,"new");
           productField.requestFocus();
           
        }
        else
        {
            oldProduct = true;
            toggleButton.setText("New Product");
            ((CardLayout)(addProductCardPanel.getLayout())).show(addProductCardPanel,"old");
            oldProductCard.requestFocus();
            productList.requestFocus();
        }
}
    
void newClient(){
        ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"newCard");
        newClientName.requestFocus();
        
}

void newOk()    {
        if(newClientName.getText().isEmpty())
        {
            nameError.setVisible(true);
        }
            
        else
        {
            partyDisplay.setText(newClientName.getText());
            nameError.setVisible(false);
        }
        
        if(newClientCity.getText().isEmpty())
        {   
            cityError.setVisible(true);
 
        }
            
        else
        {
            cityDisplay.setText(newClientCity.getText());
            cityError.setVisible(false);
            type = 0;
        }
        if(newClientName.getText().isEmpty() || newClientCity.getText().isEmpty())
        type = 0;
        else
        {
            type = 2;
            ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"displayCard");

        }
            
        productList.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        topPanel = new javax.swing.JPanel();
        infoPanel = new javax.swing.JPanel();
        clientCardPanel = new javax.swing.JPanel();
        existingClientPanel = new javax.swing.JPanel();
        existingClientLabel = new javax.swing.JLabel();
        clientList = new javax.swing.JComboBox();
        existingOKButton = new javax.swing.JButton();
        newClientButton = new javax.swing.JButton();
        displayCard = new javax.swing.JPanel();
        partyDisplay = new javax.swing.JLabel();
        cityDisplay = new javax.swing.JLabel();
        editNameButton = new javax.swing.JButton();
        newClientPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        newClientName = new javax.swing.JTextField();
        nameError = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        newClientCity = new javax.swing.JTextField();
        cityError = new javax.swing.JLabel();
        newButton = new javax.swing.JButton();
        existingClientButton = new javax.swing.JButton();
        addProductPanel1 = new javax.swing.JPanel();
        addProductCardPanel = new javax.swing.JPanel();
        oldProductCard = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        productList = new javax.swing.JComboBox();
        newProductCard = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        productField = new javax.swing.JTextField();
        productError = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        qtyField = new javax.swing.JTextField();
        qtyError = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rateField = new javax.swing.JTextField();
        rateError = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        toggleButton = new javax.swing.JButton();
        bottomPanel = new javax.swing.JPanel();
        btnPanel = new javax.swing.JPanel();
        resetButton = new javax.swing.JButton();
        deleteRowButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        gqtyLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        gTotalLabel = new javax.swing.JLabel();
        errorPanel = new javax.swing.JPanel();
        errorLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        displayTable = new javax.swing.JTable();

        setMinimumSize(new java.awt.Dimension(1200, 480));
        setPreferredSize(new java.awt.Dimension(1200, 480));
        setLayout(new java.awt.BorderLayout());

        topPanel.setLayout(new java.awt.BorderLayout());

        infoPanel.setLayout(new java.awt.BorderLayout());

        clientCardPanel.setBackground(java.awt.Color.orange);
        clientCardPanel.setMinimumSize(new java.awt.Dimension(1200, 40));
        clientCardPanel.setPreferredSize(new java.awt.Dimension(1200, 40));
        clientCardPanel.setLayout(new java.awt.CardLayout());

        existingClientPanel.setBackground(java.awt.Color.darkGray);
        existingClientPanel.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        existingClientPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        existingClientPanel.setMinimumSize(new java.awt.Dimension(1200, 40));
        existingClientPanel.setPreferredSize(new java.awt.Dimension(1200, 40));
        existingClientPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 5));

        existingClientLabel.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        existingClientLabel.setForeground(java.awt.Color.white);
        existingClientLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        existingClientLabel.setText("Existing Client");
        existingClientLabel.setAlignmentY(0.0F);
        existingClientLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        existingClientPanel.add(existingClientLabel);

        clientList.setMinimumSize(new java.awt.Dimension(400, 27));
        clientList.setPreferredSize(new java.awt.Dimension(500, 27));
        existingClientPanel.add(clientList);

        existingOKButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        existingOKButton.setText("OK");
        existingOKButton.setMaximumSize(new java.awt.Dimension(100, 30));
        existingOKButton.setMinimumSize(new java.awt.Dimension(50, 30));
        existingOKButton.setPreferredSize(new java.awt.Dimension(100, 30));
        existingOKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                existingOKButtonActionPerformed(evt);
            }
        });
        existingOKButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                existingOKButtonKeyPressed(evt);
            }
        });
        existingClientPanel.add(existingOKButton);

        newClientButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        newClientButton.setText("New Client");
        newClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newClientButtonActionPerformed(evt);
            }
        });
        newClientButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newClientButtonKeyPressed(evt);
            }
        });
        existingClientPanel.add(newClientButton);

        clientCardPanel.add(existingClientPanel, "existingCard");

        displayCard.setBackground(java.awt.Color.darkGray);
        displayCard.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        displayCard.setMinimumSize(new java.awt.Dimension(1200, 32));
        displayCard.setPreferredSize(new java.awt.Dimension(1200, 40));
        displayCard.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 5));

        partyDisplay.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        partyDisplay.setForeground(new java.awt.Color(255, 255, 255));
        partyDisplay.setText("Party Name");
        partyDisplay.setMinimumSize(new java.awt.Dimension(250, 21));
        partyDisplay.setPreferredSize(new java.awt.Dimension(250, 21));
        displayCard.add(partyDisplay);

        cityDisplay.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        cityDisplay.setForeground(new java.awt.Color(255, 255, 255));
        cityDisplay.setText("City");
        cityDisplay.setMinimumSize(new java.awt.Dimension(250, 21));
        cityDisplay.setPreferredSize(new java.awt.Dimension(250, 21));
        displayCard.add(cityDisplay);

        editNameButton.setText("Edit");
        editNameButton.setMaximumSize(null);
        editNameButton.setMinimumSize(new java.awt.Dimension(100, 29));
        editNameButton.setPreferredSize(new java.awt.Dimension(100, 29));
        editNameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNameButtonActionPerformed(evt);
            }
        });
        displayCard.add(editNameButton);

        clientCardPanel.add(displayCard, "displayCard");

        newClientPanel.setBackground(java.awt.Color.darkGray);
        newClientPanel.setEnabled(false);
        newClientPanel.setMinimumSize(new java.awt.Dimension(1200, 40));
        newClientPanel.setPreferredSize(new java.awt.Dimension(1200, 40));
        newClientPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 5));

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel2.setForeground(java.awt.Color.white);
        jLabel2.setText("Name :");
        newClientPanel.add(jLabel2);

        newClientName.setMinimumSize(new java.awt.Dimension(250, 27));
        newClientName.setPreferredSize(new java.awt.Dimension(350, 27));
        newClientPanel.add(newClientName);

        nameError.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        nameError.setForeground(java.awt.Color.red);
        nameError.setText("X");
        newClientPanel.add(nameError);

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel3.setForeground(java.awt.Color.white);
        jLabel3.setText("City :");
        newClientPanel.add(jLabel3);

        newClientCity.setMinimumSize(new java.awt.Dimension(200, 27));
        newClientCity.setPreferredSize(new java.awt.Dimension(300, 27));
        newClientPanel.add(newClientCity);

        cityError.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        cityError.setForeground(java.awt.Color.red);
        cityError.setText("X");
        newClientPanel.add(cityError);

        newButton.setText("OK");
        newButton.setMaximumSize(new java.awt.Dimension(34, 30));
        newButton.setMinimumSize(new java.awt.Dimension(100, 30));
        newButton.setPreferredSize(new java.awt.Dimension(100, 30));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        newButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                newButtonKeyPressed(evt);
            }
        });
        newClientPanel.add(newButton);

        existingClientButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        existingClientButton.setText("Existing Client");
        existingClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                existingClientButtonActionPerformed(evt);
            }
        });
        existingClientButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                existingClientButtonKeyPressed(evt);
            }
        });
        newClientPanel.add(existingClientButton);

        clientCardPanel.add(newClientPanel, "newCard");

        infoPanel.add(clientCardPanel, java.awt.BorderLayout.NORTH);

        topPanel.add(infoPanel, java.awt.BorderLayout.NORTH);

        addProductPanel1.setBackground(java.awt.Color.darkGray);
        addProductPanel1.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        addProductPanel1.setMinimumSize(new java.awt.Dimension(1200, 50));
        addProductPanel1.setPreferredSize(new java.awt.Dimension(1200, 50));
        addProductPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 14, 5));

        addProductCardPanel.setBackground(java.awt.Color.darkGray);
        addProductCardPanel.setLayout(new java.awt.CardLayout());

        oldProductCard.setBackground(java.awt.Color.darkGray);
        oldProductCard.setMinimumSize(new java.awt.Dimension(590, 35));
        oldProductCard.setPreferredSize(new java.awt.Dimension(598, 35));
        oldProductCard.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel4.setForeground(java.awt.Color.white);
        jLabel4.setText("Old Product");
        oldProductCard.add(jLabel4);

        productList.setMinimumSize(new java.awt.Dimension(400, 27));
        productList.setPreferredSize(new java.awt.Dimension(400, 27));
        oldProductCard.add(productList);

        addProductCardPanel.add(oldProductCard, "old");

        newProductCard.setBackground(java.awt.Color.darkGray);
        newProductCard.setMinimumSize(new java.awt.Dimension(634, 35));
        newProductCard.setPreferredSize(new java.awt.Dimension(562, 35));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5);
        flowLayout1.setAlignOnBaseline(true);
        newProductCard.setLayout(flowLayout1);

        jLabel8.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel8.setForeground(java.awt.Color.white);
        jLabel8.setText("New Product  ");
        newProductCard.add(jLabel8);

        productField.setMinimumSize(new java.awt.Dimension(400, 27));
        productField.setPreferredSize(new java.awt.Dimension(400, 27));
        productField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productFieldActionPerformed(evt);
            }
        });
        newProductCard.add(productField);

        productError.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        productError.setForeground(java.awt.Color.red);
        productError.setText("X");
        newProductCard.add(productError);

        addProductCardPanel.add(newProductCard, "new");

        addProductPanel1.add(addProductCardPanel);

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel5.setForeground(java.awt.Color.white);
        jLabel5.setText("Quantity");
        addProductPanel1.add(jLabel5);

        qtyField.setMinimumSize(new java.awt.Dimension(100, 27));
        qtyField.setPreferredSize(new java.awt.Dimension(100, 27));
        addProductPanel1.add(qtyField);

        qtyError.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        qtyError.setForeground(java.awt.Color.red);
        qtyError.setText("X");
        qtyError.setMaximumSize(new java.awt.Dimension(10, 15));
        qtyError.setMinimumSize(new java.awt.Dimension(10, 15));
        qtyError.setPreferredSize(new java.awt.Dimension(10, 15));
        addProductPanel1.add(qtyError);

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel6.setForeground(java.awt.Color.white);
        jLabel6.setText("Rate");
        addProductPanel1.add(jLabel6);

        rateField.setMinimumSize(new java.awt.Dimension(100, 27));
        rateField.setPreferredSize(new java.awt.Dimension(100, 27));
        addProductPanel1.add(rateField);

        rateError.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        rateError.setForeground(java.awt.Color.red);
        rateError.setText("X");
        rateError.setMaximumSize(new java.awt.Dimension(10, 15));
        rateError.setMinimumSize(new java.awt.Dimension(10, 15));
        rateError.setPreferredSize(new java.awt.Dimension(10, 15));
        addProductPanel1.add(rateError);

        addButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
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
        addProductPanel1.add(addButton);

        toggleButton.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        toggleButton.setText("New Product");
        toggleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleButtonMouseClicked(evt);
            }
        });
        toggleButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                toggleButtonKeyPressed(evt);
            }
        });
        addProductPanel1.add(toggleButton);

        topPanel.add(addProductPanel1, java.awt.BorderLayout.CENTER);

        add(topPanel, java.awt.BorderLayout.NORTH);

        bottomPanel.setLayout(new java.awt.BorderLayout());

        btnPanel.setBackground(java.awt.Color.darkGray);
        btnPanel.setBorder(new javax.swing.border.LineBorder(java.awt.Color.white, 2, true));
        btnPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 30, 5));

        resetButton.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        resetButton.setText("Reset");
        resetButton.setMinimumSize(new java.awt.Dimension(125, 31));
        resetButton.setPreferredSize(new java.awt.Dimension(125, 31));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        resetButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                resetButtonKeyPressed(evt);
            }
        });
        btnPanel.add(resetButton);

        deleteRowButton.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        deleteRowButton.setText("Delete Entry");
        deleteRowButton.setMinimumSize(new java.awt.Dimension(150, 31));
        deleteRowButton.setPreferredSize(new java.awt.Dimension(150, 31));
        deleteRowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowButtonActionPerformed(evt);
            }
        });
        deleteRowButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                deleteRowButtonKeyPressed(evt);
            }
        });
        btnPanel.add(deleteRowButton);

        printButton.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        printButton.setText("Print");
        printButton.setMinimumSize(new java.awt.Dimension(125, 31));
        printButton.setPreferredSize(new java.awt.Dimension(125, 31));
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });
        printButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                printButtonKeyPressed(evt);
            }
        });
        btnPanel.add(printButton);

        jLabel9.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel9.setForeground(java.awt.Color.white);
        jLabel9.setText("Total Quantity :");
        jLabel9.setMinimumSize(new java.awt.Dimension(150, 25));
        jLabel9.setPreferredSize(new java.awt.Dimension(150, 25));
        btnPanel.add(jLabel9);

        gqtyLabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        gqtyLabel.setForeground(java.awt.Color.white);
        gqtyLabel.setText("0");
        gqtyLabel.setMinimumSize(new java.awt.Dimension(150, 28));
        gqtyLabel.setPreferredSize(new java.awt.Dimension(150, 28));
        btnPanel.add(gqtyLabel);

        jLabel10.setFont(new java.awt.Font("Ubuntu", 1, 16)); // NOI18N
        jLabel10.setForeground(java.awt.Color.white);
        jLabel10.setText("Grand Total :");
        jLabel10.setMinimumSize(new java.awt.Dimension(150, 25));
        jLabel10.setPreferredSize(new java.awt.Dimension(150, 25));
        btnPanel.add(jLabel10);

        gTotalLabel.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        gTotalLabel.setForeground(java.awt.Color.white);
        gTotalLabel.setText("0");
        gTotalLabel.setMinimumSize(new java.awt.Dimension(150, 28));
        gTotalLabel.setPreferredSize(new java.awt.Dimension(150, 28));
        btnPanel.add(gTotalLabel);

        bottomPanel.add(btnPanel, java.awt.BorderLayout.CENTER);

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

        bottomPanel.add(errorPanel, java.awt.BorderLayout.SOUTH);

        add(bottomPanel, java.awt.BorderLayout.SOUTH);

        displayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(displayTable);

        add(jScrollPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void existingOKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_existingOKButtonActionPerformed
        // TODO add your handling code here:
        existingOk();
    }//GEN-LAST:event_existingOKButtonActionPerformed

    private void existingOKButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_existingOKButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        {
            existingOk();
        }
    }//GEN-LAST:event_existingOKButtonKeyPressed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        // TODO add your handling code here:
//        errorLabel.setText(null);
        newOk();
    }//GEN-LAST:event_newButtonActionPerformed

    private void newButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        newOk();
    }//GEN-LAST:event_newButtonKeyPressed

    private void newClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newClientButtonActionPerformed
        // TODO add your handling code here:
        newClient();
    }//GEN-LAST:event_newClientButtonActionPerformed

    private void newClientButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newClientButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        newClient();
    }//GEN-LAST:event_newClientButtonKeyPressed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
//        errorLabel.setText(null);
        add();
    }//GEN-LAST:event_addButtonActionPerformed

    private void addButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        add();
    }//GEN-LAST:event_addButtonKeyPressed

    private void productFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_productFieldActionPerformed

    private void toggleButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_toggleButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        toggle();
    }//GEN-LAST:event_toggleButtonKeyPressed

    private void deleteRowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowButtonActionPerformed
        // TODO add your handling code here:
        deleteEntry();
    }//GEN-LAST:event_deleteRowButtonActionPerformed

    private void deleteRowButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_deleteRowButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        deleteEntry();
    }//GEN-LAST:event_deleteRowButtonKeyPressed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        // TODO add your handling code here:
        if(dtm.getRowCount() == 0)
        errorLabel.setText("No Items in Bill");
        else
        {
            if(type !=0)
            {
                int dialogButton = JOptionPane.showConfirmDialog(null, "Generate Bill", "Confirmation", JOptionPane.YES_NO_OPTION);
                if(dialogButton == JOptionPane.YES_OPTION)
                {
                    print();
                }

            }
            else
                {
            errorLabel.setText("Bill without Client, Select or Enter Client ");
        }
    }//GEN-LAST:event_printButtonActionPerformed
    }
    private void printButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_printButtonKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        print();
    }//GEN-LAST:event_printButtonKeyPressed

    private void existingClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_existingClientButtonActionPerformed
        // TODO add your handling code here:
        ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"existingCard");
        
    }//GEN-LAST:event_existingClientButtonActionPerformed

    private void existingClientButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_existingClientButtonKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_existingClientButtonKeyPressed

    private void editNameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNameButtonActionPerformed
        // TODO add your handling code here:
        type = 0;
        partyDisplay.setText("");
        cityDisplay.setText("");
        ((CardLayout)(clientCardPanel.getLayout())).show(clientCardPanel,"existingCard");
    }//GEN-LAST:event_editNameButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // TODO add your handling code here:
    int dialogButton = JOptionPane.showConfirmDialog(null, "Are you Sure ? Reset Biller", "Confirmation", JOptionPane.YES_NO_OPTION);
    if(dialogButton == JOptionPane.YES_OPTION)
    {
        resetBiller();
    }
        
    }//GEN-LAST:event_resetButtonActionPerformed

    private void resetButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_resetButtonKeyPressed
        // TODO add your handling code here:
       if(evt.getKeyCode() == KeyEvent.VK_ENTER)
       {
         int dialogButton = JOptionPane.showConfirmDialog(null, "Are you Sure ? Reset Biller", "Confirmation", JOptionPane.YES_NO_OPTION);
         if(dialogButton == JOptionPane.YES_OPTION)
         {
             resetBiller();
         }
       }
     
    }//GEN-LAST:event_resetButtonKeyPressed

    private void toggleButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_toggleButtonMouseClicked
        // TODO add your handling code here:
        toggle();
    }//GEN-LAST:event_toggleButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel addProductCardPanel;
    private javax.swing.JPanel addProductPanel1;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JPanel btnPanel;
    private javax.swing.JLabel cityDisplay;
    private javax.swing.JLabel cityError;
    private javax.swing.JPanel clientCardPanel;
    private javax.swing.JComboBox clientList;
    private javax.swing.JButton deleteRowButton;
    private javax.swing.JPanel displayCard;
    private javax.swing.JTable displayTable;
    private javax.swing.JButton editNameButton;
    private javax.swing.JLabel errorLabel;
    private javax.swing.JPanel errorPanel;
    private javax.swing.JButton existingClientButton;
    private javax.swing.JLabel existingClientLabel;
    private javax.swing.JPanel existingClientPanel;
    private javax.swing.JButton existingOKButton;
    private javax.swing.JLabel gTotalLabel;
    private javax.swing.JLabel gqtyLabel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel nameError;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newClientButton;
    private javax.swing.JTextField newClientCity;
    private javax.swing.JTextField newClientName;
    private javax.swing.JPanel newClientPanel;
    private javax.swing.JPanel newProductCard;
    private javax.swing.JPanel oldProductCard;
    private javax.swing.JLabel partyDisplay;
    private javax.swing.JButton printButton;
    private javax.swing.JLabel productError;
    private javax.swing.JTextField productField;
    private javax.swing.JComboBox productList;
    private javax.swing.JLabel qtyError;
    private javax.swing.JTextField qtyField;
    private javax.swing.JLabel rateError;
    private javax.swing.JTextField rateField;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton toggleButton;
    private javax.swing.JPanel topPanel;
    // End of variables declaration//GEN-END:variables
}
