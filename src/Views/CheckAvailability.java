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
import Models.MealType;
import Models.RoomType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
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
    
    Customer loggedInCustomer = new Customer();
    Booking currentBooking = loggedInCustomer.getCurrentBooking();

    /**
     * Creates new form CheckAvailability
     */
    

    
    public CheckAvailability(Date checkIn, Date checkOut, String roomType) {
        initComponents();
        this.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        populateRoomTypeDropDown();
        updateAvailability(checkIn, checkOut, roomType);
        currentBooking.populateBookingLines();
        DBManager db = new DBManager();
        txtAvailability.setText(String.valueOf(db.getAvailability(checkIn, checkOut, roomType)));
    }
    
    public CheckAvailability() {
        initComponents();
        this.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
        populateRoomTypeDropDown();
        currentBooking.populateBookingLines();
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

        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(235, 235, 235));

        jButton2.setText("About Drovers Lodge");

        btnRegister.setText("Register");

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 254, Short.MAX_VALUE)
                .addComponent(btnSignIn)
                .addGap(18, 18, 18)
                .addComponent(btnRegister)
                .addGap(18, 18, 18)
                .addComponent(btnCart)
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(btnRegister)
                    .addComponent(btnSignIn)
                    .addComponent(btnCart))
                .addContainerGap())
        );

        lblTitle.setBackground(new java.awt.Color(255, 255, 204));
        lblTitle.setFont(new java.awt.Font("Vivaldi", 1, 48)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 0, 0));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Drovers Lodge");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(205, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSignInActionPerformed

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
        db.addBookingLineToDb(newBookingLine);
        currentBooking = loggedInCustomer.getCurrentBooking();
        txtAvailability.setText(String.valueOf(db.getAvailability(checkIn, checkOut, roomType)));
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartActionPerformed
        Cart rForm = new Cart(currentBooking);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnCartActionPerformed

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
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox checkBreakfast;
    private javax.swing.JCheckBox checkEveningMeal;
    private javax.swing.JCheckBox checkLunch;
    private javax.swing.JComboBox<String> comboRoomType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
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
    private com.toedter.calendar.JDateChooser jdateCheckIn;
    private com.toedter.calendar.JDateChooser jdateCheckOut;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTextField txtAvailability;
    private javax.swing.JTextField txtNoOfDays;
    private javax.swing.JTextField txtPrice;
    // End of variables declaration//GEN-END:variables
}
