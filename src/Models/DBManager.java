/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 30211275
 */
public class DBManager {
    
    private Connection dbCon;
    private String conString="jdbc:ucanaccess://src/Db/DroversLodgeDb.accdb";
    
    
    public DBManager()
    {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            this.dbCon = DriverManager.getConnection(conString);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean addCustomerToDb(Customer customerToAdd)
    {      
        try {
            String username = customerToAdd.getUsername();
            String password = customerToAdd.getPassword();
            String firstName = customerToAdd.getFirstName();
            String lastName = customerToAdd.getLastName();
            Date dateOfBirth = customerToAdd.getDateOfBirth();
            String email = customerToAdd.getEmail();
            String house = customerToAdd.getHouse();
            String street = customerToAdd.getStreet();
            String town = customerToAdd.getTown();
            String postcode = customerToAdd.getPostcode();
            String telephone = customerToAdd.getTelephone();
            String mobile = customerToAdd.getMobile();
            
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            String sqlString = "select * from Customers where username = '" + username + "'";
            
            rs = st.executeQuery(sqlString);
            
            if(rs.next())
            {
                return false;
            }
            else
            {
                try {
                    sqlString = "INSERT INTO Customers (Username, Password, FirstName, LastName, DateOfBirth, Email, House, Street, Town, Postcode, Telephone, Mobile) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = dbCon.prepareStatement (sqlString);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, firstName);
                    ps.setString(4, lastName);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    ps.setString(5, dateFormat.format(dateOfBirth));
                    ps.setString(6, email);
                    ps.setString(7, house);
                    ps.setString(8, street);
                    ps.setString(9, town);
                    ps.setString(10, postcode);
                    ps.setString(11, telephone);
                    ps.setString(12, mobile);
                    int i = ps.executeUpdate();
                    ps.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public void addBookingToDb(Booking bookingToAdd)
    {
        Customer currentCustomer = new Customer();
        double totalCost = bookingToAdd.getTotalCost();
        boolean isConfirmed = bookingToAdd.getIsConfirmed();
        boolean isPaid = bookingToAdd.getIsPaid();
        int paymentTypeID = bookingToAdd.getPaymentTypeID();
        int paymentID = bookingToAdd.getPaymentID();
        int customerTypeID = bookingToAdd.getCustomerTypeID();
        int customerID = bookingToAdd.getCustomerID();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "INSERT INTO Bookings (TotalCost, IsConfirmed, IsPaid, PaymentTypeID, PaymentID, CustomerTypeID, CustomerID) VALUES("
                    + totalCost + ","
                    + isConfirmed + ","
                    + isPaid + ","
                    + paymentTypeID + ","
                    + paymentID + ","
                    + customerTypeID + ","
                    + customerID + ")";
                
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addBookingLineToDb(BookingLine bookingLineToAdd)
    {
        Customer currentCustomer = new Customer();
        Date checkInDate = bookingLineToAdd.getCheckInDate();
        Date checkOutDate = bookingLineToAdd.getCheckOutDate();
        int roomID = bookingLineToAdd.getRoomID();
        boolean[] meals = bookingLineToAdd.getMeals();
        boolean breakfast = meals[0];
        boolean lunch = meals[1];
        boolean eveningMeal = meals[2];
        double lineCost = bookingLineToAdd.getLineCost();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        if(currentCustomer.findCurrentBooking())
        {
            try {
                int bookingID = currentCustomer.getCurrentBookingID();                
                Statement stmt = dbCon.createStatement();
                String sql = "INSERT INTO BookingLines (CheckInDate, CheckOutDate, BookingID, RoomID, Breakfast, Lunch, EveningMeal, LineCost) VALUES('"
                        + dateFormat.format(checkInDate) + "','"
                        + dateFormat.format(checkOutDate) + "',"
                        + bookingID + ","
                        + roomID + ","
                        + breakfast + ","
                        + lunch + ","
                        + eveningMeal + ","
                        + lineCost + ")";
                
                stmt.executeUpdate(sql);
                updateBookingTotalCost(bookingID, lineCost);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            Booking newBooking = new Booking();
            //Date date = new Date();
            //newBooking.setDateBooked(date);
            addBookingToDb(newBooking);
            try {
                int bookingID = currentCustomer.getCurrentBookingID();                
                Statement stmt = dbCon.createStatement();
                String sql = "INSERT INTO BookingLines (CheckInDate, CheckOutDate, BookingID, RoomID, Breakfast, Lunch, EveningMeal, LineCost) VALUES('"
                        + dateFormat.format(checkInDate) + "','"
                        + dateFormat.format(checkOutDate) + "',"
                        + bookingID + ","
                        + roomID + ","
                        + breakfast + ","
                        + lunch + ","
                        + eveningMeal + ","
                        + lineCost + ")";
                
                stmt.executeUpdate(sql);
                updateBookingTotalCost(bookingID, lineCost);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    

    
    
    public HashMap<Integer, Booking> getBookings()
    {
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        try 
        {
            String sqlString = "select * from Bookings";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Booking bookingToAdd = new Booking();
                bookingToAdd.setBookingID(rs.getInt("ID"));
                bookingToAdd.setDateBooked(rs.getDate("DateBooked"));
                bookingToAdd.setOutstandingBalance(rs.getDouble("OutstandingBalance"));
                bookingToAdd.setTotalCost(rs.getDouble("TotalCost"));
                bookingToAdd.setIsConfirmed(rs.getBoolean("IsConfirmed"));
                bookingToAdd.setIsPaid(rs.getBoolean("IsPaid"));
                bookingToAdd.setPaymentTypeID(rs.getInt("PaymentTypeID"));
                bookingToAdd.setPaymentID(rs.getInt("PaymentID"));
                bookingToAdd.setCustomerTypeID(rs.getInt("CustomerTypeID"));
                bookingToAdd.setCustomerID(rs.getInt("CustomerID"));                
                bookings.put(bookingToAdd.getBookingID(), bookingToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return bookings;
    }
    
    
    public HashMap<Integer, BookingLine> getBookingLines()
    {
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        try 
        {
            String sqlString = "select * from BookingLines";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                BookingLine bookingLineToAdd = new BookingLine();
                bookingLineToAdd.setBookingLineID(rs.getInt("ID"));
                bookingLineToAdd.setCheckInDate(rs.getDate("CheckInDate"));
                bookingLineToAdd.setCheckOutDate(rs.getDate("CheckOutDate"));
                bookingLineToAdd.setBookingID(rs.getInt("BookingID"));
                bookingLineToAdd.setRoomID(rs.getInt("RoomID"));
                boolean breakfast = (rs.getBoolean("Breakfast"));
                boolean lunch = (rs.getBoolean("Lunch"));
                boolean eveningMeal = (rs.getBoolean("EveningMeal"));
                boolean[] meals = {breakfast, lunch, eveningMeal};
                bookingLineToAdd.setMeals(meals);
                bookingLineToAdd.setLineCost(rs.getDouble("LineCost"));      
                bookingLines.put(bookingLineToAdd.getBookingLineID(), bookingLineToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return bookingLines;
    }
    
    public HashMap<Integer, RoomType> getRoomTypes()
    {
        HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
        try 
        {
            String sqlString = "select * from RoomTypes";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                RoomType roomTypeToAdd = new RoomType();
                roomTypeToAdd.setRoomTypeID(rs.getInt("ID"));
                roomTypeToAdd.setRoomType(rs.getString("RoomType"));
                roomTypeToAdd.setRoomPrice(rs.getDouble("RoomPrice"));
                roomTypes.put(roomTypeToAdd.getRoomTypeID(), roomTypeToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return roomTypes;
    }
    
    public HashMap<Integer, PaymentType> getPaymentTypes()
    {
        HashMap<Integer, PaymentType> paymentTypes = new HashMap<Integer, PaymentType>();
        try 
        {
            String sqlString = "select * from PaymentTypes";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                PaymentType paymentTypeToAdd = new PaymentType();
                paymentTypeToAdd.setPaymentTypeID(rs.getInt("ID"));
                paymentTypeToAdd.setPaymentType(rs.getString("PaymentType"));
                paymentTypes.put(paymentTypeToAdd.getPaymentTypeID(), paymentTypeToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return paymentTypes;
    }
    
    public HashMap<Integer, MealType> getMealTypes()
    {
        HashMap<Integer, MealType> meals = new HashMap<Integer, MealType>();
        try 
        {
            String sqlString = "select * from MealTypes";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                MealType mealToAdd = new MealType();
                mealToAdd.setMealID(rs.getInt("ID"));
                mealToAdd.setMealType(rs.getString("MealType"));
                mealToAdd.setMealPrice(rs.getDouble("MealPrice"));
                meals.put(mealToAdd.getMealID(), mealToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return meals;
    }
    
    
    
    public void updateBookingTotalCost(int bookingID, double costToBeAdded)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Bookings SET TotalCost = TotalCost + " + costToBeAdded  + " WHERE ID = " + bookingID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reduceBookingTotalCost(int bookingID, double costToBeTakenOff)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Bookings SET TotalCost = TotalCost - " + costToBeTakenOff  + " WHERE ID = " + bookingID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void removeBookingLine(int bookingLineID)
    {
        int bookingID = 0;
        double bookingLineValue = 0;
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        bookingLines = getBookingLines();
        for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingLineID() == bookingLineID)
            {
                bookingID = bookingLineEntry.getValue().getBookingID();
                bookingLineValue = bookingLineEntry.getValue().getLineCost();
            }
        }
        if(bookingLines.size()>1)
        {
            try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from BookingLines where ID = " + bookingLineID;
                stmt.execute(sql);
                reduceBookingTotalCost(bookingID, bookingLineValue);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
        {
            try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from BookingLines where ID = " + bookingLineID;
                stmt.execute(sql);
                String sql2 = "DELETE from Bookings where ID = " + bookingID;
                stmt.execute(sql2);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
       
}