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
 * @author Paul
 */
public class BookingLine {
    private int bookingLineID;
    private Date checkInDate;
    private Date checkOutDate;
    private int bookingID;
    private int roomID;
    private boolean[] meals;
    private double lineCost;

    public BookingLine() {
        this.meals = new boolean[3];
    }

    public BookingLine(int bookingLineID, Date checkInDate, Date checkOutDate, int bookingID, int roomID, boolean[] meals, double lineCost) {
        this.bookingLineID = bookingLineID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.meals = meals;
        this.lineCost = lineCost;
    }
    
    public BookingLine(Date checkInDate, Date checkOutDate, int bookingID, int roomID, boolean[] meals, double lineCost) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.meals = meals;
        this.lineCost = lineCost;
    }

    public BookingLine(Date checkInDate, Date checkOutDate, int roomID, boolean[] meals, double lineCost) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomID = roomID;
        this.meals = meals;
        this.lineCost = lineCost;
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


    public int getBookingID() {
        return bookingID;
    }

    public int getRoomID() {
        return roomID;
    }

    public boolean[] getMeals() {
        return meals;
    }
    
    public double getLineCost() {
        return lineCost;
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

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setMeals(boolean[] meals) {
        this.meals = meals;
    }
    
    public void setLineCost(double lineCost) {
        this.lineCost = lineCost;
    }   
    
    
    public String getRoomType()
    {
        DBManager db = new DBManager();
        HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
        roomTypes = db.getRoomTypes();
        String roomType = "";
        
        for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
        {
            if(roomTypeEntry.getValue().getRoomTypeID() == roomID)
            {
                roomType = roomTypeEntry.getValue().getRoomType();
            }
        } 
        return roomType;
    }
    
//    public int getRoomTypeID()
//    {
//        
//    }
    
    public boolean getBreakfast()
    {
        return meals[0];
    }
    
    public boolean getLunch()
    {
        return meals[1];
    }
    
    public boolean getEveningMeal()
    {
        return meals[2];
    }
    
}
