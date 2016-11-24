/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dtos;

/**
 *
 * @author Milan
 */

import java.io.Serializable;
import java.lang.String;
import java.sql.Blob;
public class ProductDTO implements Serializable {
    public ProductDTO() {}

    private String productcode;
    private int vendorno;
    private String vendorsku;
    private String productname;
    private double costprice; 
    private double msrp;
    private int rop;
    private int eoq;
    private int qoh;
    private int qoo;
    //private Blob QRCODE;
    //private String QRCODETXT;   
    
    public String getProductcode() {
        return productcode;
    }

    public int getVendorno() {
        return vendorno;
    }

    public String getVendorsku() {
        return vendorsku;
    }

    public String getProductname() {
        return productname;
    }

    public double getCostprice() {
        return costprice;
    }

    public double getMsrp() {
        return msrp;
    }

    public int getRop() {
        return rop;
    }

    public int getEoq() {
        return eoq;
    }

    public int getQoh() {
        return qoh;
    }

    public int getQoo() {
        return qoo;
    }

//    public Blob getQRCODE() {
//        return QRCODE;
//    }

//    public String getQRCODETXT() {
//        return QRCODETXT;
//    }
    
    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public void setVendorno(int vendorno) {
        this.vendorno = vendorno;
    }

    public void setVendorsku(String vendorsku) {
        this.vendorsku = vendorsku;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public void setCostprice(double costprice) {
        this.costprice = costprice;
    }

    public void setMsrp(double msrp) {
        this.msrp = msrp;
    }

    public void setRop(int rop) {
        this.rop = rop;
    }

    public void setEoq(int eoq) {
        this.eoq = eoq;
    }

    public void setQoh(int qoh) {
        this.qoh = qoh;
    }

    public void setQoo(int qoo) {
        this.qoo = qoo;
    }

//    public void setQRCODE(Blob QRCODE) {
//        this.QRCODE = QRCODE;
//    }

//    public void setQRCODETXT(String QRCODETXT) {
//        this.QRCODETXT = QRCODETXT;
//    }
}
