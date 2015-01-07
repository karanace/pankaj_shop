package vedantanew;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author boss
 */
public class BillPrint
{
    Connection conn;
    PreparedStatement stmt;
    Statement stmt1;
    private JasperReport jasperReport;

    public BillPrint() throws SQLException {
        conn = DBHelper.initConnection();
        stmt1 = conn.createStatement();
    }
    
    public void showReport(HashMap map)
    {
        
        try {
            String reportName = "bill.jasper";
            
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportName));

            JasperPrint jp = null;
            try {
                jp = JasperFillManager.fillReport(jasperReport,map,conn);
            } catch (JRException ex) {
                Logger.getLogger(BillPrint.class.getName()).log(Level.SEVERE, null, ex);
            }
            JasperViewer.viewReport(jp,false);
           } catch (Exception ex) {
            Logger.getLogger(BillPrint.class.getName()).log(Level.SEVERE, null, ex);
        }
            
  }
    
    
  
}