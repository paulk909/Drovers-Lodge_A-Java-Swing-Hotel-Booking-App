/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

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
    private String staffRoleID;

    public Staff() {
    }

    public Staff(int staffID, String username, String password, String firstName, String lastName, String email, String staffRoleID) {
        this.staffID = staffID;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.staffRoleID = staffRoleID;
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

    public String getStaffRoleID() {
        return staffRoleID;
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

    public void setStaffRoleID(String staffRoleID) {
        this.staffRoleID = staffRoleID;
    }
    
    
    
}
