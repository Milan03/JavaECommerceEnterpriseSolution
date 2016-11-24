package models;

import java.io.Serializable;
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named; 
import javax.sql.DataSource;  
import java.util.ArrayList;
import dtos.ProductDTO;
/**
 *
 * @author      Milan
 * @file        ProductModel.java
 * @description Methods for supporting database access for product information
 * @date        Sept 21, 2014
 */
@Named(value = "productModel")
@RequestScoped
public class ProductModel implements Serializable {

    public ProductModel() {}
    
    public String addProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        String productcode = "";

        String sql = "INSERT INTO Products (productcode,vendorno,vendorsku,productname,"
                +  "costprice,msrp,rop,eoq,qoh,qoo)"
                + " VALUES (?,?,?,?,?,?,?,?,?,?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, details.getProductcode());
            pstmt.setInt(2, details.getVendorno());
            pstmt.setString(3, details.getVendorsku());
            pstmt.setString(4, details.getProductname());
            pstmt.setDouble(5, details.getCostprice());
            pstmt.setDouble(6, details.getMsrp());
            pstmt.setInt(7, details.getRop());
            pstmt.setInt(8, details.getEoq());
            pstmt.setInt(9, details.getQoh());
            pstmt.setInt(10, details.getQoo());
            productcode = details.getProductcode();
            pstmt.execute();

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
        return productcode;
    } //addProduct
    
    public int deleteProduct(String pc, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int rowsDeleted = -1;
        
        String sql = "DELETE FROM Products WHERE productcode = '" +pc +"'";
        
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
    } //deleteProduct
    
    public int updateProduct(ProductDTO details, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        int numRows = -1;

        String sql = "UPDATE Products SET productcode = ?, vendorno = ?, vendorsku = ?, productname = ?, costprice = ?, msrp = ?, rop = ?, eoq = ?, qoh = ?, qoo = ? WHERE productcode = ?";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, details.getProductcode());
            pstmt.setInt(2, details.getVendorno());
            pstmt.setString(3, details.getVendorsku());
            pstmt.setString(4, details.getProductname());
            pstmt.setDouble(5, details.getCostprice());
            pstmt.setDouble(6, details.getMsrp());
            pstmt.setInt(7, details.getRop());
            pstmt.setInt(8, details.getEoq());
            pstmt.setInt(9, details.getQoh());
            pstmt.setInt(10, details.getQoo());
            pstmt.setString(11, details.getProductcode());
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
    } //updateProduct
    
    public ArrayList<ProductDTO> getProducts(DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList<ProductDTO> products;
        products = new ArrayList<>();
        
        String sql = "SELECT * FROM Products";
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    ProductDTO product = new ProductDTO();
                    product.setProductcode(rs.getString("productcode"));
                    product.setVendorno(rs.getInt("vendorno"));
                    product.setVendorsku(rs.getString("vendorsku"));
                    product.setProductname(rs.getString("productname"));
                    product.setCostprice(rs.getDouble("costprice"));
                    product.setMsrp(rs.getDouble("msrp"));
                    product.setRop(rs.getInt("rop"));
                    product.setEoq(rs.getInt("eoq"));
                    product.setQoh(rs.getInt("qoh"));
                    product.setQoo(rs.getInt("qoo"));
                    //product.setQRCODE(rs.getBlob("QRCODE"));
                    //product.setQRCODETXT(rs.getString("QRCODETXT"));
                    products.add(product);
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
        return products;        
    } //getProducts
    
    public ArrayList<ProductDTO> getProdsForVendor(int vendorno, DataSource ds) {
        PreparedStatement pstmt;
        Connection con = null;
        ArrayList<ProductDTO> products;
        products = new ArrayList<>();
        
        String sql = "SELECT * FROM Products WHERE vendorno = " +vendorno;
        
        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    ProductDTO product = new ProductDTO();
                    product.setProductcode(rs.getString("productcode"));
                    product.setVendorno(rs.getInt("vendorno"));
                    product.setVendorsku(rs.getString("vendorsku"));
                    product.setProductname(rs.getString("productname"));
                    product.setCostprice(rs.getDouble("costprice"));
                    product.setMsrp(rs.getDouble("msrp"));
                    product.setRop(rs.getInt("rop"));
                    product.setEoq(rs.getInt("eoq"));
                    product.setQoh(rs.getInt("qoh"));
                    product.setQoo(rs.getInt("qoo"));
                    //product.setQRCODE(rs.getBlob("QRCODE"));
                    //product.setQRCODETXT(rs.getString("QRCODETXT"));
                    products.add(product);
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
        return products;
    }
}
