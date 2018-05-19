/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author 30211275
 */
public class Booking {
    
    private int bookingID;
    private Date dateBooked;
    private double outstandingBalance;
    private double totalCost;
    private int paymentID;
    private int userID;
    private HashMap<Integer, BookingLine> bookingLines;

    public Booking() {
        bookingLines = new HashMap<Integer, BookingLine>();
    }

    public Booking(Date dateBooked, double outstandingBalance, double totalCost, int paymentID, int userID) {
        this.dateBooked = dateBooked;
        this.outstandingBalance = outstandingBalance;
        this.totalCost = totalCost;
        this.paymentID = paymentID;
        this.userID = userID;
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

    public int getPaymentID() {
        return paymentID;
    }

    public int getUserID() {
        return userID;
    }

    public HashMap<Integer, BookingLine> getBookingLines() {
        return bookingLines;
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

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setBookingLines(HashMap<Integer, BookingLine> bookingLines) {
        this.bookingLines = bookingLines;
    }
    
    
    
}
