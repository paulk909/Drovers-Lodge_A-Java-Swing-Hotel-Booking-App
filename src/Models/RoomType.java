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
public class RoomType {
    
    private int roomTypeID;
    private String roomType;
    private double roomPrice;

    public RoomType() {
    }

    public RoomType(int roomTypeID, String roomType, double roomPrice) {
        this.roomTypeID = roomTypeID;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public int getRoomTypeID() {
        return roomTypeID;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomTypeID(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice = roomPrice;
    }
    
    
    
}
