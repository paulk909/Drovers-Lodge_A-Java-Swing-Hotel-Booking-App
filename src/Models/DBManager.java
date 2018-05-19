/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author 30211275
 */
public class DBManager {
    
    public void addCustomer(Customer addCustomer)
    {
        Customer newCustomer = new Customer();
        newCustomer = addCustomer;
        
        String username = newCustomer.getUsername();
        String password = newCustomer.getPassword();
        String firstName = newCustomer.getFirstName();
        String lastName = newCustomer.getLastName();
        String dateOfBirth = newCustomer.getDateOfBirth();
        String email = newCustomer.getEmail();
        String house = newCustomer.getHouse();
        String street = newCustomer.getStreet();
        String town = newCustomer.getTown();
        String postcode = newCustomer.getPostcode();
        String telephone = newCustomer.getTelephone();
        String mobile = newCustomer.getMobile();
                
        try
        {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection conn = DriverManager.getConnection("jdbc:ucanaccess://src/Db/DroversLodgeDb.accdb");

        Statement stmt = conn.createStatement();
        String sql = "INSERT into Customers (Username, Password, FirstName, LastName, DateOfBirth, Email, House, Street, Town, Postcode, Telephone, Mobile)"
                + "VALUES ('"
                + username + "','" 
                + password + "','"
                + firstName + "','"
                + lastName + "'," 
                + dateOfBirth + ",'"
                + email + "','"
                + house + "','" 
                + street + "','"
                + town + "','"
                + postcode + "','" 
                + telephone + "','"
                + mobile + "')";
        stmt.executeUpdate(sql);
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }        
    }
    
}
