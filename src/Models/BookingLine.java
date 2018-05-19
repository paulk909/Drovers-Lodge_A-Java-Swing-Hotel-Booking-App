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
public class BookingLine {
    private int bookingLineID;
    private Date checkInDate;
    private Date checkOutDate;
    private boolean isPaid;
    private int bookingID;
    private int roomID;
    private Meal[] mealArray;

    public BookingLine() {
        this.mealArray = new Meal[3];
    }

    public BookingLine(int bookingLineID, Date checkInDate, Date checkOutDate, boolean isPaid, int bookingID) {
        this.bookingLineID = bookingLineID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.isPaid = isPaid;
        this.bookingID = bookingID;
        this.mealArray = new Meal[3];
    }

    public int getBookingLineID() {
        return bookingLineID;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public int getBookingID() {
        return bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    public Meal[] getMealArray() {
        return mealArray;
    }

    public void setBookingLineID(int bookingLineID) {
        this.bookingLineID = bookingLineID;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setMealArray(Meal[] mealArray) {
        this.mealArray = mealArray;
    }
    
    
    
}
