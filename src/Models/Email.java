/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.awt.HeadlessException;
import javax.mail.*;
import javax.mail.internet.AddressException.*;
import java.util.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

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
}
