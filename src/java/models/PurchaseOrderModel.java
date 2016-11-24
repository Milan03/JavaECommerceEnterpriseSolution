package models;

/**
 *
 * @author Milan
 */

import dtos.POLineItemDTO;
import dtos.PurchaseOrderDTO;
import java.io.Serializable;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 
import javax.sql.DataSource;  

public class PurchaseOrderModel {
    
    public PurchaseOrderModel() {}
    
    public int purchaseOrderAdd(double amt, int vendorno, ArrayList<POLineItemDTO> items, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";
        int poNum = 0;
        double poamt = 0.0;
        
        for(POLineItemDTO item : items ) {
            poamt += item.getPrice().doubleValue();
        }
        //poamt = poamt * 1.13;
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date utilDate = cal.getTime();
        java.sql.Date sqlDate = new Date(utilDate.getTime());
        
        String sql = "INSERT INTO PurchaseOrders (Vendorno, PODate, Amount)"
                + " VALUES (?,?,?)";
        
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, vendorno);
            pstmt.setDate(2, sqlDate);
            pstmt.setBigDecimal(3, BigDecimal.valueOf(poamt));
            pstmt.execute();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                poNum = rs.getInt(1);
            }
            for (POLineItemDTO item : items) {
                if (item.getQuantity() > 0) {
                    sql = "INSERT INTO PurchaseOrderLineItems (PONumber,Prodcd,Qty,Price)"
                            + " VALUES (?,?,?,?)";
                    pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstmt.setInt(1, poNum);
                    pstmt.setString(2, item.getproductcode());
                    pstmt.setInt(3, item.getQuantity());
                    pstmt.setBigDecimal(4, item.getPrice());
                    pstmt.execute();
                }
            }
            con.commit();
//            msg = "PO " +poNum +" added!";
            con.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
            msg = "PO not added! Error: " +se.getMessage();
            try {
                con.rollback();
            } catch (SQLException sqx) {
                System.out.println("Rollback failed - " +sqx.getMessage());
            }
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        
        return poNum;
    } //purchaseOrderAdd
    
    public PurchaseOrderDTO getPO(int pono, DataSource ds) {
        PurchaseOrderDTO poDTO = null;
        Connection con = null;
        PreparedStatement pstmt;
        ArrayList poLineItems;
        poLineItems = new ArrayList<POLineItemDTO>();
        
        String sql = "SELECT * FROM PurchaseOrders WHERE PONumber = " +pono;
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    poDTO = new PurchaseOrderDTO();
                    poDTO.setPONumber(rs.getInt("PONumber"));
                    poDTO.setVendorno(rs.getInt("Vendorno"));
                    poDTO.setAmount(rs.getDouble("Amount"));
                    poDTO.setPODate(rs.getDate("PODate"));
                    try {
                        sql = "SELECT * FROM PurchaseOrderLineItems "
                                + "WHERE PONumber = " +poDTO.getPONumber();
                        pstmt = con.prepareStatement(sql);
                        try (ResultSet rs2 = pstmt.executeQuery()) {
                            while(rs2.next()) {
                                POLineItemDTO lineItem = new POLineItemDTO();
                                lineItem.setPONumber(rs2.getInt("PONumber"));
                                lineItem.setPrice(rs2.getBigDecimal("Price"));
                                lineItem.setQuantity(rs2.getInt("Qty"));
                                lineItem.setproductcode(rs2.getString("Prodcd"));
                                poLineItems.add(lineItem);
                            }
                            poDTO.setItems(poLineItems);
                        }
                    } catch (Exception e) {
                        System.out.println("Exception: " +e.getMessage() 
                                                +"\n in PurchaseOrderDTO.");
                    }
                }
            }
        } catch (SQLException se) {
            //Handle errors for JDBC
            System.out.println("SQL issue " + se.getMessage());
        } catch (Exception e) {
            //Handle other errors
            System.out.println("other issue " + e.getMessage());
        } finally {
            //finally block used to close resources
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                System.out.println("SQL issue on close " + se.getMessage());
            }//end finally try
        }
        return poDTO;
    } //getPO
}
