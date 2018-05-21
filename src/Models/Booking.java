/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author 30211275
 */
public class Booking {    
    private int bookingID;
    private Date dateBooked;
    private double outstandingBalance;
    private double totalCost;
    private boolean isConfirmed;
    private boolean isPaid;
    private int paymentTypeID;
    private int paymentID;
    private int customerTypeID;
    private int customerID;
    private HashMap<Integer, BookingLine> bookingLines;

    public Booking() {
        paymentTypeID = 1;
        customerTypeID = 1;
        bookingLines = new HashMap<Integer, BookingLine>();
    }

    public Booking(int bookingID, Date dateBooked, double outstandingBalance, double totalCost, int paymentID, int customerID) {
        this.bookingID = bookingID;
        this.dateBooked = dateBooked;
        this.outstandingBalance = outstandingBalance;
        this.totalCost = totalCost;
        this.paymentID = paymentID;
        this.customerID = customerID;
        bookingLines = new HashMap<Integer, BookingLine>();
    }

    public int getBookingID() {
        return bookingID;
    }

    public Date getDateBooked() {
        return dateBooked;
    }

    public double getOutstandingBalance() {
        return outstandingBalance;
    }

    public double getTotalCost() {
        return totalCost;
    }
    
    public boolean getIsConfirmed() {
        return isConfirmed;
    }
    
    public boolean getIsPaid() {
        return isPaid;
    }
    
    public int getPaymentTypeID() {
        return paymentTypeID;
    }

    public int getPaymentID() {
        return paymentID;
    }
    
    public int getCustomerTypeID() {
        return customerTypeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public HashMap<Integer, BookingLine> getBookingLines() {
        return bookingLines;
    }

    
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setDateBooked(Date dateBooked) {
        this.dateBooked = dateBooked;
    }

    public void setOutstandingBalance(double outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    public void setIsConfirmed(boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
    
    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }
    
    public void setPaymentTypeID(int paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
    
    public void setCustomerTypeID(int customerTypeID) {
        this.customerTypeID = customerTypeID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setBookingLines(HashMap<Integer, BookingLine> bookingLines) {
        this.bookingLines = bookingLines;
    }
    
    
    public void populateBookingLines()
    {
        HashMap<Integer, BookingLine> allBookingLines = new HashMap<Integer, BookingLine>();
        DBManager db = new DBManager();
        allBookingLines = db.getBookingLines();
        
        for (Map.Entry<Integer, BookingLine> bookingLineEntry : allBookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingID() == bookingID)
            {
                bookingLines.put(bookingLineEntry.getValue().getBookingLineID(), bookingLineEntry.getValue());
            }
        }        
    }
    
    
}
