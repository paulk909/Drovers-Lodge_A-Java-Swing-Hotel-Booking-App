/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Date;

/**
 *
 * @author Paul
 */
public class Payment {
    private int paymentID;
    private Date datePaid;
    private String payeeName;
    private String cardNo;
    private String securityNo;
    private Date expiryDate;
    private int cardTypeID;
    private double totalCost;

    public Payment() {
    }

    public Payment(int paymentID, Date datePaid, String payeeName, String cardNo, String securityNo, Date expiryDate, int cardTypeID, double totalCost) {
        this.paymentID = paymentID;
        this.datePaid = datePaid;
        this.payeeName = payeeName;
        this.cardNo = cardNo;
        this.securityNo = securityNo;
        this.expiryDate = expiryDate;
        this.cardTypeID = cardTypeID;
        this.totalCost = totalCost;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getSecurityNo() {
        return securityNo;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public int getCardTypeID() {
        return cardTypeID;
    }

    public double getTotalCost() {
        return totalCost;
    }

    
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public void setSecurityNo(String securityNo) {
        this.securityNo = securityNo;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCardTypeID(int cardTypeID) {
        this.cardTypeID = cardTypeID;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    
}
