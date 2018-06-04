/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Paul
 */
public class Room {
    
    private int roomID;
    private int roomTypeID;
    private String roomName;
    private String roomImage;

    public Room() {
    }

    public Room(int roomID, int roomTypeID, String roomName, String roomImage) {
        this.roomID = roomID;
        this.roomTypeID = roomTypeID;
        this.roomName = roomName;
        this.roomImage = roomImage;
    }
    
        public Room(int roomTypeID, String roomName, String roomImage) {
        this.roomTypeID = roomTypeID;
        this.roomName = roomName;
        this.roomImage = roomImage;
    }
        

    public Room(int roomID, int roomTypeID) {
        this.roomID = roomID;
        this.roomTypeID = roomTypeID;
    }
    
    public Room(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getRoomTypeID() {
        return roomTypeID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomImage() {
        return roomImage;
    }
    
    

            
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public void setRoomTypeID(int roomTypeID) {
        this.roomTypeID = roomTypeID;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }
    
    
    
    
    
    
}
