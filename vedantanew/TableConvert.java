/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vedantanew;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author boss
 */
public class TableConvert {

    public TableConvert() {
        try {
            Connection conn = DBHelper.initConnection();
            Statement stmt = conn.createStatement();
            PreparedStatement stmt1 = null;
            ResultSet executeQuery = null;
            String query = "",pro="",id="";
            Double rate=0.0,total= 0.0;
            int qty=0;
            conn.setAutoCommit(false);
            String query1 = "insert into billEntry(billno,pid,pname,qty,rate,total)" + " values(?,?,?,?,?,?)";
            stmt1 = conn.prepareStatement(query1);
             for (int i = 406; i <=450 ; i++)
            {
                try
                {
                    query = "Select * from billnum"+i;
                    executeQuery = stmt.executeQuery(query);
                    stmt1.setInt(1, i);
                while(executeQuery.next())
                {
                    id = executeQuery.getString("id");
                    stmt1.setString(2, id);
                    pro = executeQuery.getString("pro");
                    stmt1.setString(3, pro);
                    qty = executeQuery.getInt("qty");
                    stmt1.setDouble(4, qty);
                    rate = executeQuery.getDouble("rate");
                    stmt1.setDouble(5, rate);
                    total =executeQuery.getDouble("total");
                    stmt1.setDouble(6, rate);
                    stmt1.addBatch();
                }
                stmt1.executeBatch();
                System.out.println("Converted "+ i);
                conn.commit();
                }
                catch(Exception ex)
                {
                
                }
                            }
        } catch (SQLException ex) {
            Logger.getLogger(TableConvert.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void main(String[] args) 
    {
        new TableConvert();
    }
    
    
    
}
