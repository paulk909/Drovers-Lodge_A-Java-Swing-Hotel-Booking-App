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
public class StaffRole {
    
    private int staffRoleID;
    private String staffRole;

    public StaffRole() {
    }

    public StaffRole(int staffRoleID, String staffRole) {
        this.staffRoleID = staffRoleID;
        this.staffRole = staffRole;
    }

    public int getStaffRoleID() {
        return staffRoleID;
    }

    public String getStaffRole() {
        return staffRole;
    }

    public void setStaffRoleID(int staffRoleID) {
        this.staffRoleID = staffRoleID;
    }

    public void setStaffRole(String staffRole) {
        this.staffRole = staffRole;
    }
    
    
    
}
