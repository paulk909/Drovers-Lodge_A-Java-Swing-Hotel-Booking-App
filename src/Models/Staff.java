/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paul
 */
public class Staff {
    private int staffID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int staffRoleID;
    private boolean isLoggedIn;

    public Staff() {
        isLoggedIn = false;
    }

    public Staff(int staffID, String username, String password, String firstName, String lastName, String email, int staffRoleID) {
        this.staffID = staffID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.staffRoleID = staffRoleID;
    }
        
    public Staff(String username, String password, String firstName, String lastName, String email, int staffRoleID) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.staffRoleID = staffRoleID;
    }
    
    public Staff(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getStaffID() {
        return staffID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int getStaffRoleID() {
        return staffRoleID;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    
    public void setStaffID(int staffID) {
        this.staffID = staffID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStaffRoleID(int staffRoleID) {
        this.staffRoleID = staffRoleID;
    }
    
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    
    public boolean findCurrentBooking()
    {
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        DBManager db = new DBManager();
        bookings = db.getBookings();
        boolean isFound = false;
        
        if(isLoggedIn)
        {
            for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
            {
                if(bookingEntry.getValue().getStaffID() == staffID)
                {
                    if(bookingEntry.getValue().getIsConfirmed() == false)
                    {
                        isFound = true;
                    }
                }
            }  
        }
        return isFound;
    }
    
    public int getCurrentBookingID()
    {        
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        DBManager db = new DBManager();
        bookings = db.getBookings();
        int currentBookingID = 0;
        
        if(isLoggedIn)
        {
            for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
            {
                if(bookingEntry.getValue().getStaffID() == staffID)
                {
                    if(bookingEntry.getValue().getIsConfirmed() == false)
                    {
                        currentBookingID = bookingEntry.getValue().getBookingID();
                    }
                }
            }
        }
        return currentBookingID;
    }
    
    public Booking getCurrentBooking()
    {        
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        DBManager db = new DBManager();
        bookings = db.getBookings();
        Booking currentBooking = new Booking();
        
        if(isLoggedIn)
        {
            for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
            {
                if(bookingEntry.getValue().getStaffID() == staffID)
                {
                    if(bookingEntry.getValue().getIsConfirmed() == false)
                    {
                        currentBooking = bookingEntry.getValue();
                    }
                }
            }
        }
        return currentBooking;
    }
    
}
