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
public class LoggedInUser {
    
    private String username;
    private int userTypeID;
    private int staffTypeID;
    private boolean isLoggedIn;

    public LoggedInUser() {
        isLoggedIn = false;
    }

    public LoggedInUser(String username, int userTypeID, int staffTypeID) {
        this.username = username;
        this.userTypeID = userTypeID;
        this.staffTypeID = staffTypeID;
        isLoggedIn = false;
    }

    public String getUsername() {
        return username;
    }

    public int getUserTypeID() {
        return userTypeID;
    }

    public int getStaffTypeID() {
        return staffTypeID;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }
    
    

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserTypeID(int userTypeID) {
        this.userTypeID = userTypeID;
    }

    public void setStaffTypeID(int staffTypeID) {
        this.staffTypeID = staffTypeID;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    
    
}
