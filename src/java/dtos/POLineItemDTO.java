package dtos;

import java.math.BigDecimal;

/**
 *
 * @author Milan
 */
public class POLineItemDTO {
    public POLineItemDTO() {}
    
    private int LineID;
    private int PONumber;
    private String productcode;
    private int Quantity;
    private BigDecimal Price;

    public int getLineID() {
        return LineID;
    }

    public void setLineID(int LineID) {
        this.LineID = LineID;
    }

    public int getPONumber() {
        return PONumber;
    }

    public void setPONumber(int PONumber) {
        this.PONumber = PONumber;
    }

    public String getproductcode() {
        return productcode;
    }

    public void setproductcode(String productcode) {
        this.productcode = productcode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal Price) {
        this.Price = Price;
    }
}
