/*
 *   Created by Nishu Sharma on 26/10/22, 12:49 AM
 */

package com.example.scrappy;

public class DataObj {

    long invoiceNo;
    String customerName;
    long date;

    public long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getDate() {
        return date;
    }

    public DataObj() {
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getScrapType() {
        return scrapType;
    }

    public void setScrapType(String scrapType) {
        this.scrapType = scrapType;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    String scrapType;
    double Quantity;
    double amt;
}
