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
import java.text.ParseException;
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
    
    
    public boolean addStaffToDb(Staff staffToAdd)
    {      
        try {
            String username = staffToAdd.getUsername();
            String password = staffToAdd.getPassword();
            String firstName = staffToAdd.getFirstName();
            String lastName = staffToAdd.getLastName();
            String email = staffToAdd.getEmail();
            int staffRoleID = staffToAdd.getStaffRoleID();
                        
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            String sqlString = "select * from Staff where username = '" + username + "'";
            
            rs = st.executeQuery(sqlString);
            
            if(rs.next())
            {
                return false;
            }
            else
            {
                try {
                    sqlString = "INSERT INTO Staff (Username, Password, FirstName, LastName, Email, StaffRoleID) values (?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = dbCon.prepareStatement (sqlString);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    ps.setString(3, firstName);
                    ps.setString(4, lastName);
                    ps.setString(5, email);
                    ps.setString(6, String.valueOf(staffRoleID));
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
    
    public boolean addRoomToDb(Room roomToAdd) throws SQLException
    {      
        int roomTypeID = roomToAdd.getRoomTypeID();

        Statement st = dbCon.createStatement();
        ResultSet rs = null;
        try {
                String sqlString = "INSERT INTO Rooms (RoomTypeID) values (?)";
                PreparedStatement ps = dbCon.prepareStatement (sqlString);
                ps.setString(1, String.valueOf(roomTypeID));
                int i = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }        
        return true;
    }
    
    public void addBookingToDb(LoggedInUser loggedInUser, Booking bookingToAdd)
    {
        if(loggedInUser.getIsLoggedIn() == false)
        {
            Customer currentCustomer = new Customer();
            double totalCost = bookingToAdd.getTotalCost();
            boolean isConfirmed = bookingToAdd.getIsConfirmed();
            boolean isPaid = bookingToAdd.getIsPaid();
            int paymentTypeID = bookingToAdd.getPaymentTypeID();
            int paymentID = bookingToAdd.getPaymentID();
            int customerTypeID = 1;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try 
            {
                Statement stmt = dbCon.createStatement();
                String sql = "INSERT INTO Bookings (TotalCost, IsConfirmed, IsPaid, PaymentTypeID, PaymentID, CustomerTypeID) VALUES("
                        + totalCost + ","
                        + isConfirmed + ","
                        + isPaid + ","
                        + paymentTypeID + ","
                        + paymentID + ","
                        + customerTypeID + ")";

                stmt.executeUpdate(sql);
            } catch (SQLException ex) 
            {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(loggedInUser.getIsLoggedIn() == true)
        {
            if(loggedInUser.getUserTypeID() == 2)
            {
                String username = loggedInUser.getUsername();
                Customer currentCustomer = getCustomerFromUsername(username);
                double totalCost = bookingToAdd.getTotalCost();
                boolean isConfirmed = bookingToAdd.getIsConfirmed();
                boolean isPaid = bookingToAdd.getIsPaid();
                int paymentTypeID = bookingToAdd.getPaymentTypeID();
                int paymentID = bookingToAdd.getPaymentID();
                int customerTypeID = 2;
                int customerID = currentCustomer.getCustomerID();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try 
                {
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
                } catch (SQLException ex) 
                {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(loggedInUser.getUserTypeID() == 3)
            {
                String username = loggedInUser.getUsername();
                Staff currentStaff = getStaffFromUsername(username);
                double totalCost = bookingToAdd.getTotalCost();
                boolean isConfirmed = bookingToAdd.getIsConfirmed();
                boolean isPaid = bookingToAdd.getIsPaid();
                int paymentTypeID = bookingToAdd.getPaymentTypeID();
                int paymentID = bookingToAdd.getPaymentID();
                int customerTypeID = 3;
                int staffID = currentStaff.getStaffID();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try 
                {
                    Statement stmt = dbCon.createStatement();
                    String sql = "INSERT INTO Bookings (TotalCost, IsConfirmed, IsPaid, PaymentTypeID, PaymentID, CustomerTypeID, StaffID) VALUES("
                            + totalCost + ","
                            + isConfirmed + ","
                            + isPaid + ","
                            + paymentTypeID + ","
                            + paymentID + ","
                            + customerTypeID + ","
                            + staffID + ")";

                    stmt.executeUpdate(sql);
                } catch (SQLException ex) 
                {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void addBookingLineToDb(LoggedInUser loggedInUser, BookingLine bookingLineToAdd)
    {
        if(loggedInUser.getIsLoggedIn() == false)
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
                try 
                {
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
                } catch (SQLException ex) 
                {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                Booking newBooking = new Booking();
                addBookingToDb(loggedInUser, newBooking);
                try 
                {
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
                } catch (SQLException ex) 
                {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }            
            }
        } else if (loggedInUser.getIsLoggedIn() == true)
        {
            if(loggedInUser.getUserTypeID() == 2)
            {
                String username = loggedInUser.getUsername();
                Customer currentCustomer = getCustomerFromUsername(username);
                currentCustomer.setIsLoggedIn(true);
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
                    try 
                    {
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
                    } catch (SQLException ex) 
                    {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    Booking newBooking = new Booking();
                    addBookingToDb(loggedInUser, newBooking);
                    try 
                    {
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
                    } catch (SQLException ex) 
                    {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                }
            } else if(loggedInUser.getUserTypeID() == 3)
            {
                String username = loggedInUser.getUsername();
                Staff currentStaff = getStaffFromUsername(username);
                currentStaff.setIsLoggedIn(true);
                Date checkInDate = bookingLineToAdd.getCheckInDate();
                Date checkOutDate = bookingLineToAdd.getCheckOutDate();
                int roomID = bookingLineToAdd.getRoomID();
                boolean[] meals = bookingLineToAdd.getMeals();
                boolean breakfast = meals[0];
                boolean lunch = meals[1];
                boolean eveningMeal = meals[2];
                double lineCost = bookingLineToAdd.getLineCost();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if(currentStaff.findCurrentBooking())
                {
                    try 
                    {
                        int bookingID = currentStaff.getCurrentBookingID();                
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
                    } catch (SQLException ex) 
                    {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                {
                    Booking newBooking = new Booking();
                    addBookingToDb(loggedInUser, newBooking);
                    try 
                    {
                        int bookingID = currentStaff.getCurrentBookingID();                
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
                    } catch (SQLException ex) 
                    {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }            
                }
            }
        }
    }
    

    public void confirmBookingPayAtCheckIn(LoggedInUser loggedInUser, int bookingID, int customerID)
    {
        try {
            Date dateBooked = new Date();
            double outstandingBalance = getBookingFromBookingID(bookingID).getTotalCost();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Statement stmt = dbCon.createStatement();
            
            String sql = "UPDATE Bookings SET DateBooked = '"+ dateFormat.format(dateBooked) + 
                    "', OutstandingBalance = " + outstandingBalance + 
                    ", IsConfirmed = " + true + 
                    ", CustomerID = " + customerID + 
                    " WHERE ID = " + bookingID;

            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean checkForUnassignedBooking()
    {
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        bookings = getBookings();
        boolean isFound = false;
        
        for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
            {
                if(bookingEntry.getValue().getCustomerTypeID() == 1)
                {
                    if(bookingEntry.getValue().getIsConfirmed() == false)
                    {
                        isFound = true;
                    }
                }
            } 
        return isFound;
    }
    
    public void assignUnassignedBookingToUser(LoggedInUser loggedInUser)
    {
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        bookings = getBookings();
        
        for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
            {
                if(bookingEntry.getValue().getCustomerTypeID() == 1)
                {
                    if(bookingEntry.getValue().getIsConfirmed() == false)
                    {
                        if(loggedInUser.getUserTypeID() == 2)
                        {
                            try {
                                int customerID = getCustomerFromUsername(loggedInUser.getUsername()).getCustomerID();
                                int bookingID = bookingEntry.getValue().getBookingID();
                                Statement stmt = dbCon.createStatement();
                                String sql = "UPDATE Bookings SET CustomerTypeID = 2, CustomerID = " + customerID  + " WHERE ID = " + bookingID;
                                stmt.execute(sql);
                            } catch (SQLException ex) {
                                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else if(loggedInUser.getUserTypeID() == 3)
                        {
                            try {
                                int staffID = getStaffFromUsername(loggedInUser.getUsername()).getStaffID();
                                int bookingID = bookingEntry.getValue().getBookingID();
                                Statement stmt = dbCon.createStatement();
                                String sql = "UPDATE Bookings SET CustomerTypeID = 3, StaffID = " + staffID  + " WHERE ID = " + bookingID;
                                stmt.execute(sql);
                            } catch (SQLException ex) {
                                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } 
    }
    
    
    public void clearUnassignedBookingsOnStartup()
    {
        int bookingID = 0;
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        bookings = getBookings();
        for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
        {
            if(bookingEntry.getValue().getCustomerTypeID() == 1)
            {
                bookingID = bookingEntry.getValue().getBookingID();
                try 
                {
                    Statement stmt = dbCon.createStatement();
                    String sql = "DELETE from BookingLines where BookingID = " + bookingID;
                    stmt.execute(sql);
                    String sql2 = "DELETE from Bookings where ID = " + bookingID;
                    stmt.execute(sql2);
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }        
    }
    
    
    public void takePayment(Payment payment)
    {
        try {
            Date dateBooked = new Date();
            String payeeName = payment.getPayeeName();
            String cardNo = payment.getCardNo();
            String securityNo = payment.getSecurityNo();
            Date expiryDate = payment.getExpiryDate();
            int cardTypeID = payment.getCardTypeID();
            double totalCost = payment.getTotalCost();
            int bookingID = payment.getBookingID();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Statement stmt = dbCon.createStatement();
            String sql = "INSERT INTO Payments (DatePaid, PayeeName, CardNo, SecurityNo, ExpiryDate, CardTypeID, TotalCost, BookingID) VALUES('"
                    + dateFormat.format(dateBooked) + "', '"
                    + payeeName + "', '"
                    + cardNo + "', '"
                    + securityNo + "', '"
                    + dateFormat.format(expiryDate) + "', "
                    + cardTypeID + ", "
                    + totalCost + ", "
                    + bookingID +")";

            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setOutstandingPaymentZeroIsPaidTrue(int bookingID)
    {
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Bookings SET OutstandingPayment = 0.00, IsPaid = true WHERE ID = " + bookingID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void confirmBookingPayNow(LoggedInUser loggedInUser, int bookingID, int customerID, Payment payment)
    {
        try 
        {
            Date dateBooked = new Date();
            String payeeName = payment.getPayeeName();
            String cardNo = payment.getCardNo();
            String securityNo = payment.getSecurityNo();
            Date expiryDate = payment.getExpiryDate();
            int cardTypeID = payment.getCardTypeID();
            double totalCost = payment.getTotalCost();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            Statement stmt = dbCon.createStatement();
                String sql = "INSERT INTO Payments (DatePaid, PayeeName, CardNo, SecurityNo, ExpiryDate, CardTypeID, TotalCost, BookingID) VALUES('"
                        + dateFormat.format(dateBooked) + "', '"
                        + payeeName + "', '"
                        + cardNo + "', '"
                        + securityNo + "', '"
                        + dateFormat.format(expiryDate) + "', "
                        + cardTypeID + ", "
                        + totalCost + ", "
                        + bookingID +")";

                stmt.executeUpdate(sql);
            
            int paymentID = getMostRecentPaymentIDFromDateBooked(dateBooked);
            
            String sql2 = "UPDATE Bookings SET DateBooked = '" + dateFormat.format(dateBooked) + 
                    "', IsConfirmed = " + true + 
                    ", IsPaid = " + true + 
                    ", OutstandingBalance = 0.00 " +
                    ", PaymentTypeID = " + 2 + 
                    ", PaymentID = " + paymentID + 
                    ", CustomerID = " + customerID + 
                    " WHERE ID = " + bookingID;

            stmt.executeUpdate(sql2);
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public int getMostRecentPaymentIDFromDateBooked(Date dateBooked)
    {
        HashMap<Integer, Payment> payments = new HashMap<Integer, Payment>();
        payments = getPayments();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int paymentID = 0;
        
        for(Map.Entry<Integer, Payment> paymentEntry : payments.entrySet())
        {
            if(dateFormat.format(paymentEntry.getValue().getDatePaid()).equals(dateFormat.format(dateBooked)))
            {
                paymentID = paymentEntry.getValue().getPaymentID();
            }
        }
        
        return paymentID;
    }

    
    public HashMap<Integer, Payment> getPayments()
    {
        HashMap<Integer, Payment> payments = new HashMap<Integer, Payment>();
        try 
        {
            String sqlString = "select * from Payments";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Payment paymentToAdd = new Payment();
                paymentToAdd.setPaymentID(rs.getInt("ID"));
                paymentToAdd.setDatePaid(rs.getDate("DatePaid"));  
                paymentToAdd.setPayeeName(rs.getString("PayeeName"));
                paymentToAdd.setCardNo(rs.getString("CardNo"));  
                paymentToAdd.setSecurityNo(rs.getString("SecurityNo"));
                paymentToAdd.setExpiryDate(rs.getDate("ExpiryDate"));  
                paymentToAdd.setCardTypeID(rs.getInt("CardTypeID"));
                paymentToAdd.setTotalCost(rs.getDouble("TotalCost"));  
                payments.put(paymentToAdd.getPaymentID(), paymentToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return payments;
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
                bookingToAdd.setStaffID(rs.getInt("StaffID"));           
                bookingToAdd.setCustomerID(rs.getInt("CustomerID"));                
                bookings.put(bookingToAdd.getBookingID(), bookingToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return bookings;
    }
    
    public HashMap<Integer, Booking> getBookingsForCustomerID(int customerID)
    {
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        HashMap<Integer, Booking> bookingsForCustomerID = new HashMap<Integer, Booking>();
        bookings = getBookings();
        int index = 0;
        
        for(Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
        {
            if(bookingEntry.getValue().getCustomerID() == customerID)
            {
                bookingsForCustomerID.put(index, bookingEntry.getValue());
                index++;
            }
        }        
        return bookingsForCustomerID;
    }
    
    public Booking getBookingFromBookingID(int bookingID)
    {
     HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
     bookings = getBookings();
     Booking booking = new Booking();
     
     for (Map.Entry<Integer, Booking> bookingEntry : bookings.entrySet())
     {
         if(bookingEntry.getValue().getBookingID() == bookingID)
         {
             return bookingEntry.getValue();
         }
     }
     return booking;
    }
    
    
    public BookingLine getBookingLineFromBookingLineID(int bookingLineID)
    {
     HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
     bookingLines = getBookingLines();
     BookingLine bookingLine = new BookingLine();
     
     for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
     {
         if(bookingLineEntry.getValue().getBookingLineID() == bookingLineID)
         {
             return bookingLineEntry.getValue();
         }
     }
     return bookingLine;
    }
    
    
    public double getBookingLineCostFromBookingLineID(int bookingLineID)
    {
     HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
     bookingLines = getBookingLines();
     double bookingLineCost = 0;
     
     for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
     {
         if(bookingLineEntry.getValue().getBookingLineID() == bookingLineID)
         {
             return bookingLineEntry.getValue().getLineCost();
         }
     }
     return bookingLineCost;
    }
    
    
    public HashMap<Integer, Customer> getCustomers()
    {
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        try 
        {
            String sqlString = "select * from Customers";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Customer customerToAdd = new Customer();
                customerToAdd.setCustomerID(rs.getInt("ID"));
                customerToAdd.setUsername(rs.getString("Username")); 
                customerToAdd.setPassword(rs.getString("Password"));        
                customerToAdd.setFirstName(rs.getString("FirstName"));
                customerToAdd.setLastName(rs.getString("LastName"));  
                customerToAdd.setDateOfBirth(rs.getDate("DateOfBirth"));
                customerToAdd.setEmail(rs.getString("Email"));  
                customerToAdd.setHouse(rs.getString("House"));
                customerToAdd.setStreet(rs.getString("Street"));  
                customerToAdd.setTown(rs.getString("Town"));
                customerToAdd.setPostcode(rs.getString("Postcode"));  
                customerToAdd.setTelephone(rs.getString("Telephone"));
                customerToAdd.setMobile(rs.getString("Mobile"));
                customers.put(customerToAdd.getCustomerID(), customerToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return customers;
    }
    
    public Customer getCustomerFromUsername(String username)
    {
        Customer customer = new Customer();
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        customers = getCustomers();
        
        for(Map.Entry<Integer, Customer> customerEntry : customers.entrySet())
        {
            if(customerEntry.getValue().getUsername().equals(username))
            {
                return customerEntry.getValue();
            }
        }
        return customer;
    }
    
    
    public Customer getCustomerFromCustomerID(int customerID)
    {
        Customer customer = new Customer();
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        customers = getCustomers();
        
        for(Map.Entry<Integer, Customer> customerEntry : customers.entrySet())
        {
            if(customerEntry.getValue().getCustomerID() == customerID)
            {
                return customerEntry.getValue();
            }
        }
        return customer;
    }
    
    
    public HashMap<Integer, Staff> getStaff()
    {
        HashMap<Integer, Staff> staff = new HashMap<Integer, Staff>();
        try 
        {
            String sqlString = "select * from Staff";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Staff staffToAdd = new Staff();
                staffToAdd.setStaffID(rs.getInt("ID"));
                staffToAdd.setUsername(rs.getString("Username")); 
                staffToAdd.setPassword(rs.getString("Password"));        
                staffToAdd.setFirstName(rs.getString("FirstName"));
                staffToAdd.setLastName(rs.getString("LastName"));  
                staffToAdd.setEmail(rs.getString("Email"));  
                staffToAdd.setStaffRoleID(rs.getInt("StaffRoleID"));
                staff.put(staffToAdd.getStaffID(), staffToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return staff;
    }
    
    
    public HashMap<Integer, StaffRole> getStaffRoles()
    {
        HashMap<Integer, StaffRole> staffRoles = new HashMap<Integer, StaffRole>();
        try 
        {
            String sqlString = "select * from StaffRoles";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                StaffRole staffRoleToAdd = new StaffRole();
                staffRoleToAdd.setStaffRoleID(rs.getInt("ID"));
                staffRoleToAdd.setStaffRole(rs.getString("StaffRole"));
                staffRoles.put(staffRoleToAdd.getStaffRoleID(), staffRoleToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return staffRoles;
    }
    
    
    public Staff getStaffFromUsername(String username)
    {
        Staff staffMember = new Staff();
        HashMap<Integer, Staff> staff = new HashMap<Integer, Staff>();
        staff = getStaff();
        
        for(Map.Entry<Integer, Staff> staffEntry : staff.entrySet())
        {
            if(staffEntry.getValue().getUsername().equals(username))
            {
                return staffEntry.getValue();
            }
        }
        return staffMember;
    }
    
    
    public Staff getStaffFromStaffID(int staffID)
    {
        Staff staffMember = new Staff();
        HashMap<Integer, Staff> staff = new HashMap<Integer, Staff>();
        staff = getStaff();
        
        for(Map.Entry<Integer, Staff> staffEntry : staff.entrySet())
        {
            if(staffEntry.getValue().getStaffID() == staffID)
            {
                return staffEntry.getValue();
            }
        }
        return staffMember;
    }
    
    
    public String getStaffRoleFromStaffRoleID(int staffRoleID)
    {
        String staffRole="";
        HashMap<Integer, StaffRole> staffRoles = new HashMap<Integer, StaffRole>();
        staffRoles = getStaffRoles();
        
        for(Map.Entry<Integer, StaffRole> staffRoleEntry : staffRoles.entrySet())
        {
            if(staffRoleEntry.getValue().getStaffRoleID() == staffRoleID)
            {
                return staffRoleEntry.getValue().getStaffRole();
            }
        }
        return staffRole;
    }
    
    
    public boolean checkLoginDetails(String username, String password)
    {
        boolean isValidLogin = false;
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        HashMap<Integer, Staff> staff = new HashMap<Integer, Staff>();
        customers = getCustomers();
        staff = getStaff();
        
        for(Map.Entry<Integer, Customer> customerEntry : customers.entrySet())
        {
            if(customerEntry.getValue().getUsername().equals(username))
            {
                if(customerEntry.getValue().getPassword().equals(password))
                {
                    return true;
                }
            }
        }
        
        for(Map.Entry<Integer, Staff> staffEntry : staff.entrySet())
        {
            if(staffEntry.getValue().getUsername().equals(username))
            {
                if(staffEntry.getValue().getPassword().equals(password))
                {
                    return true;
                }
            }
        }
        return isValidLogin;
    }
    
    
    public LoggedInUser getValidUser(String username)
    {
        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setUsername(username);
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        HashMap<Integer, Staff> staff = new HashMap<Integer, Staff>();
        customers = getCustomers();
        staff = getStaff();
        
        for(Map.Entry<Integer, Customer> customerEntry : customers.entrySet())
        {
            if(customerEntry.getValue().getUsername().equals(username))
            {
                loggedInUser.setUserTypeID(2);
                loggedInUser.setStaffTypeID(0);
                loggedInUser.setIsLoggedIn(true);
            }
        }
        
        for(Map.Entry<Integer, Staff> staffEntry : staff.entrySet())
        {
            if(staffEntry.getValue().getUsername().equals(username))
            {
                loggedInUser.setUserTypeID(3);
                loggedInUser.setStaffTypeID(staffEntry.getValue().getStaffRoleID());
                loggedInUser.setIsLoggedIn(true);
            }
        }
        return loggedInUser;
    }
    
    
    
    public HashMap<Integer, Room> getRooms()
    {
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        try 
        {
            String sqlString = "select * from Rooms";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Room roomToAdd = new Room();
                roomToAdd.setRoomID(rs.getInt("ID"));
                roomToAdd.setRoomTypeID(rs.getInt("RoomTypeID"));        
                rooms.put(roomToAdd.getRoomID(), roomToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return rooms;
    }
    
    public HashMap<Integer, Room> getRoomsOfRoomType(int roomTypeID)
    {
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        try 
        {
            String sqlString = "select * from Rooms where RoomTypeID = " + roomTypeID;
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Room roomToAdd = new Room();
                roomToAdd.setRoomID(rs.getInt("ID"));
                roomToAdd.setRoomTypeID(rs.getInt("RoomTypeID"));        
                rooms.put(roomToAdd.getRoomID(), roomToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return rooms;
    }
    
    
    public Room getRoomFromRoomID(int roomID)
    {
        Room room = new Room();
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        rooms = getRooms();
        
        for(Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            if(roomEntry.getValue().getRoomID() == roomID)
            {
                return roomEntry.getValue();
            }
        }
        return room;
    }
    
    public int getAvailableRoomID(Date checkIn, Date checkOut, String roomType)
    {
        int roomID = 0;
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        int roomTypeID = getRoomTypeIDFromString(roomType);
        rooms = getRoomsOfRoomType(roomTypeID);
            
        for(Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            if(isAvailable(checkIn, checkOut, roomEntry.getValue().getRoomID()))
            {
                return roomEntry.getValue().getRoomID();
            }
        }  
        return roomID;
    }
    
    public int getEditAvailableRoomID(int bookingLineID, Date checkIn, Date checkOut, String roomType)
    {
        int roomID = 0;
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        int roomTypeID = getRoomTypeIDFromString(roomType);
        rooms = getRoomsOfRoomType(roomTypeID);
            
        for(Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            if(isEditAvailable(bookingLineID, checkIn, checkOut, roomEntry.getValue().getRoomID()))
            {
                return roomEntry.getValue().getRoomID();
            }
        }  
        return roomID;
    }
    
    public int getTotalRoomsOfRoomType(String roomType)
    {
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        rooms = getRooms();
        int roomTypeID = getRoomTypeIDFromString(roomType);
        int available = 0;
        
        for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            if(roomEntry.getValue().getRoomTypeID() == (roomTypeID))
            {
                available = available + 1;
            }
        }
        return available;
    }
    
    
    public int getAvailability(Date checkIn, Date checkOut, String roomType)
    {
        int available = getTotalRoomsOfRoomType(roomType);
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        int roomTypeID = getRoomTypeIDFromString(roomType);
        rooms = getRoomsOfRoomType(roomTypeID);
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        bookingLines = getBookingLinesOfRoomType(roomType);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        try 
        {
            checkIn = fmt.parse(fmt.format(checkIn));
            checkOut = fmt.parse(fmt.format(checkOut));
        } catch (ParseException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
            {                
                if(bookingLineEntry.getValue().getRoomID() == roomEntry.getValue().getRoomID())
                {
                    try 
                    {
                        Date existingCheckIn = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckInDate()));
                        Date existingCheckOut = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckOutDate()));
                        if((!(checkOut.after(existingCheckIn))) == false && (!(checkIn.before(existingCheckOut)) == false))
                        {
                            available = available - 1;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return available;
    }
    
    
    public int getEditAvailability(int bookingLineID, Date checkIn, Date checkOut, String roomType)
    {
        int available = getTotalRoomsOfRoomType(roomType);
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        int roomTypeID = getRoomTypeIDFromString(roomType);
        rooms = getRoomsOfRoomType(roomTypeID);
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        bookingLines = getBookingLinesOfRoomType(roomType);
        
        int bookingLineToRemoveKey = 0;
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingLineID() == bookingLineID)
            {
                bookingLineToRemoveKey = bookingLineEntry.getKey();
            }
        }
        bookingLines.remove(bookingLineToRemoveKey);
        
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        try 
        {
            checkIn = fmt.parse(fmt.format(checkIn));
            checkOut = fmt.parse(fmt.format(checkOut));
        } catch (ParseException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
            {                
                if(bookingLineEntry.getValue().getRoomID() == roomEntry.getValue().getRoomID())
                {
                    try 
                    {
                        Date existingCheckIn = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckInDate()));
                        Date existingCheckOut = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckOutDate()));
                        if((!(checkOut.after(existingCheckIn))) == false && (!(checkIn.before(existingCheckOut)) == false))
                        {
                            available = available - 1;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return available;
    }
    
    
    public boolean isAvailable(Date checkIn, Date checkOut, int roomID)
    {
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        int roomTypeID = getRoomTypeIDFromRoomID(roomID);
        String roomType = getRoomTypeFromRoomTypeID(roomTypeID);
        bookingLines = getBookingLinesOfRoomType(roomType);
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            checkIn = fmt.parse(fmt.format(checkIn));
            checkOut = fmt.parse(fmt.format(checkOut));
        } catch (ParseException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getRoomID() == roomID)
            {
                try 
                {
                    Date existingCheckIn = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckInDate()));
                    Date existingCheckOut = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckOutDate()));
                    if((!(checkOut.after(existingCheckIn))) == false && (!(checkIn.before(existingCheckOut)) == false))
                    {
                        return false;
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
    }
    
    public boolean isEditAvailable(int bookingLineID, Date checkIn, Date checkOut, int roomID)
    {
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        int roomTypeID = getRoomTypeIDFromRoomID(roomID);
        String roomType = getRoomTypeFromRoomTypeID(roomTypeID);
        bookingLines = getBookingLinesOfRoomType(roomType);
        
        int bookingLineToRemoveKey = 0;
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingLineID() == bookingLineID)
            {
                bookingLineToRemoveKey = bookingLineEntry.getKey();
            }
        }
        bookingLines.remove(bookingLineToRemoveKey);
        
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        
        try {
            checkIn = fmt.parse(fmt.format(checkIn));
            checkOut = fmt.parse(fmt.format(checkOut));
        } catch (ParseException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getRoomID() == roomID)
            {
                try 
                {
                    Date existingCheckIn = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckInDate()));
                    Date existingCheckOut = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckOutDate()));
                    if((!(checkOut.after(existingCheckIn))) == false && (!(checkIn.before(existingCheckOut)) == false))
                    {
                        return false;
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return true;
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
    
    public HashMap<Integer, BookingLine> getBookingLinesOfRoomType(String roomType)
    {
        HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
        HashMap<Integer, BookingLine> bookingLinesOfRoomType = new HashMap<Integer, BookingLine>();
        bookingLines = getBookingLines();
        int index = 0;
        
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            int roomTypeID = getRoomTypeIDFromRoomID(bookingLineEntry.getValue().getRoomID());            
 
            if(getRoomTypeFromRoomTypeID(roomTypeID).equals(roomType))
            {
                bookingLinesOfRoomType.put(index, bookingLineEntry.getValue());
                index++;
            }
        }        
        return bookingLinesOfRoomType;
    }
    
    public int getRoomTypeIDFromRoomID(int roomID)
    {
        int roomTypeID = 0;
        HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
        rooms = getRooms();
        
        for(Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
        {
            if(roomEntry.getValue().getRoomID() == roomID)
            {
                return roomEntry.getValue().getRoomTypeID();
            }
        }
        return roomTypeID;
    }
    
    public String getRoomTypeFromRoomTypeID(int roomTypeID)
    {
     HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
     roomTypes = getRoomTypes();
     String roomType = "";
     
     for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
     {
         if(roomTypeEntry.getValue().getRoomTypeID() == roomTypeID)
         {
             return roomTypeEntry.getValue().getRoomType();
         }
     }
     return roomType;
    }
    
    
    public int getRoomTypeIDFromString(String roomType)
    {
     HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
     roomTypes = getRoomTypes();
     int roomTypeID = 0;
     
     for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
     {
         if(roomTypeEntry.getValue().getRoomType().equalsIgnoreCase(roomType))
         {
             roomTypeID = roomTypeEntry.getValue().getRoomTypeID();
         }
     }
     return roomTypeID;
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
    
    
    public int getPaymentTypeIDFromPaymentType(String paymentType)
    {
        HashMap<Integer, PaymentType> paymentTypes = new HashMap<Integer, PaymentType>();
        paymentTypes = getPaymentTypes();
        int paymentTypeID = 0;

        for (Map.Entry<Integer, PaymentType> paymentTypeEntry : paymentTypes.entrySet())
        {
            if(paymentTypeEntry.getValue().getPaymentType() == paymentType)
            {
                return paymentTypeEntry.getValue().getPaymentTypeID();
            }
        }
        return paymentTypeID;
    }
    
    public int getCardTypeIDFromCardType(String cardType)
    {
        HashMap<Integer, CardType> cardTypes = new HashMap<Integer, CardType>();
        cardTypes = getCardTypes();
        int cardTypeID = 0;

        for (Map.Entry<Integer, CardType> cardTypeEntry : cardTypes.entrySet())
        {
            if(cardTypeEntry.getValue().getCardType().equals(cardType))
            {
                return cardTypeEntry.getValue().getCardTypeID();
            }
        }
        return cardTypeID;
    }
    
    public HashMap<Integer, CardType> getCardTypes()
    {
        HashMap<Integer, CardType> cardTypes = new HashMap<Integer, CardType>();
        try 
        {
            String sqlString = "select * from CardTypes";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                CardType cardTypeToAdd = new CardType();
                cardTypeToAdd.setCardTypeID(rs.getInt("ID"));
                cardTypeToAdd.setCardType(rs.getString("CardType"));
                cardTypes.put(cardTypeToAdd.getCardTypeID(), cardTypeToAdd);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }            
        return cardTypes;
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
    
    
    public void updateCustomerDetails(int customerID, Customer newCustomerDetails)
    {        
        String firstName = newCustomerDetails.getFirstName();
        String lastName = newCustomerDetails.getLastName();
        Date dateOfBirth = newCustomerDetails.getDateOfBirth();
        String email = newCustomerDetails.getEmail();
        String house = newCustomerDetails.getHouse();
        String street = newCustomerDetails.getStreet();
        String town = newCustomerDetails.getTown();
        String postcode = newCustomerDetails.getPostcode();
        String telephone = newCustomerDetails.getTelephone();
        String mobile = newCustomerDetails.getMobile();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Customers SET FirstName = '" + firstName + 
                    "', LastName = '" + lastName + 
                    "', DateOfBirth = '" + dateFormat.format(dateOfBirth) + 
                    "', Email = '" + email + 
                    "', House = '" + house + 
                    "', Street = '" + street + 
                    "', Town = '" + town + 
                    "', Postcode = '" + postcode + 
                    "', Telephone = '" + telephone + 
                    "', Mobile = '" + mobile + 
                    "' WHERE ID = " + customerID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateStaffDetails(int staffID, Staff newStaffDetails)
    {        
        String firstName = newStaffDetails.getFirstName();
        String lastName = newStaffDetails.getLastName();
        String email = newStaffDetails.getEmail();
            
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Staff SET FirstName = '" + firstName + 
                    "', LastName = '" + lastName + 
                    "', Email = '" + email + 
                    "' WHERE ID = " + staffID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateRoomDetails(int roomID, Room newRoomDetails)
    {        
        int roomTypeID = newRoomDetails.getRoomTypeID();
            
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Rooms SET RoomTypeID = '" + roomTypeID + 
                    "' WHERE ID = " + roomID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateCustomerPassword(int customerID, String newPassword)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Customers SET Password = '" + newPassword + "' WHERE ID = " + customerID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateStaffPassword(int staffID, String newPassword)
    {        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE Staff SET Password = '" + newPassword + "' WHERE ID = " + staffID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    
    public void editBookingLineUpdateBookingTotalCost(int bookingID, double costToBeAdded)
    {        
        Booking bookingToBeUpdated = getBookingFromBookingID(bookingID);
        boolean isPaid = bookingToBeUpdated.getIsPaid();
        double outstandingBalance = bookingToBeUpdated.getOutstandingBalance();
        double newOutstandingBalance = outstandingBalance + costToBeAdded;
               
        if(costToBeAdded < 0)
        {
            try {
                Statement stmt = dbCon.createStatement();
                String sql = "UPDATE Bookings SET TotalCost = TotalCost + " + costToBeAdded  + 
                        ", OutstandingBalance = " + newOutstandingBalance  + 
                        " WHERE ID = " + bookingID;
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else
        {
            if( isPaid == true)
            {
                try {
                    Statement stmt = dbCon.createStatement();
                    String sql = "UPDATE Bookings SET TotalCost = TotalCost + " + costToBeAdded  + 
                            ", OutstandingBalance = " + newOutstandingBalance  + 
                            ", IsPaid = false" +
                            " WHERE ID = " + bookingID;
                    stmt.execute(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else
            {
                try {
                Statement stmt = dbCon.createStatement();
                String sql = "UPDATE Bookings SET TotalCost = TotalCost + " + costToBeAdded  + 
                        ", OutstandingBalance = " + newOutstandingBalance  + 
                        " WHERE ID = " + bookingID;
                stmt.execute(sql);
                } catch (SQLException ex) {
                    Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void updateBookingLine(int bookingLineID, BookingLine updatedBookingLineDetails, double changeInCost)
    {
        Date checkIn = updatedBookingLineDetails.getCheckInDate();
        Date checkOut = updatedBookingLineDetails.getCheckOutDate();
        int roomID = updatedBookingLineDetails.getRoomID();
        boolean breakfast = updatedBookingLineDetails.getBreakfast();
        boolean lunch = updatedBookingLineDetails.getLunch();
        boolean eveningMeal = updatedBookingLineDetails.getEveningMeal();
        int bookingID = updatedBookingLineDetails.getBookingID();
        double lineCost = updatedBookingLineDetails.getLineCost();
        double originalLineCost = getBookingLineCostFromBookingLineID(bookingLineID);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            Statement stmt = dbCon.createStatement();
            String sql = "UPDATE BookingLines SET CheckInDate = '" + dateFormat.format(checkIn) + 
                    "', CheckOutDate = '" + dateFormat.format(checkOut) + 
                    "', RoomID = " + roomID + 
                    ", Breakfast = " + breakfast + 
                    ", Lunch = " + lunch + 
                    ", EveningMeal = " + eveningMeal + 
                    ", LineCost = " + lineCost + 
                    " WHERE ID = " + bookingLineID;
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(lineCost != originalLineCost)
        {
            editBookingLineUpdateBookingTotalCost(bookingID, changeInCost);
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
    
    
    public void editBookingRemoveBookingLine(int bookingLineID)
    {
        int bookingID = 0;
        int noOfBookingLines = 0;
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
        for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingID() == bookingID)
            {
                noOfBookingLines++;
            }
        }
        if(noOfBookingLines > 1)
        {
            try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from BookingLines where ID = " + bookingLineID;
                stmt.execute(sql);
                editBookingLineUpdateBookingTotalCost(bookingID, 0 - bookingLineValue);
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
    
    public void removeBooking(int bookingID)
    {
        try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from BookingLines where BookingID = " + bookingID;
                stmt.execute(sql);
                String sql2 = "DELETE from Bookings where ID = " + bookingID;
                stmt.execute(sql2);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void removeCustomer(int customerID)
    {
        try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from Customers where ID = " + customerID;
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void removeStaff(int staffID)
    {
        try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from Staff where ID = " + staffID;
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void removeRoom(int roomID)
    {
        try {
                Statement stmt = dbCon.createStatement();
                String sql = "DELETE from Rooms where ID = " + roomID;
                stmt.execute(sql);
            } catch (SQLException ex) {
                Logger.getLogger(DBManager.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    
    public void removeBookingLine(int bookingLineID)
    {
        int bookingID = 0;
        int noOfBookingLines = 0;
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
        for (Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
        {
            if(bookingLineEntry.getValue().getBookingID() == bookingID)
            {
                noOfBookingLines++;
            }
        }
        if(noOfBookingLines > 1)
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