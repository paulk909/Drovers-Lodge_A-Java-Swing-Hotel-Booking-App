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
public class Room {
    
    private int roomID;
    private int roomTypeID;

    public Room() {
    }

    public Room(int roomID, int roomTypeID) {
        this.roomID = roomID;
        this.roomTypeID = roomTypeID;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getRoomTypeID() {
        return roomTypeID;
    }

    
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setRoomTypeID(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }
    
    
    
}
