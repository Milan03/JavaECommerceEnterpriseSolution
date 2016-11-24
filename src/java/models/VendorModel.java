package models;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 
import javax.sql.DataSource;  
import java.util.ArrayList;
import dtos.VendorDTO;

/*
 * VendorModel.java
 *
 * Created on Aug 31, 2013, 3:03 PM
 *  Purpose:    Contains methods for supporting db access for vendor information
 *              Usually consumed by the ViewModel Class via DTO
 *  Revisions: 
 */
@Named(value = "vendorModel")  
@RequestScoped
public class VendorModel implements Serializable {

    public VendorModel() {
    }
    public int addVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int vendorno = -1;

        String sql = "INSERT INTO Vendors (Address1,City,Province,PostalCode,"
                + "Phone,VendorType,Name,Email) "
                + " VALUES (?,?,?,?,?,?,?,?)";

        try {
            System.out.println("before get connection");
            con = ds.getConnection();
            System.out.println("after get connection");
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getProvince());
            pstmt.setString(4, details.getPostalCode());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getType());
            pstmt.setString(7, details.getName());
            pstmt.setString(8, details.getEmail());
            pstmt.execute();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                rs.next();
                vendorno = rs.getInt(1);
            }
            con.close();
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
        return vendorno;
    } //addVendor
    
    public int deleteVendor(int vendorno, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int rowsDeleted = -1;
        
        String sql = "DELETE FROM vendors WHERE vendorno = " +vendorno;
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            rowsDeleted = pstmt.executeUpdate();
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
        return rowsDeleted;
    } //deleteVendor
    
    public ArrayList<VendorDTO> getVendors(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList<VendorDTO> vendors;
        vendors = new ArrayList<>();
        
        String sql = "SELECT * FROM vendors";
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    VendorDTO vendor = new VendorDTO();
                    vendor.setAddress1(rs.getString("Address1"));
                    vendor.setCity(rs.getString("City"));
                    vendor.setEmail(rs.getString("Email"));
                    vendor.setName(rs.getString("Name"));
                    vendor.setPhone(rs.getString("Phone"));
                    vendor.setProvince((rs.getString("Province")));
                    vendor.setPostalCode(rs.getString("PostalCode"));
                    vendor.setType(rs.getString("VendorType"));
                    vendor.setVendorno(rs.getInt("vendorno"));
                    vendors.add(vendor);
                }
            }
            con.close();
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
        return vendors;
    }
    
    public VendorDTO getVendor(int venNo, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        VendorDTO venDTO = null;
        String sql = "SELECT * FROM vendors WHERE vendorno = " +venNo;
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            venDTO = new VendorDTO();
            while (rs.next()) {
                venDTO.setAddress1(rs.getString("Address1"));
                venDTO.setCity(rs.getString("City"));
                venDTO.setEmail(rs.getString("Email"));
                venDTO.setName(rs.getString("Name"));
                venDTO.setPhone(rs.getString("Phone"));
                venDTO.setProvince((rs.getString("Province")));
                venDTO.setPostalCode(rs.getString("PostalCode"));
                venDTO.setType(rs.getString("VendorType"));
                venDTO.setVendorno(rs.getInt("vendorno"));
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
        
        return venDTO;
    }
    
    public int updateVendor(VendorDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String msg = "";
        int numRows = -1;
        
        String sql = "UPDATE vendors SET Address1 = ?, City = ?, Email = ?, "
                +"Name = ?, Phone = ?, Province = ?, PostalCode = ?, VendorType = ? "
                +"WHERE vendorno = ?";
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, details.getAddress1());
            pstmt.setString(2, details.getCity());
            pstmt.setString(3, details.getEmail());
            pstmt.setString(4, details.getName());
            pstmt.setString(5, details.getPhone());
            pstmt.setString(6, details.getProvince());
            pstmt.setString(7, details.getPostalCode());
            pstmt.setString(8, details.getType());
            pstmt.setInt(9, details.getVendorno());
            numRows = pstmt.executeUpdate();
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
        return numRows;
    }
        
}