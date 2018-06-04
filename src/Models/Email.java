/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.HeadlessException;
import java.text.SimpleDateFormat;
import javax.mail.*;
import javax.mail.internet.AddressException.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author Paul Kerr
 */
public class Email {
        
    public void registerEmail(String email, String firstName)
    {
        String recipient = email;
        
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");
        
        Session s = Session.getDefaultInstance(p, new javax.mail.Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication("droverslodge1@gmail.com", "metallica123!");
                    }
                });
        
        try
        {
            MimeMessage mm = new MimeMessage(s);
            mm.setFrom(new InternetAddress("droverslodge1@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mm.setSubject("Registration");
            mm.setText("Hello, " + firstName + "!\n\n Thank you for registering with Drovers Lodge guest house.");
            
            Transport.send(mm);
            JOptionPane.showMessageDialog(null, "Email has been sent!");
        }
        catch (HeadlessException | MessagingException ex)
        {
            ex.getMessage();
        }
    
    }
    
    public void bookingEmail(String email, Booking booking)
    {
        String recipient = email;
        
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");
        
        Session s = Session.getDefaultInstance(p, new javax.mail.Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication("droverslodge1@gmail.com", "metallica123!");
                    }
                });
        
        try
        {
            DBManager db = new DBManager();
            String firstName = db.getCustomerFromCustomerID(booking.getCustomerID()).getFirstName();
            HashMap<Integer, BookingLine> bookingLines = booking.getBookingLines();
            int noOfBookingLines = bookingLines.size();
            StringBuilder bookingDetails = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
            {
                String bookingLineID = String.valueOf(bookingLineEntry.getValue().getBookingLineID());
                String roomName = db.getRoomFromRoomID(bookingLineEntry.getValue().getRoomID()).getRoomName();
                String checkIn = dateFormat.format(bookingLineEntry.getValue().getCheckInDate());
                String checkOut = dateFormat.format(bookingLineEntry.getValue().getCheckOutDate());
                String meals = "";
                boolean[] mealsArray = new boolean[3];
                mealsArray[0] = bookingLineEntry.getValue().getBreakfast();
                mealsArray[1] = bookingLineEntry.getValue().getLunch();
                mealsArray[2] = bookingLineEntry.getValue().getEveningMeal();
                
                if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, false, false})) 
                {
                    meals = "None";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, false, false})) 
                {
                    meals = "Breakfast";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, true, false})) 
                {
                    meals = "Lunch";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, false, true})) 
                {
                    meals = "Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, true, false})) 
                {
                    meals = "Breakfast & Lunch";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, false, true})) 
                {
                    meals = "Breakfast & Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, true, true})) 
                {
                    meals = "Lunch & Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, true, true})) 
                {
                    meals = "Breakfast, Lunch & Evening Meal";
                }

                
                String lineCost = "£" + String.format("%.02f",(bookingLineEntry.getValue().getLineCost()));
                
                
                bookingDetails.append("Booking Line ID: " + bookingLineID + 
                                        ", Room Name: " + roomName + 
                                        ", Check In: " + checkIn + 
                                        ", Check Out: " + checkOut + 
                                        ", Meals: " + meals + 
                                        ", Line Cost: " + lineCost + "\n\n " );
            }
            
            String bookingID = String.valueOf(booking.getBookingID());
            Date today = new Date();
            String dateBooked = dateFormat.format(today);
            String totalCost = "£" + String.format("%.02f",(booking.getTotalCost()));
            String paymentType = db.getPaymentTypeFromPaymentTypeID(booking.getPaymentTypeID());
            String outstandingBalance = "£" + String.format("%.02f",(booking.getOutstandingBalance()));
            
            MimeMessage mm = new MimeMessage(s);
            mm.setFrom(new InternetAddress("droverslodge1@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mm.setSubject("Booking Confirmation");
            mm.setText("Hello, " + firstName + "!\n\nThank you for booking with Drovers Lodge guest house.\n\n"
                    + "Your booking details are as follows:\n\n" +
                    "Booking ID: " + bookingID + ", Date Booked: " + dateBooked + 
                    ", Total Cost: " + totalCost + ", Payment Type: " + paymentType + ", Outstanding Balance: " + outstandingBalance + "\n\n" +
                    bookingDetails + "\n\n" + 
                    "We look forward to welcoming you to Drovers Lodge,\n\n"
                            + "The Drovers Lodge Team");
            
            Transport.send(mm);
            JOptionPane.showMessageDialog(null, "Booking has been confirmed");
        }
        catch (HeadlessException | MessagingException ex)
        {
            ex.getMessage();
        }
    
    }
    
    
        public void updateBookingEmail(String email, Booking booking)
    {
        String recipient = email;
        
        Properties p = new Properties();
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.socketFactory.port", "465");
        p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.port", "465");
        
        Session s = Session.getDefaultInstance(p, new javax.mail.Authenticator()
                {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication("droverslodge1@gmail.com", "metallica123!");
                    }
                });
        
        try
        {
            DBManager db = new DBManager();
            String firstName = db.getCustomerFromCustomerID(booking.getCustomerID()).getFirstName();
            HashMap<Integer, BookingLine> bookingLines = booking.getBookingLines();
            int noOfBookingLines = bookingLines.size();
            StringBuilder bookingDetails = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
            {
                String bookingLineID = String.valueOf(bookingLineEntry.getValue().getBookingLineID());
                String roomName = db.getRoomFromRoomID(bookingLineEntry.getValue().getRoomID()).getRoomName();
                String checkIn = dateFormat.format(bookingLineEntry.getValue().getCheckInDate());
                String checkOut = dateFormat.format(bookingLineEntry.getValue().getCheckOutDate());
                String meals = "";
                boolean[] mealsArray = new boolean[3];
                mealsArray[0] = bookingLineEntry.getValue().getBreakfast();
                mealsArray[1] = bookingLineEntry.getValue().getLunch();
                mealsArray[2] = bookingLineEntry.getValue().getEveningMeal();
                
                if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, false, false})) 
                {
                    meals = "None";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, false, false})) 
                {
                    meals = "Breakfast";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, true, false})) 
                {
                    meals = "Lunch";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, false, true})) 
                {
                    meals = "Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, true, false})) 
                {
                    meals = "Breakfast & Lunch";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, false, true})) 
                {
                    meals = "Breakfast & Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {false, true, true})) 
                {
                    meals = "Lunch & Evening Meal";
                }
                else if (ArrayUtils.isEquals(mealsArray, new boolean[] {true, true, true})) 
                {
                    meals = "Breakfast, Lunch & Evening Meal";
                }

                
                String lineCost = "£" + String.format("%.02f",(bookingLineEntry.getValue().getLineCost()));
                
                
                bookingDetails.append("Booking Line ID: " + bookingLineID + 
                                        ", Room Name: " + roomName + 
                                        ", Check In: " + checkIn + 
                                        ", Check Out: " + checkOut + 
                                        ", Meals: " + meals + 
                                        ", Line Cost: " + lineCost + "\n\n " );
            }
            
            String bookingID = String.valueOf(booking.getBookingID());
            Date today = new Date();
            String dateBooked = dateFormat.format(today);
            String totalCost = "£" + String.format("%.02f",(booking.getTotalCost()));
            String paymentType = db.getPaymentTypeFromPaymentTypeID(booking.getPaymentTypeID());
            String outstandingBalance = "£" + String.format("%.02f",(booking.getOutstandingBalance()));
            
            MimeMessage mm = new MimeMessage(s);
            mm.setFrom(new InternetAddress("droverslodge1@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mm.setSubject("Updated Booking Confirmation");
            mm.setText("Hello, " + firstName + "!\n\nYour booking has been amended for your stay at Drovers Lodge.\n\n"
                    + "Your updated booking details are as follows:\n\n" +
                    "Booking ID: " + bookingID + ", Date Booked: " + dateBooked + 
                    ", Total Cost: " + totalCost + ", Payment Type: " + paymentType + ", Outstanding Balance: " + outstandingBalance + "\n\n" +
                    bookingDetails + "\n\n" + 
                    "We look forward to welcoming you to Drovers Lodge,\n\n"
                            + "The Drovers Lodge Team");
            
            Transport.send(mm);
            JOptionPane.showMessageDialog(null, "Booking has been updated");
        }
        catch (HeadlessException | MessagingException ex)
        {
            ex.getMessage();
        }
    
    }
    
}
