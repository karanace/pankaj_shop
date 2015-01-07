package vedantanew;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boss
 */
public  class DBHelper 
{
    public static Connection __Connection = null; 
    
    private DBHelper(){}
    public static Connection  getConnection(){
        if(__Connection == null){
            __Connection = initConnection();
            //__instance.initConnection();
        }
        return __Connection;
    }
    public static Connection initConnection()
    {
        Connection conn = null;
        try {    
            
            Class.forName(Host.JDBCDriver);
            try {
                conn = DriverManager.getConnection(Host.host,Host.user,Host.password);
            } catch (SQLException ex) {
                Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;

    }
    
    public void closeConnection(Connection conn, Statement stmt)
    {
        try {
            conn.close();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
