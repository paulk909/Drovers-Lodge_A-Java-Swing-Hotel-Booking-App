/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Models.Booking;
import Models.BookingLine;
import Models.Customer;
import Models.DBManager;
import Models.LoggedInUser;
import Models.MealType;
import Models.RoomType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Paul
 */
public class CheckAvailability extends javax.swing.JFrame {
    
    private LoggedInUser loggedInUser = new LoggedInUser();
    private Booking currentBooking = new Booking(); 

    /**
     * Creates new form CheckAvailability
     */
    

    
    public CheckAvailability(Date checkIn, Date checkOut, String roomType, LoggedInUser loggedInUser) {
        getUser(loggedInUser);
        loadFrame();
        updateAvailability(checkIn, checkOut, roomType);
        currentBooking.populateBookingLines();
        DBManager db = new DBManager();
        txtAvailability.setText(String.valueOf(db.getAvailability(checkIn, checkOut, roomType)));
    }
    
    public CheckAvailability(LoggedInUser loggedInUser) {
        getUser(loggedInUser);
        loadFrame();
        currentBooking.populateBookingLines();
    }
    
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
        populateRoomTypeDropDown();        
    }
    
    
    public void getUser(LoggedInUser loggedInUser)
    {
        this.loggedInUser = loggedInUser; 
    }
    
    
    public void refreshAvailability()
    {
        Date checkIn = jdateCheckIn.getDate();
        Date checkOut = jdateCheckOut.getDate();
        String roomType = String.valueOf(comboRoomType.getSelectedItem());
        updateAvailability(checkIn, checkOut, roomType);
    }
    
    
    public void updateAvailability(Date checkIn, Date checkOut, String roomType)
    {
        jdateCheckIn.setDate(checkIn);
        jdateCheckOut.setDate(checkOut);
        comboRoomType.setSelectedItem(roomType);
        boolean[] meals = new boolean[3];
        if(checkBreakfast.isSelected()) { meals[0] = true; }
        if(checkLunch.isSelected()) { meals[1] = true; }
        if(checkEveningMeal.isSelected()) { meals[2] = true; }
        
        long lengthOfStay = getLengthOfStay(checkIn, checkOut);
        txtNoOfDays.setText(String.valueOf(lengthOfStay));
        double price = calculatePrice(lengthOfStay, meals, getRoomTypeID(roomType));
        txtPrice.setText("£" + String.format("%.02f",price));
        DBManager db = new DBManager();
        txtAvailability.setText(String.valueOf(db.getAvailability(checkIn, checkOut, roomType)));
    }
    
    
    public double getMealPrice(boolean[] meals)
    {
        double mealPrice  = 0;
        double breakfastPrice  = 0;
        double lunchPrice  = 0;
        double eveningMealPrice  = 0;
        HashMap<Integer, MealType> mealTypes = new HashMap<Integer, MealType>();
        DBManager db = new DBManager();
        mealTypes = db.getMealTypes();
        
        for (Map.Entry<Integer, MealType> mealTypeEntry : mealTypes.entrySet())
        {
            if(mealTypeEntry.getValue().getMealType().endsWith("Breakfast"))
            {
                breakfastPrice = mealTypeEntry.getValue().getMealPrice();
            } 
            else if (mealTypeEntry.getValue().getMealType().endsWith("Lunch"))
            {
                lunchPrice = mealTypeEntry.getValue().getMealPrice();
            }
            else if (mealTypeEntry.getValue().getMealType().endsWith("Evening Meal"))
            {
                eveningMealPrice = mealTypeEntry.getValue().getMealPrice();
            } 
        }
        
        if(meals[0] == true)
        {
            mealPrice = mealPrice + breakfastPrice;
        }
        if(meals[1] == true)
        {
            mealPrice = mealPrice + lunchPrice;
        }
        if(meals[2] == true)
        {
            mealPrice = mealPrice + eveningMealPrice;
        }
               
        return mealPrice;
    }
    
    public int getRoomTypeID(String roomType)
    {
        int roomTypeID = 0;
        HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
        DBManager db = new DBManager();
        roomTypes = db.getRoomTypes();
        
        for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
        {
            if(roomTypeEntry.getValue().getRoomType().equals(roomType))
            {
                roomTypeID = roomTypeEntry.getValue().getRoomTypeID();
            }
        }
        return roomTypeID;
    }
    
    
    public double calculatePrice(long lengthOfStay, boolean[] meals, int roomTypeID)
    {
        double totalPrice = 0;
        double mealPrice = 0;
        mealPrice = getMealPrice(meals);
        double roomTypePrice = 0;
        
        HashMap<Integer, RoomType> roomTypes = new HashMap<Integer, RoomType>();
        DBManager db = new DBManager();
        roomTypes = db.getRoomTypes();
        
        for (Map.Entry<Integer, RoomType> roomTypeEntry : roomTypes.entrySet())
        {
            if(roomTypeEntry.getValue().getRoomTypeID() == roomTypeID)
            {
                roomTypePrice = roomTypeEntry.getValue().getRoomPrice();
            }
        }
        
        totalPrice = (roomTypePrice*lengthOfStay) + (mealPrice*lengthOfStay);       
        return totalPrice;
    }
    
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
    }
        
    public long getLengthOfStay(Date checkIn, Date checkOut)
    {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fCheckIn = fmt.parse(fmt.format(checkIn));
            Date fCheckOut = fmt.parse(fmt.format(checkOut));
            long length = fCheckOut.getTime() - fCheckIn.getTime();
            long lengthInDays = TimeUnit.DAYS.convert(length, TimeUnit.MILLISECONDS);
            return lengthInDays;
        } catch (ParseException ex) {
            Logger.getLogger(CheckAvailability.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
        
        

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jframeLogin = new javax.swing.JFrame();
        jLabel10 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        comboRoomType = new javax.swing.JComboBox<>();
        jdateCheckIn = new com.toedter.calendar.JDateChooser();
        jdateCheckOut = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        txtNoOfDays = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        txtPrice = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        checkBreakfast = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        checkLunch = new javax.swing.JCheckBox();
        checkEveningMeal = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        txtAvailability = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnControlPanel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblStaff = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Check Availability", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("From");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("To");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Room Type");

        comboRoomType.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                comboRoomTypePropertyChange(evt);
            }
        });

        jdateCheckIn.setBackground(new java.awt.Color(255, 255, 255));
        jdateCheckIn.setFocusable(false);
        jdateCheckIn.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateCheckInPropertyChange(evt);
            }
        });

        jdateCheckOut.setBackground(new java.awt.Color(255, 255, 255));
        jdateCheckOut.setFocusable(false);
        jdateCheckOut.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdateCheckOutPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jdateCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jdateCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(comboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jdateCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdateCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jLabel3))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(235, 235, 235));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtNoOfDays.setEditable(false);

        btnAdd.setBackground(new java.awt.Color(51, 0, 0));
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Add to Cart");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        txtPrice.setEditable(false);

        jLabel2.setText("No of Days");

        jLabel8.setText("Price");

        jPanel4.setBackground(new java.awt.Color(235, 235, 235));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Add Meals"));
        jPanel4.setToolTipText("");

        checkBreakfast.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                checkBreakfastPropertyChange(evt);
            }
        });

        jLabel5.setText("Breakfast");

        jLabel6.setText("Lunch");

        checkLunch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                checkLunchPropertyChange(evt);
            }
        });

        checkEveningMeal.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                checkEveningMealPropertyChange(evt);
            }
        });

        jLabel7.setText("Evening Meal");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(checkBreakfast)))
                .addGap(55, 55, 55)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(checkLunch))
                .addGap(49, 49, 49)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(checkEveningMeal)
                        .addGap(22, 22, 22)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkBreakfast)
                    .addComponent(checkLunch)
                    .addComponent(checkEveningMeal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        txtAvailability.setEditable(false);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Availability");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtNoOfDays, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel8)))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtAvailability)
                    .addComponent(btnAdd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNoOfDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdate)
                            .addComponent(btnAdd))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStaff)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jdateCheckInPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateCheckInPropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_jdateCheckInPropertyChange

    private void jdateCheckOutPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdateCheckOutPropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_jdateCheckOutPropertyChange

    private void comboRoomTypePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_comboRoomTypePropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_comboRoomTypePropertyChange

    private void checkBreakfastPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_checkBreakfastPropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_checkBreakfastPropertyChange

    private void checkLunchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_checkLunchPropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_checkLunchPropertyChange

    private void checkEveningMealPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_checkEveningMealPropertyChange
//        refreshAvailability();
    }//GEN-LAST:event_checkEveningMealPropertyChange

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        refreshAvailability();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        Date checkIn = jdateCheckIn.getDate();
        Date checkOut = jdateCheckOut.getDate();
        int roomID = 0;
        String roomType = String.valueOf(comboRoomType.getSelectedItem());
        
        DBManager db = new DBManager();
        roomID = db.getAvailableRoomID(checkIn, checkOut, roomType);
        
        boolean[] meals = new boolean[3];
        
        if(checkBreakfast.isSelected())
        {
            meals[0] = true;
        } else
        {
            meals[0] = false;
        }
        if(checkLunch.isSelected())
        {
            meals[1] = true;
        } else
        {
            meals[1] = false;
        }
        if(checkEveningMeal.isSelected())
        {
            meals[2] = true;
        } else
        {
            meals[2] = false;
        }
        long lengthOfStay = getLengthOfStay(checkIn, checkOut);
        double lineCost = calculatePrice(lengthOfStay, meals, db.getRoomTypeIDFromRoomID(roomID));
        
        BookingLine newBookingLine = new BookingLine(checkIn, checkOut, roomID, meals, lineCost);
        db.addBookingLineToDb(loggedInUser, newBookingLine);
//        currentBooking = loggedInCustomer.getCurrentBooking();
        txtAvailability.setText(String.valueOf(db.getAvailability(checkIn, checkOut, roomType)));
    }//GEN-LAST:event_btnAddActionPerformed

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

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username  =  txtUsername.getText();
        String password  =  txtPassword.getText();
        DBManager db= new DBManager();

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
            java.util.logging.Logger.getLogger(CheckAvailability.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CheckAvailability.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CheckAvailability.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CheckAvailability.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            //    new CheckAvailability().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnControlPanel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox checkBreakfast;
    private javax.swing.JCheckBox checkEveningMeal;
    private javax.swing.JCheckBox checkLunch;
    private javax.swing.JComboBox<String> comboRoomType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator2;
    private com.toedter.calendar.JDateChooser jdateCheckIn;
    private com.toedter.calendar.JDateChooser jdateCheckOut;
    private javax.swing.JFrame jframeLogin;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAvailability;
    private javax.swing.JTextField txtNoOfDays;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPrice;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
