/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;


import Models.BookingLine;
import Models.CardType;
import Models.DBManager;
import Models.LoggedInUser;
import Models.Room;
import Models.RoomType;
import com.javaswingcomponents.calendar.cellrenderers.CalendarCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Paul
 */
public class About extends javax.swing.JFrame {
    //load user details
    LoggedInUser loggedInUser = new LoggedInUser();

    /**
     * displays contact info and allows users to view room details
     */
    public About(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        loadFrame();
    }
    
    //initialise components in frame
    public void loadFrame()
    {
        initComponents();
        this.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        Font ttfBase = null;
        Font ttfReal = null;
        try 
        {
            InputStream myStream = new BufferedInputStream(new FileInputStream("src/fonts/Dillanda Caligraphy Script Demo.ttf"));
            ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
            ttfReal = ttfBase.deriveFont(Font.BOLD, 50);
            lblTitle.setFont(ttfReal);
            lblTitle.setForeground(new Color(102, 0, 0));

        } catch (Exception ex) 
        {
            ex.printStackTrace();
            System.err.println("Custom font not loaded.");
        }
        if(!loggedInUser.getIsLoggedIn())
        {
            btnControlPanel.setVisible(false);
            lblStaff.setVisible(false); 
        }
        else
        {
            btnSignIn.setText("Logged in as " + this.loggedInUser.getUsername());
            btnSignIn.setEnabled(false);
            btnRegister.setText("Logout");
            txtUsername.setText("");
            txtPassword.setText("");
            btnControlPanel.setVisible(true);
            lblStaff.setVisible(false); 
            if(loggedInUser.getUserTypeID() == 3)
            {
                lblStaff.setVisible(true);
            }
        }  
        
        txtContactInfo.append("Drover’s Lodge Guest House\n");
        txtContactInfo.append("Dervaig,\n");
        txtContactInfo.append("Isle of Mull,\n");
        txtContactInfo.append("PA75 6QJ\n\n");
        txtContactInfo.append("Telephone Number: \n01688 400282");
        txtContactInfo.setDisabledTextColor(Color.black);
        txtContactInfo.setLineWrap(true);
        populateRoomTypeDropDown();
    }
    
    //add room types to drop down
    public void populateRoomTypeDropDown()
    {
        comboRoomType.removeAllItems();
        DBManager db = new DBManager();        
        HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
        roomTypes = db.getRoomTypes(); 
        comboRoomType.addItem("--Please Select--");
        for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
        {
            comboRoomType.addItem(roomTypeEntry.getValue().getRoomType());
        }
        populateRoomNameDropDown();
    }
    
     //add room names to drop down
    public void populateRoomNameDropDown()
    {
        comboRoomName.removeAllItems();
        comboRoomName.addItem("--Please Select--");
        comboRoomName.setEnabled(false);
        comboRoomType.addActionListener (new ActionListener () 
        {
            public void actionPerformed(ActionEvent e) 
            {
                DBManager db = new DBManager();        
                HashMap<Integer, Room> rooms = new HashMap<Integer, Room>();
                rooms = db.getRooms(); 

                if(comboRoomType.getSelectedIndex() == 0)
                {
                    comboRoomName.setEnabled(false);
                }
                else if (comboRoomType.getSelectedIndex() == 1)
                {
                    comboRoomName.removeAllItems();
                    comboRoomName.addItem("--Please Select--");
                    for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
                    {
                        if(roomEntry.getValue().getRoomTypeID() == 1)
                        {
                            comboRoomName.addItem(roomEntry.getValue().getRoomName());
                            comboRoomName.setEnabled(true);
                            loadRoomImage();
                        }
                    }
                }
                else if (comboRoomType.getSelectedIndex() == 2)
                {
                    comboRoomName.removeAllItems();
                    comboRoomName.addItem("--Please Select--");
                    for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
                    {
                        if(roomEntry.getValue().getRoomTypeID() == 2)
                        {
                            comboRoomName.addItem(roomEntry.getValue().getRoomName());
                            comboRoomName.setEnabled(true);
                            loadRoomImage();
                        }
                    }
                }
                else if (comboRoomType.getSelectedIndex() == 3)
                {
                    comboRoomName.removeAllItems();
                    comboRoomName.addItem("--Please Select--");
                    for (Map.Entry<Integer, Room> roomEntry : rooms.entrySet())
                    {
                        if(roomEntry.getValue().getRoomTypeID() == 3)
                        {
                            comboRoomName.addItem(roomEntry.getValue().getRoomName());
                            comboRoomName.setEnabled(true);
                            loadRoomImage();
                        }
                    }
                }
            }
        });
    }
    
    //load image of selected room
    public void loadRoomImage()
    {
        comboRoomName.addActionListener (new ActionListener () 
        {
            public void actionPerformed(ActionEvent e) {
                if(comboRoomName.getSelectedIndex() != 0)
                {
                    String roomName = String.valueOf(comboRoomName.getSelectedItem());
                    DBManager db = new DBManager();
                    Room selectedRoom = db.getRoomFromRoomName(roomName);
                    ImageIcon roomImage = new ImageIcon("src\\img\\rooms\\JPEG\\" + selectedRoom.getRoomImage());
                    lblRoomImage.setIcon(roomImage);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jframeLogin = new javax.swing.JFrame();
        jLabel10 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jframeAvailability = new javax.swing.JFrame();
        btnAvailabilityClose = new javax.swing.JButton();
        txtAvailabilityTitle = new javax.swing.JLabel();
        calendarAvailability = new com.javaswingcomponents.calendar.JSCCalendar();
        jframeRoomPrices = new javax.swing.JFrame();
        jLabel1 = new javax.swing.JLabel();
        btnPricesClose = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtRoomDetails = new javax.swing.JTextArea();
        lblRoomPic = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        comboRoomName = new javax.swing.JComboBox<>();
        comboRoomType = new javax.swing.JComboBox<>();
        lblRoomImage = new javax.swing.JLabel();
        btnViewAvailability = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblRoomCost = new javax.swing.JLabel();
        lblRoomAvailable = new javax.swing.JLabel();
        btnRoomPrices = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtContactInfo = new javax.swing.JTextArea();
        lblTitle = new javax.swing.JLabel();
        lblStaff = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnControlPanel = new javax.swing.JButton();
        btnMakeABooking = new javax.swing.JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Username");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Password");

        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Enter login details");

        javax.swing.GroupLayout jframeLoginLayout = new javax.swing.GroupLayout(jframeLogin.getContentPane());
        jframeLogin.getContentPane().setLayout(jframeLoginLayout);
        jframeLoginLayout.setHorizontalGroup(
            jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeLoginLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeLoginLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(btnLogin)
                        .addGap(51, 51, 51)
                        .addComponent(btnClose))
                    .addGroup(jframeLoginLayout.createSequentialGroup()
                        .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGap(31, 31, 31)
                        .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframeLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(91, 91, 91))
        );
        jframeLoginLayout.setVerticalGroup(
            jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(33, 33, 33)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnClose))
                .addGap(20, 20, 20))
        );

        btnAvailabilityClose.setText("Close");
        btnAvailabilityClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvailabilityCloseActionPerformed(evt);
            }
        });

        txtAvailabilityTitle.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtAvailabilityTitle.setForeground(new java.awt.Color(51, 0, 0));
        txtAvailabilityTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtAvailabilityTitle.setText("Availability of");
        txtAvailabilityTitle.setToolTipText("");
        txtAvailabilityTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jframeAvailabilityLayout = new javax.swing.GroupLayout(jframeAvailability.getContentPane());
        jframeAvailability.getContentPane().setLayout(jframeAvailabilityLayout);
        jframeAvailabilityLayout.setHorizontalGroup(
            jframeAvailabilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeAvailabilityLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtAvailabilityTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jframeAvailabilityLayout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(btnAvailabilityClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jframeAvailabilityLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(calendarAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jframeAvailabilityLayout.setVerticalGroup(
            jframeAvailabilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeAvailabilityLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtAvailabilityTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(calendarAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(btnAvailabilityClose)
                .addGap(24, 24, 24))
        );

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Room Details");

        btnPricesClose.setText("Close");
        btnPricesClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPricesCloseActionPerformed(evt);
            }
        });

        txtRoomDetails.setEditable(false);
        txtRoomDetails.setColumns(20);
        txtRoomDetails.setRows(5);
        jScrollPane5.setViewportView(txtRoomDetails);

        javax.swing.GroupLayout jframeRoomPricesLayout = new javax.swing.GroupLayout(jframeRoomPrices.getContentPane());
        jframeRoomPrices.getContentPane().setLayout(jframeRoomPricesLayout);
        jframeRoomPricesLayout.setHorizontalGroup(
            jframeRoomPricesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                .addGroup(jframeRoomPricesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                        .addGroup(jframeRoomPricesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframeRoomPricesLayout.createSequentialGroup()
                                .addGap(301, 301, 301)
                                .addComponent(btnPricesClose)))
                        .addGap(49, 49, 49)
                        .addComponent(lblRoomPic, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jframeRoomPricesLayout.setVerticalGroup(
            jframeRoomPricesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addGroup(jframeRoomPricesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeRoomPricesLayout.createSequentialGroup()
                        .addComponent(lblRoomPic, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 27, Short.MAX_VALUE))
                    .addComponent(jScrollPane5))
                .addGap(18, 18, 18)
                .addComponent(btnPricesClose)
                .addGap(24, 24, 24))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("View Room"));

        comboRoomName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                comboRoomNameMouseClicked(evt);
            }
        });
        comboRoomName.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboRoomNamePropertyChange(evt);
            }
        });

        comboRoomType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboRoomTypePropertyChange(evt);
            }
        });

        btnViewAvailability.setText("Check Availability");
        btnViewAvailability.setToolTipText("");
        btnViewAvailability.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAvailabilityActionPerformed(evt);
            }
        });

        jLabel3.setText("Room Type");

        jLabel4.setText("Room Name");

        btnRoomPrices.setText("Room Details");
        btnRoomPrices.setToolTipText("");
        btnRoomPrices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRoomPricesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboRoomName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(comboRoomType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRoomPrices, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnViewAvailability, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRoomCost, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblRoomAvailable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(lblRoomImage, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lblRoomImage, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addGap(4, 4, 4)
                        .addComponent(comboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboRoomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblRoomCost, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblRoomAvailable)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnRoomPrices)
                                .addGap(18, 18, 18)))
                        .addComponent(btnViewAvailability)))
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 0, 0));
        jLabel2.setText("Contact Info");
        jLabel2.setToolTipText("");

        txtContactInfo.setEditable(false);
        txtContactInfo.setColumns(20);
        txtContactInfo.setRows(5);
        txtContactInfo.setEnabled(false);
        jScrollPane2.setViewportView(txtContactInfo);

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 0, 0));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Drovers Lodge");

        lblStaff.setBackground(new java.awt.Color(255, 255, 51));
        lblStaff.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblStaff.setForeground(new java.awt.Color(51, 51, 51));
        lblStaff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStaff.setText("STAFF");
        lblStaff.setOpaque(true);

        jSeparator2.setEnabled(false);

        jPanel2.setBackground(new java.awt.Color(224, 224, 224));
        jPanel2.setToolTipText("");

        jButton2.setText("About Drovers Lodge");

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });

        btnSignIn.setText("Sign In");
        btnSignIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignInActionPerformed(evt);
            }
        });

        btnCart.setBackground(new java.awt.Color(51, 0, 0));
        btnCart.setForeground(new java.awt.Color(255, 255, 255));
        btnCart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/basket.png"))); // NOI18N
        btnCart.setText("Cart");
        btnCart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCartActionPerformed(evt);
            }
        });

        btnControlPanel.setBackground(new java.awt.Color(51, 0, 0));
        btnControlPanel.setForeground(new java.awt.Color(255, 255, 255));
        btnControlPanel.setText("Control Panel");
        btnControlPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnControlPanelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(btnControlPanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSignIn)
                .addGap(18, 18, 18)
                .addComponent(btnRegister)
                .addGap(18, 18, 18)
                .addComponent(btnCart)
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(btnSignIn)
                    .addComponent(btnRegister)
                    .addComponent(btnCart)
                    .addComponent(btnControlPanel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnMakeABooking.setText("Make a Booking");
        btnMakeABooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMakeABookingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(btnMakeABooking, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStaff)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnMakeABooking)
                        .addGap(10, 10, 10))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboRoomTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboRoomTypePropertyChange
        
    }//GEN-LAST:event_comboRoomTypePropertyChange

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        if(loggedInUser.getIsLoggedIn())
        {
            loggedInUser = new LoggedInUser();
            loggedInUser.setIsLoggedIn(false);
            btnSignIn.setText("Sign In");
            btnSignIn.setEnabled(true);
            btnRegister.setText("Register");
            lblStaff.setVisible(false);
            btnControlPanel.setVisible(false);
        }
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        jframeLogin.setVisible(true);
        jframeLogin.setSize(400,250);
        jframeLogin.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframeLogin.setLocation(dim.width/2-jframeLogin.getSize().width/2, dim.height/2-jframeLogin.getSize().height/2);
    }//GEN-LAST:event_btnSignInActionPerformed

    private void btnCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartActionPerformed
        Cart rForm = new Cart(loggedInUser);//currentBooking);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnCartActionPerformed

    private void btnControlPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnControlPanelActionPerformed
        if(loggedInUser.getUserTypeID() == 2)
        {
            CustomerHome rForm = new CustomerHome(loggedInUser);
            this.dispose();
            rForm.setVisible(true);
        } else if(loggedInUser.getUserTypeID() == 3)
        {
            StaffHome rForm = new StaffHome(loggedInUser);
            this.dispose();
            rForm.setVisible(true);
        }
    }//GEN-LAST:event_btnControlPanelActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username  =  txtUsername.getText();
        String password  =  txtPassword.getText();
        DBManager db= new DBManager();
        //check login details exist in db
        if(db.checkLoginDetails(username, password))
        {
            loggedInUser = db.getValidUser(username);
            loggedInUser.setIsLoggedIn(true);
            btnSignIn.setText("Logged in as " + loggedInUser.getUsername());
            btnSignIn.setEnabled(false);
            btnRegister.setText("Logout");
            txtUsername.setText("");
            txtPassword.setText("");
            btnControlPanel.setVisible(true);
            jframeLogin.dispose();
            if(loggedInUser.getUserTypeID() == 3)
            {
                lblStaff.setVisible(true);
            }
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        jframeLogin.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnViewAvailabilityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAvailabilityActionPerformed
        //check that room is selected and load availability calendar
        if(comboRoomName.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Please select a room");
        }
        else
        {
            jframeAvailability.setVisible(true);
            jframeAvailability.setSize(410,440);
            jframeAvailability.getContentPane().setBackground(Color.white);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            jframeAvailability.setLocation(dim.width/2-jframeAvailability.getSize().width/2, dim.height/2-jframeAvailability.getSize().height/2);
        
            String roomName = String.valueOf(comboRoomName.getSelectedItem());
            DBManager db = new DBManager();
            Room selectedRoom = db.getRoomFromRoomName(roomName);
            txtAvailabilityTitle.setText("Availability of " + roomName);

            HashMap<Integer, BookingLine> bookingLines = new HashMap<Integer, BookingLine>();
            bookingLines = db.getBookingLinesOfRoomID(selectedRoom.getRoomID());
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
            List<Date> datesBooked = new ArrayList<Date>();

            for(Map.Entry<Integer, BookingLine> bookingLineEntry : bookingLines.entrySet())
            {
                try 
                {
                    Date checkIn = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckInDate()));
                    Date checkOut = fmt.parse(fmt.format(bookingLineEntry.getValue().getCheckOutDate()));

                    for(Date date : getDaysBetweenDates(checkIn, checkOut))
                    {
                        datesBooked.add(date);
                    }                

                } catch (ParseException ex) 
                {
                    Logger.getLogger(About.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    for(Date date : datesBooked)
                    {
                        calendarAvailability.setSelectedDates(datesBooked);
                        calendarAvailability.disable();
                        for (Component c : calendarAvailability.getComponents()) 
                        {
                            if (c instanceof JTextField) { 
                               ((JTextField)c).setEnabled(false); 
                               ((JTextField)c).setDisabledTextColor(Color.BLACK);
                            }
                        }
                    }
        }
    }//GEN-LAST:event_btnViewAvailabilityActionPerformed

    private void btnAvailabilityCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvailabilityCloseActionPerformed
        jframeAvailability.dispose();
    }//GEN-LAST:event_btnAvailabilityCloseActionPerformed

    private void btnPricesCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPricesCloseActionPerformed
        jframeRoomPrices.dispose();
    }//GEN-LAST:event_btnPricesCloseActionPerformed

    private void btnRoomPricesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRoomPricesActionPerformed
        //check that room is selected and load room details
        if(comboRoomName.getSelectedIndex() == 0)
        {
            JOptionPane.showMessageDialog(null, "Please select a room");
        }
        else
        {        
            jframeRoomPrices.setVisible(true);
            jframeRoomPrices.setSize(690,390);
            jframeRoomPrices.getContentPane().setBackground(Color.white);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            jframeRoomPrices.setLocation(dim.width/2-jframeRoomPrices.getSize().width/2, dim.height/2-jframeRoomPrices.getSize().height/2);

            DBManager db = new DBManager();
            Room roomSelected = db.getRoomFromRoomName(String.valueOf(comboRoomName.getSelectedItem()));
            String roomName = roomSelected.getRoomName();
            String roomType = db.getRoomTypeFromRoomTypeID(roomSelected.getRoomTypeID());
            String roomPrice = "";
            if(roomSelected.getRoomTypeID() == 1)
            {
                roomPrice = "£55.00 per night";
            }
            else if(roomSelected.getRoomTypeID() == 2)
            {
                roomPrice = "£75.00 per night";
            }
            else if(roomSelected.getRoomTypeID() == 3)
            {
                roomPrice = "£90.00 per night";
            }
            String roomDescription = "";
            if(roomSelected.getRoomTypeID() == 1)
            {
                roomDescription = "A single room with a double bed \nwhich can sleep two people.";
            }
            else if(roomSelected.getRoomTypeID() == 2)
            {
                roomDescription = "A double room which has two \nsingle beds and a fold up sofa bed. \nThis can sleep up to three people.";
            }
            else if(roomSelected.getRoomTypeID() == 3)
            {
                roomDescription = "A family room with a double bed, \ntwo double beds, and a fold up \nsofa bed. This room can sleep up to five people.";
            }

            txtRoomDetails.setText(
            "Room Name: " + roomName + "\n" +
            "Room Type: " + roomType + "\n" +
            "Room Price: " + roomPrice + "\n" +
            "Room Description: " + roomDescription + "\n\n" +
            "Meal Prices \nBreakfast: £10.00 per day\nLunch: £9.50 per day\nEvening Meal: £25.00 per day"
            );
            txtRoomDetails.setWrapStyleWord(true);

            ImageIcon roomImage = new ImageIcon("src\\img\\rooms\\JPEG\\" + roomSelected.getRoomImage());
            lblRoomPic.setIcon(roomImage);
        }
    }//GEN-LAST:event_btnRoomPricesActionPerformed

    private void btnMakeABookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMakeABookingActionPerformed
        CheckAvailability rForm = new CheckAvailability(loggedInUser);//currentBooking);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnMakeABookingActionPerformed

    private void comboRoomNamePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboRoomNamePropertyChange

    }//GEN-LAST:event_comboRoomNamePropertyChange

    private void comboRoomNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_comboRoomNameMouseClicked

    }//GEN-LAST:event_comboRoomNameMouseClicked

    /**
     * returns a list of dates between two dates
     * @param startDate
     * @param endDate
     * @return list of dates
     */
    public List<Date> getDaysBetweenDates(Date startDate, Date endDate)
    {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        while (!(calendar.getTime().after(endDate)))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(About.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new About().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAvailabilityClose;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnControlPanel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnMakeABooking;
    private javax.swing.JButton btnPricesClose;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRoomPrices;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnViewAvailability;
    private com.javaswingcomponents.calendar.JSCCalendar calendarAvailability;
    private javax.swing.JComboBox<String> comboRoomName;
    private javax.swing.JComboBox<String> comboRoomType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JFrame jframeAvailability;
    private javax.swing.JFrame jframeLogin;
    private javax.swing.JFrame jframeRoomPrices;
    private javax.swing.JLabel lblRoomAvailable;
    private javax.swing.JLabel lblRoomCost;
    private javax.swing.JLabel lblRoomImage;
    private javax.swing.JLabel lblRoomPic;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel txtAvailabilityTitle;
    private javax.swing.JTextArea txtContactInfo;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextArea txtRoomDetails;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
