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
    
    
    public boolean addCustomer(Customer addCustomer)
    {      
        try {
            String username = addCustomer.getUsername();
            String password = addCustomer.getPassword();
            String firstName = addCustomer.getFirstName();
            String lastName = addCustomer.getLastName();
            Date dateOfBirth = addCustomer.getDateOfBirth();
            String email = addCustomer.getEmail();
            String house = addCustomer.getHouse();
            String street = addCustomer.getStreet();
            String town = addCustomer.getTown();
            String postcode = addCustomer.getPostcode();
            String telephone = addCustomer.getTelephone();
            String mobile = addCustomer.getMobile();
            
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
    
    public HashMap<Integer, Meal> getMeals()
    {
        HashMap<Integer, Meal> meals = new HashMap<Integer, Meal>();
        try 
        {
            String sqlString = "select * from Meals";
            Statement st = dbCon.createStatement();
            ResultSet rs = null;
            rs = st.executeQuery(sqlString);
            while(rs.next())        
            {
                Meal mealToAdd = new Meal();
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
    
    
    
}
