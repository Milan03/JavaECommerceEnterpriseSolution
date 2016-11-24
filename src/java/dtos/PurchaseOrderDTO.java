/*
 * File:         PurchaseOrderDTO.java
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      Data transfer object for Purchase Orders for 
 *               interacting with PO database entity.
 */

package dtos;

/**
 *
 * @author Milan
 */

import java.io.Serializable;
import java.lang.String;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import dtos.POLineItemDTO;
import java.math.BigDecimal;

public class PurchaseOrderDTO {
    public PurchaseOrderDTO() {}

    private int PONumber;
    private int Vendorno;
    private double Amount;
    private ArrayList<POLineItemDTO> Items;
    private Date PODate;
    
    public int getPONumber() {
        return PONumber;
    }

    public void setPONumber(int PONumber) {
        this.PONumber = PONumber;
    }

    public int getVendorno() {
        return Vendorno;
    }

    public void setVendorno(int Vendorno) {
        this.Vendorno = Vendorno;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double Amount) {
        this.Amount = Amount;
    }

    public ArrayList<POLineItemDTO> getItems() {
        return Items;
    }

    public void setItems(ArrayList<POLineItemDTO> Items) {
        this.Items = Items;
    }

    public Date getPODate() {
        return PODate;
    }

    public void setPODate(Date PODate) {
        this.PODate = PODate;
    }
    
}
