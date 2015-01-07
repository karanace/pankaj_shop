package vedantanew;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
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
public class ReportGenNew
{
    PreparedStatement stmt;
    Statement stmt1;
    private JasperReport jasperReport;
    int billNum ;
    boolean updateCase;
    Connection conn;
    
    public ReportGenNew()
    {
        conn = DBHelper.initConnection();
        updateCase = false;
    }
    public ReportGenNew(int billNum)
    {
        conn = DBHelper.initConnection();
        this.billNum = billNum;
        updateCase = true;
    }
    public void showReport(DefaultTableModel dtm,HashMap map,Connection conn,Object[] param)
    {
        try {
                String query = null;
                int billNum;
                stmt1 = conn.createStatement();
                if(updateCase)
                {
                    billNum = this.billNum;
                    query = "delete from billEntry where billNo = "+ billNum;
                    stmt1.executeUpdate(query);
                    query = "delete from transactions where billNo = "+ billNum;
                    stmt1.executeUpdate(query);
                }
                else
                {
                    query = "select count from counter where id = 'billNo' ";
                    ResultSet executeQuery = stmt1.executeQuery(query);
                    executeQuery.first();
                    billNum = executeQuery.getInt("count");
                }
                               
                  String appendQuery = "";
            
            appendQuery = "insert into billEntry(Billno,pid,pname,qty,rate,total) values(?,?,?,?,?,?)" ;
            stmt = conn.prepareStatement(appendQuery);
            Vector dataVector = dtm.getDataVector();
            conn.setAutoCommit(false);
            if(dtm.getRowCount() !=0)
            {
                int i;
                for ( i = 0; i < dtm.getRowCount(); i++)
                {   stmt.setString(1,billNum +"");
                    for (int j = 0; j < 5; j++) 
                    {
                        stmt.setString(j+2,((Vector)dataVector.elementAt(i)).elementAt(j).toString() );
                    }
                    stmt.addBatch();
                }
                stmt.executeBatch();
                conn.commit();
            }
            conn.setAutoCommit(true);
            //String property = System.getProperty("");
            String reportName = null;
            map.put("billNo",billNum+""); 
            if(!updateCase)
            {
            reportName = "vedanta.jasper";
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportName));

            JasperPrint jp = null;
            try {
                jp = JasperFillManager.fillReport(jasperReport,map,conn);
            } catch (JRException ex) {
                Logger.getLogger(ReportGenNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            JasperViewer.viewReport(jp, false);
            query = "update counter set count = count+1 where id = 'billNo'";
            stmt1.executeUpdate(query);            
            }
            else
            {
                reportName = "bill.jasper";
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String format = sdf.format(Calendar.getInstance().getTime());
                map.put("billDate", format);
                JasperReport jasperReport = (JasperReport)JRLoader.loadObject(new File(reportName));

            JasperPrint jp = null;
            try {
                jp = JasperFillManager.fillReport(jasperReport,map,conn);
            } catch (JRException ex) {
                Logger.getLogger(ReportGenNew.class.getName()).log(Level.SEVERE, null, ex);
            }
            JasperViewer.viewReport(jp, false);
            }
                         
                if(param[5].toString().equals("1"))
                {
                    query = "insert into transactions(billNo,pid,pname,pcity,gqty,gtotal,billDate)" 
                        + " values(?,?,?,?,?,?,?)";
                    PreparedStatement prepareStatement = conn.prepareStatement(query);
                    prepareStatement.setInt(1, billNum);
                    prepareStatement.setString(2,param[0].toString() );
                    prepareStatement.setString(3,param[1].toString() );
                    prepareStatement.setString(4,param[2].toString() );
                    prepareStatement.setInt(5, Integer.parseInt(param[3].toString()));
                    prepareStatement.setDouble(6, Double.parseDouble(param[4].toString()));
                    Calendar calenderInstance = Calendar.getInstance();
                    java.sql.Date date = new java.sql.Date(calenderInstance.getTime().getTime());
                    prepareStatement.setDate(7, date);
                    prepareStatement.execute();
                }
                else
                {
                    query = "insert into transactions(billNo,pname,pcity,gqty,gtotal,billDate)" 
                        + " values(?,?,?,?,?,?)";
                    PreparedStatement prepareStatement = conn.prepareStatement(query);
            
                    prepareStatement.setInt(1, billNum);
                    prepareStatement.setString(2,param[1].toString() );
                    prepareStatement.setString(3,param[2].toString() );
                    prepareStatement.setInt(4, Integer.parseInt(param[3].toString()));
                    prepareStatement.setDouble(5, Double.parseDouble(param[4].toString()));
                    Calendar calenderInstance = Calendar.getInstance();
                    java.sql.Date date = new java.sql.Date(calenderInstance.getTime().getTime());
                    prepareStatement.setDate(6, date);
                    prepareStatement.execute();
                }
                
         
        } catch (Exception ex) {
            Logger.getLogger(ReportGenNew.class.getName()).log(Level.SEVERE, null, ex);
        }
            
  }
    
  
}