/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Models.Booking;
import Models.BookingLine;
import Models.CardType;
import Models.Customer;
import Models.DBManager;
import Models.Email;
import Models.LoggedInUser;
import Models.Payment;
import Models.PaymentType;
import Models.RoomType;
import Models.Staff;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Paul
 */
public class Cart extends javax.swing.JFrame {
    
    private LoggedInUser loggedInUser = new LoggedInUser();
    private Booking currentBooking = new Booking();
    

    /**
     * Creates new form Cart
     */
    public Cart(LoggedInUser loggedInUser){
        this.loggedInUser = loggedInUser;
        getCurrentBooking();
        loadFrame();
        loadCart();        
    }
    
   
    public void getCurrentBooking()
    {
        if(loggedInUser.getIsLoggedIn() == false)
        {
            DBManager db = new DBManager();
            Customer loggedInCustomer = new Customer();
            loggedInCustomer.setIsLoggedIn(false);
            if(loggedInCustomer.findCurrentBooking())
            {
                currentBooking = loggedInCustomer.getCurrentBooking();
                currentBooking.populateBookingLines();
            }            
        }else if(loggedInUser.getIsLoggedIn())
        {
            if(loggedInUser.getUserTypeID() == 2)
            {
                DBManager db = new DBManager();
                Customer loggedInCustomer = db.getCustomerFromUsername(loggedInUser.getUsername());
                loggedInCustomer.setIsLoggedIn(true);
                if(loggedInCustomer.findCurrentBooking())
                {
                    currentBooking = loggedInCustomer.getCurrentBooking();
                    currentBooking.populateBookingLines();
                }
            }else if(loggedInUser.getUserTypeID() == 3)
            {
                DBManager db = new DBManager();
                Staff loggedInStaff = db.getStaffFromUsername(loggedInUser.getUsername());
                loggedInStaff.setIsLoggedIn(true);
                if(loggedInStaff.findCurrentBooking())
                {
                    currentBooking = loggedInStaff.getCurrentBooking();
                    currentBooking.populateBookingLines();
                }
            }
        }
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
        
        if(loggedInUser.getIsLoggedIn())
        {
            btnSignIn.setText("Logged in as " + loggedInUser.getUsername());
            btnSignIn.setEnabled(false);
            btnRegister.setText("Logout");
        }
        lblStaff.setVisible(false);
        if(loggedInUser.getUserTypeID() == 3)
        {
            lblStaff.setVisible(true);
        }
        
        if(!loggedInUser.getIsLoggedIn())
        {
            btnControlPanel.setVisible(false);
        }
    }
    
    public void loadCart()
    {
        tblBookingLines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        DefaultTableModel model = (DefaultTableModel)tblBookingLines.getModel();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for(Map.Entry<Integer, BookingLine> bookingLineEntry : 
                currentBooking.getBookingLines().entrySet())
        {
            BookingLine currentBookingLine = bookingLineEntry.getValue();
            DBManager db = new DBManager();
            String roomType = db.getRoomTypeFromRoomTypeID(db.getRoomTypeIDFromRoomID(currentBookingLine.getRoomID()));
            
            model.addRow(new Object[]{currentBookingLine.getBookingLineID(), dateFormat.format(currentBookingLine.getCheckInDate()), dateFormat.format(currentBookingLine.getCheckOutDate()),
                            currentBookingLine.getRoomID(), roomType, currentBookingLine.getBreakfast(), currentBookingLine.getLunch(), 
                            currentBookingLine.getEveningMeal(), "£" + String.format("%.02f",currentBookingLine.getLineCost()) });
        }
        txtBookingID.setText(String.valueOf(currentBooking.getBookingID()));
        txtTotalCost.setText("£" + String.format("%.02f",(currentBooking.getTotalCost())));
        populatePaymentTypeDropDown();
        populateCustomerDropDown();
        if(!loggedInUser.getIsLoggedIn())
        {
            comboCustomer.setEnabled(false);
        }
        else
        {
            if(loggedInUser.getUserTypeID() == 2)
            {
                comboCustomer.setSelectedItem(loggedInUser.getUsername());
                comboCustomer.setEnabled(false);
            } 
            else if(loggedInUser.getUserTypeID() == 3)
            {
                comboCustomer.setEnabled(true);
            }
        }
    }
    
    
    public void refreshCart(int rowIndex)
    {
        ((DefaultTableModel)tblBookingLines.getModel()).removeRow(rowIndex);
        Customer loggedInCustomer = new Customer();
        if(loggedInUser.getIsLoggedIn())
        {
            DBManager db = new DBManager();
            loggedInCustomer = db.getCustomerFromUsername(loggedInUser.getUsername());
        }
        currentBooking = loggedInCustomer.getCurrentBooking();
        txtBookingID.setText(String.valueOf(currentBooking.getBookingID()));
        txtTotalCost.setText("£" + String.format("%.02f",(currentBooking.getTotalCost())));
    }
    
    public void clearCart()
    {
        DefaultTableModel model = (DefaultTableModel)tblBookingLines.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
        {
           model.removeRow(i); 
        }
    }
    
    public void emptyFrame()
    {
        clearCart();
        txtBookingID.setText("");
        txtTotalCost.setText("");
        comboPaymentType.setSelectedIndex(0);
        if(loggedInUser.getUserTypeID() == 3)
        {
            comboCustomer.setSelectedIndex(0);
        }
    }
    
    public void logoutFrame()
    {
        clearCart();
        txtBookingID.setText("");
        txtTotalCost.setText("");
        comboPaymentType.setSelectedIndex(0);
        comboCustomer.setSelectedIndex(0);
        if(loggedInUser.getUserTypeID() == 3)
        {
            comboCustomer.setSelectedIndex(0);
        }
        
    }
    
    public void populatePaymentTypeDropDown()
    {
        comboPaymentType.removeAllItems();
        DBManager db = new DBManager();        
        HashMap<Integer, PaymentType> paymentTypes = new HashMap<Integer, PaymentType>();
        paymentTypes = db.getPaymentTypes(); 
        comboPaymentType.addItem("--Please Select--");
        for (Map.Entry<Integer, PaymentType> paymentTypeEntry : paymentTypes.entrySet())
        {
            comboPaymentType.addItem(paymentTypeEntry.getValue().getPaymentType());
        }
    }
    
    
    public void populateCardTypeDropDown()
    {
        comboCardType.removeAllItems();
        DBManager db = new DBManager();        
        HashMap<Integer, CardType> cardTypes = new HashMap<Integer, CardType>();
        cardTypes = db.getCardTypes(); 
        comboCardType.addItem("--Please Select--");
        for (Map.Entry<Integer, CardType> cardTypeEntry : cardTypes.entrySet())
        {
            comboCardType.addItem(cardTypeEntry.getValue().getCardType());
        }
    }
    
    public void populateCustomerDropDown()
    {
        comboCustomer.removeAllItems();
        DBManager db = new DBManager();        
        HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();
        customers = db.getCustomers(); 
        comboCustomer.addItem("--Unassigned--");
        for (Map.Entry<Integer, Customer> customerEntry : customers.entrySet())
        {
            comboCustomer.addItem(customerEntry.getValue().getUsername());
        }
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
        jLabel5 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jframePayment = new javax.swing.JFrame();
        btnPaymentSubmit = new javax.swing.JButton();
        btnPaymentClear = new javax.swing.JButton();
        btnPaymentClose = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtPayeeName = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtCardNo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSecurityNo = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        comboCardType = new javax.swing.JComboBox<>();
        txtPaymentTotalCost = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jMonthExpiry = new com.toedter.calendar.JMonthChooser();
        jYearExpiry = new com.toedter.calendar.JYearChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtPaymentHouse = new javax.swing.JTextField();
        txtPaymentStreet = new javax.swing.JTextField();
        txtPaymentTown = new javax.swing.JTextField();
        txtPaymentPostcode = new javax.swing.JTextField();
        checkSameAddress = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBookingLines = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtBookingID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        comboPaymentType = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnConfirm = new javax.swing.JButton();
        txtTotalCost = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        comboCustomer = new javax.swing.JComboBox<>();
        btnRemove = new javax.swing.JButton();
        btnAddMoreRooms = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAbout = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnControlPanel = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblStaff = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Username");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Password");

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

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Enter login details");

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
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(31, 31, 31)
                        .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPassword)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframeLoginLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(91, 91, 91))
        );
        jframeLoginLayout.setVerticalGroup(
            jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeLoginLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(33, 33, 33)
                .addGroup(jframeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnClose))
                .addGap(20, 20, 20))
        );

        btnPaymentSubmit.setText("Submit");
        btnPaymentSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentSubmitActionPerformed(evt);
            }
        });

        btnPaymentClear.setText("Clear");
        btnPaymentClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentClearActionPerformed(evt);
            }
        });

        btnPaymentClose.setText("Close");
        btnPaymentClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaymentCloseActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Card Holder's Name");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Card No");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Security No");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Card Type");

        txtPaymentTotalCost.setEditable(false);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Total Cost");

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 0, 0));
        jLabel14.setText("Payment Details");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expiry Date");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jMonthExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jYearExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(70, 70, 70))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMonthExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jYearExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Payee's Address"));

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Address 1");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Address 2");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Town");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Postcode");

        checkSameAddress.setText("Same as account address");
        checkSameAddress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSameAddressActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPaymentHouse, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPaymentTown, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPaymentPostcode, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPaymentStreet, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(checkSameAddress)
                .addGap(54, 54, 54))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtPaymentHouse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtPaymentStreet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(txtPaymentTown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtPaymentPostcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(checkSameAddress)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jframePaymentLayout = new javax.swing.GroupLayout(jframePayment.getContentPane());
        jframePayment.getContentPane().setLayout(jframePaymentLayout);
        jframePaymentLayout.setHorizontalGroup(
            jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframePaymentLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframePaymentLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel14))
                    .addGroup(jframePaymentLayout.createSequentialGroup()
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframePaymentLayout.createSequentialGroup()
                                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(jframePaymentLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(txtSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel8)))
                                .addGap(32, 32, 32)
                                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPayeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframePaymentLayout.createSequentialGroup()
                                .addComponent(btnPaymentSubmit)
                                .addGap(54, 54, 54)
                                .addComponent(btnPaymentClear)
                                .addGap(61, 61, 61)
                                .addComponent(btnPaymentClose))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jframePaymentLayout.createSequentialGroup()
                                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addGap(32, 32, 32)
                                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txtPaymentTotalCost, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboCardType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(81, 81, 81)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jframePaymentLayout.setVerticalGroup(
            jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframePaymentLayout.createSequentialGroup()
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframePaymentLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel14)
                        .addGap(49, 49, 49)
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtPayeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jframePaymentLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jframePaymentLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(comboCardType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtPaymentTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframePaymentLayout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPaymentSubmit)
                    .addComponent(btnPaymentClear)
                    .addComponent(btnPaymentClose))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        tblBookingLines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking Line ID", "Check In", "Check Out", "Room ID", "Room Type", "Breakfast", "Lunch", "Evening Meal", "Cost"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblBookingLines);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Booking ID");

        txtBookingID.setEditable(false);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Payment Type");

        comboPaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPaymentTypeActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Total Cost");

        btnConfirm.setBackground(new java.awt.Color(51, 0, 0));
        btnConfirm.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirm.setText("Confirm Booking");
        btnConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmActionPerformed(evt);
            }
        });

        txtTotalCost.setEditable(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Customer");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(comboCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(txtTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(63, 63, 63)
                .addComponent(btnConfirm)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnConfirm)
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(comboCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        btnRemove.setText("Remove Booking Line");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

        btnAddMoreRooms.setText("Add More Rooms");
        btnAddMoreRooms.setToolTipText("");
        btnAddMoreRooms.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMoreRoomsActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(224, 224, 224));
        jPanel2.setToolTipText("");

        btnAbout.setText("About Drovers Lodge");
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });

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
                .addComponent(btnAbout)
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
                    .addComponent(btnAbout)
                    .addComponent(btnSignIn)
                    .addComponent(btnRegister)
                    .addComponent(btnCart)
                    .addComponent(btnControlPanel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTitle.setBackground(new java.awt.Color(255, 255, 204));
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

        jSeparator1.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddMoreRooms)
                .addGap(18, 18, 18)
                .addComponent(btnRemove)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(lblTitle)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(19, 19, 19))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStaff)
                        .addGap(23, 23, 23)
                        .addComponent(jSeparator1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemove)
                    .addComponent(btnAddMoreRooms))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void comboPaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPaymentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPaymentTypeActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        if(tblBookingLines.getSelectedRow() == -1)
        {
            //lblMessage.setText("No Booking Line Selected");
        }
        else
        {
            DefaultTableModel model = (DefaultTableModel)tblBookingLines.getModel();
            int bookingLineID = Integer.parseInt(String.valueOf(model.getValueAt(tblBookingLines.getSelectedRow(),0)));
            int rowIndex = tblBookingLines.getSelectedRow();
            DBManager db = new DBManager();
            db.removeBookingLine(bookingLineID);
            refreshCart(rowIndex);
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnAddMoreRoomsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMoreRoomsActionPerformed
        CheckAvailability rForm = new CheckAvailability(loggedInUser);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnAddMoreRoomsActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        if(loggedInUser.getIsLoggedIn())
        {
            loggedInUser = new LoggedInUser();
            loggedInUser.setIsLoggedIn(false);
            getCurrentBooking();
            btnSignIn.setText("Sign In");
            btnSignIn.setEnabled(true);
            btnRegister.setText("Register");
            lblStaff.setVisible(false);
            btnControlPanel.setVisible(false);
            logoutFrame();
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
//        Cart rForm = new Cart(currentBooking);
//        this.dispose();
//        rForm.setVisible(true);
    }//GEN-LAST:event_btnCartActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username  =  txtUsername.getText();
        String password  =  txtPassword.getText();
        DBManager db= new DBManager();

        if(db.checkLoginDetails(username, password))
        {
            loggedInUser = db.getValidUser(username);
            loggedInUser.setIsLoggedIn(true);
            getCurrentBooking();
            btnSignIn.setText("Logged in as " + loggedInUser.getUsername());
            btnSignIn.setEnabled(false);
            btnRegister.setText("Logout");
            txtUsername.setText("");
            txtPassword.setText("");
            btnControlPanel.setVisible(true);
            jframeLogin.dispose();
            
            Customer ccheck = db.getCustomerFromUsername(loggedInUser.getUsername());
            boolean bcheck = db.getCustomerFromUsername(loggedInUser.getUsername()).findCurrentBooking();
            Booking bbcheck = db.getCustomerFromUsername(loggedInUser.getUsername()).getCurrentBooking();
            
            if(loggedInUser.getUserTypeID() == 2)
            {
                Customer loggedInCustomer = db.getCustomerFromUsername(loggedInUser.getUsername());
                loggedInCustomer.setIsLoggedIn(true);
                comboCustomer.setSelectedItem(loggedInUser.getUsername());
                if(loggedInCustomer.findCurrentBooking())
                {
                    clearCart();
                    loadCart();
                }
                else
                {
                    if(db.checkForUnassignedBooking())
                    {
                        db.assignUnassignedBookingToUser(loggedInUser);
                        clearCart();
                        loadCart();
                    }
                }
            } 
            else if(loggedInUser.getUserTypeID() == 3)
            {
                Staff loggedInStaff = db.getStaffFromUsername(loggedInUser.getUsername());
                loggedInStaff.setIsLoggedIn(true);
                lblStaff.setVisible(true);
                comboCustomer.setEnabled(true);
                if(loggedInStaff.findCurrentBooking())
                {
                    clearCart();
                    loadCart();
                }
                else
                {
                    if(db.checkForUnassignedBooking())
                    {
                        db.assignUnassignedBookingToUser(loggedInUser);
                        clearCart();
                        loadCart();
                    }
                }
            }            
            
        } else
        {
                JOptionPane.showMessageDialog(null, "Login details not valid");
        }  

    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        jframeLogin.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed
        DBManager db = new DBManager();
        
        int paymentTypeID = db.getPaymentTypeIDFromPaymentType(String.valueOf(comboPaymentType.getSelectedItem()));
        int customerID = 0;
        if(loggedInUser.getUserTypeID() == 2)
        {
            customerID = db.getCustomerFromUsername(loggedInUser.getUsername()).getCustomerID();
            currentBooking.setCustomerID(customerID);
        }
        else if(loggedInUser.getUserTypeID() == 3)
        {
            customerID = db.getCustomerFromUsername(String.valueOf(comboCustomer.getSelectedItem())).getCustomerID();
            currentBooking.setCustomerID(customerID);
        }
        int bookingID = currentBooking.getBookingID();
        
        if(loggedInUser.getIsLoggedIn() == false)
        {
            JOptionPane.showMessageDialog(null, "Please log in to confirm booking");
        }
        else
        {
            if(comboPaymentType.getSelectedIndex() == 0 )
            {
                JOptionPane.showMessageDialog(null, "Please select a payment type");
            }
            else
            {
                if(comboCustomer.getSelectedIndex() == 0)
                {
                    JOptionPane.showMessageDialog(null, "Please select a customer");
                }
                else
                {
                    if(paymentTypeID == 1)
                    {
                        currentBooking.setOutstandingBalance(currentBooking.getTotalCost());
                        db.confirmBookingPayAtCheckIn(loggedInUser, bookingID, customerID);
                        JOptionPane.showMessageDialog(null, "Booking has been confirmed");
                        String email = db.getCustomerFromCustomerID(customerID).getEmail();
                        Email bookingEmail = new Email();
                        bookingEmail.bookingEmail(email, currentBooking);
                        emptyFrame();
                    }
                    else if(paymentTypeID == 2)
                    {
                        jframePayment.setVisible(true);
                        jframePayment.setSize(840,600); 
                        jframePayment.getContentPane().setBackground(Color.white); 
                        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                        jframePayment.setLocation(dim.width/2-jframePayment.getSize().width/2, dim.height/2-jframePayment.getSize().height/2);
                        jframePayment.setVisible(true);
                        populateCardTypeDropDown();
                        txtPaymentTotalCost.setText("£" + String.format("%.02f",(currentBooking.getTotalCost())));
                    }
                }
            }
        }
    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnPaymentSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentSubmitActionPerformed
        if(txtPayeeName.getText().isEmpty() || txtCardNo.getText().isEmpty() || txtSecurityNo.getText().isEmpty() ||
                comboCardType.getSelectedIndex()==0 || txtPaymentHouse.getText().isEmpty() ||
                txtPaymentTown.getText().isEmpty() || txtPaymentPostcode.getText().isEmpty())
        {
                JOptionPane.showMessageDialog(null, "Please fill in all payment fields");
        }
        else
        {
            int length = txtCardNo.getText().length();
            if(length < 10)
            {
                JOptionPane.showMessageDialog(null, "Card number must be at least 10 digits");
            }
            else
            {
                DBManager db = new DBManager();
                String payeeName = txtPayeeName.getText();
                String cardNo = txtCardNo.getText();
                String securityNo = txtSecurityNo.getText();
                int expiryMonth = jMonthExpiry.getMonth();
                String monthString;
                switch (expiryMonth) {
                    case 0:  monthString = "Jan";
                             break;
                    case 1:  monthString = "Feb";
                             break;
                    case 2:  monthString = "Mar";
                             break;
                    case 3:  monthString = "Apr";
                             break;
                    case 4:  monthString = "May";
                             break;
                    case 5:  monthString = "Jun";
                             break;
                    case 6:  monthString = "Jul";
                             break;
                    case 7:  monthString = "Aug";
                             break;
                    case 8:  monthString = "Sep";
                             break;
                    case 9: monthString = "Oct";
                             break;
                    case 10: monthString = "Nov";
                             break;
                    case 11: monthString = "Dec";
                             break;
                    default: monthString = "Invalid month";
                             break;
                }
                int expiryYear = jYearExpiry.getYear();      
                Date expiryDate = new Date();
                try 
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
                    expiryDate = dateFormat.parse(expiryYear + "-" + monthString + "-01 00:00:00");
                } 
                catch (ParseException ex) 
                {
                    Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
                }
                int cardTypeID = db.getCardTypeIDFromCardType(String.valueOf(comboCardType.getSelectedItem()));
                double totalCost = currentBooking.getTotalCost();
                
                String paymentHouse = txtPaymentHouse.getText();
                String paymentStreet = txtPaymentStreet.getText();
                String paymentTown = txtPaymentTown.getText();
                String paymentPostcode = txtPaymentPostcode.getText();
                
                Payment payment = new Payment(payeeName, cardNo, securityNo, expiryDate, cardTypeID, totalCost, paymentHouse, paymentStreet, paymentTown, paymentPostcode);

                int customerID = 0;
                if(loggedInUser.getUserTypeID() == 2)
                {
                    customerID = db.getCustomerFromUsername(loggedInUser.getUsername()).getCustomerID();
                }
                else if(loggedInUser.getUserTypeID() == 3)
                {
                    customerID = db.getCustomerFromUsername(String.valueOf(comboCustomer.getSelectedItem())).getCustomerID();
                }
                int bookingID = currentBooking.getBookingID();

                db.confirmBookingPayNow(loggedInUser, bookingID, customerID, payment);
                jframePayment.dispose();
                JOptionPane.showMessageDialog(null, "Booking has been confirmed");
                String email = db.getCustomerFromCustomerID(customerID).getEmail();
                Email bookingEmail = new Email();
                bookingEmail.bookingEmail(email, currentBooking);
                txtPayeeName.setText("");
                txtCardNo.setText("");
                txtSecurityNo.setText("");
                comboCardType.setSelectedIndex(0);
                checkSameAddress.setSelected(false);
                txtPaymentHouse.setText("");
                txtPaymentStreet.setText("");
                txtPaymentTown.setText("");
                txtPaymentPostcode.setText("");
                emptyFrame();
            }
        }      
        
        

    }//GEN-LAST:event_btnPaymentSubmitActionPerformed

    private void btnPaymentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentClearActionPerformed
        txtPayeeName.setText("");
        txtCardNo.setText("");
        txtSecurityNo.setText("");
        comboCardType.setSelectedIndex(0);
        checkSameAddress.setSelected(false);
        txtPaymentHouse.setText("");
        txtPaymentStreet.setText("");
        txtPaymentTown.setText("");
        txtPaymentPostcode.setText("");
    }//GEN-LAST:event_btnPaymentClearActionPerformed

    private void btnPaymentCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentCloseActionPerformed
        jframePayment.dispose();
    }//GEN-LAST:event_btnPaymentCloseActionPerformed

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

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        About rForm = new About(loggedInUser);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnAboutActionPerformed

    private void checkSameAddressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkSameAddressActionPerformed
        DBManager db = new DBManager();
        Customer paymentCustomer = new Customer();
        if(loggedInUser.getUserTypeID() == 2)
        {
            paymentCustomer = db.getCustomerFromUsername(loggedInUser.getUsername());
        }
        else if(loggedInUser.getUserTypeID() == 3)
        {
            paymentCustomer = db.getCustomerFromUsername(String.valueOf(comboCustomer.getSelectedItem()));
        }
        
        txtPaymentHouse.setText(paymentCustomer.getHouse());
        txtPaymentStreet.setText(paymentCustomer.getStreet());
        txtPaymentTown.setText(paymentCustomer.getTown());
        txtPaymentPostcode.setText(paymentCustomer.getPostcode());
        
    }//GEN-LAST:event_checkSameAddressActionPerformed

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
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new Cart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnAddMoreRooms;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnControlPanel;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnPaymentClear;
    private javax.swing.JButton btnPaymentClose;
    private javax.swing.JButton btnPaymentSubmit;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JCheckBox checkSameAddress;
    private javax.swing.JComboBox<String> comboCardType;
    private javax.swing.JComboBox<String> comboCustomer;
    private javax.swing.JComboBox<String> comboPaymentType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private com.toedter.calendar.JMonthChooser jMonthExpiry;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JYearChooser jYearExpiry;
    private javax.swing.JFrame jframeLogin;
    private javax.swing.JFrame jframePayment;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblBookingLines;
    private javax.swing.JTextField txtBookingID;
    private javax.swing.JTextField txtCardNo;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPayeeName;
    private javax.swing.JTextField txtPaymentHouse;
    private javax.swing.JTextField txtPaymentPostcode;
    private javax.swing.JTextField txtPaymentStreet;
    private javax.swing.JTextField txtPaymentTotalCost;
    private javax.swing.JTextField txtPaymentTown;
    private javax.swing.JTextField txtSecurityNo;
    private javax.swing.JTextField txtTotalCost;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
