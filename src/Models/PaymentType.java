/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Paul
 */
public class PaymentType {
    
    private int paymentTypeID;
    private String paymentType;

    public PaymentType() {
    }

    public PaymentType(int paymentTypeID, String paymentType) {
        this.paymentTypeID = paymentTypeID;
        this.paymentType = paymentType;
    }

    public int getPaymentTypeID() {
        return paymentTypeID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentTypeID(int paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
    
    
    
}
