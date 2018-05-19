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
public class Customer {
    
    private int customerID;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String email;
    private String house;
    private String street;
    private String town;
    private String postcode;
    private String telephone;
    private String mobile;
    private HashMap<Integer, Booking> bookingList;

    public Customer() {
        bookingList = new HashMap<Integer, Booking>();
    }

    public Customer(int custoemrID, String username, String password, String firstName, String lastName, Date dateOfBirth, String email, String house, String street, String town, String postcode, String telephone, String mobile) {
        this.customerID = customerID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.house = house;
        this.street = street;
        this.town = town;
        this.postcode = postcode;
        this.telephone = telephone;
        this.mobile = mobile;
        bookingList = new HashMap<Integer, Booking>();
    }

    public int getCustomerID() {
        return customerID;
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
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getHouse() {
        return house;
    }

    public String getStreet() {
        return street;
    }

    public String getTown() {
        return town;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public HashMap<Integer, Booking> getBookingList() {
        return bookingList;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
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
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setTelephone(String Telephone) {
        this.telephone = Telephone;
    }

    public void setMobile(String Mobile) {
        this.mobile = Mobile;
    }

    public void setBookingList(HashMap<Integer, Booking> BookingList) {
        this.bookingList = bookingList;
    }
    
    
    
}
