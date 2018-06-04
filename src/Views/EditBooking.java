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
import Models.MealType;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Paul
 */
public class EditBooking extends javax.swing.JFrame {
    
    private LoggedInUser loggedInUser = new LoggedInUser();
    private Booking currentBooking = new Booking();

    /**
     * Creates new form EditBooking
     */
    public EditBooking(int bookingID, LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        DBManager db = new DBManager();
        currentBooking = db.getBookingFromBookingID(bookingID);        
        currentBooking.populateBookingLines();
        loadFrame();
        loadCart();        
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
        
        btnConfirm.setVisible(false);
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
                            currentBookingLine.getEveningMeal(), "£" + String.format("%.02f",currentBookingLine.getLineCost()),
                            currentBookingLine.getIsCheckedIn(), currentBookingLine.getIsCheckedOut()});
        }
        txtBookingID.setText(String.valueOf(currentBooking.getBookingID()));
        txtTotalCost.setText("£" + String.format("%.02f",(currentBooking.getTotalCost())));
        populatePaymentTypeDropDown();
        int paymentTypeID = currentBooking.getPaymentTypeID();
        DBManager db = new DBManager();
        comboPaymentType.setSelectedIndex(currentBooking.getPaymentTypeID());
        comboPaymentType.setEnabled(false);
        populateCustomerDropDown();
        if(!loggedInUser.getIsLoggedIn())
        {
            comboCustomer.setEnabled(false);
        }
        else
        {
                comboCustomer.setSelectedItem(db.getCustomerFromCustomerID(currentBooking.getCustomerID()).getUsername());
                comboCustomer.setEnabled(false);
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

        jframeEditBookingLine = new javax.swing.JFrame();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jdateCheckIn = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jdateCheckOut = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        checkBreakfast = new javax.swing.JCheckBox();
        checkEveningMeal = new javax.swing.JCheckBox();
        checkLunch = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtBookingLineID = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtRoomID = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtAvailability = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtEditTotalCost = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtChangeInCost = new javax.swing.JTextField();
        btnEditSubmit = new javax.swing.JButton();
        btnEditClear = new javax.swing.JButton();
        btnEditClose = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        comboRoomType = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtLengthOfStay = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBookingLines = new javax.swing.JTable();
        btnRemove = new javax.swing.JButton();
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
        jPanel2 = new javax.swing.JPanel();
        btnAbout = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnControlPanel = new javax.swing.JButton();
        lblStaff = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnEdit = new javax.swing.JButton();
        btnCheckIn = new javax.swing.JButton();
        btnCheckOut = new javax.swing.JButton();

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Edit Booking Line");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Check In Date");

        jdateCheckIn.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Check Out Date");

        jdateCheckOut.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Meals"));

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Breakfast");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Lunch");

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Evening Meal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(checkBreakfast)
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(checkLunch)
                        .addGap(58, 58, 58)
                        .addComponent(checkEveningMeal))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(checkLunch)
                    .addComponent(checkEveningMeal)
                    .addComponent(checkBreakfast))
                .addContainerGap())
        );

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Booking Line ID");
        jLabel11.setToolTipText("");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Room ID");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Availability");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Total Cost");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Change in Cost");

        btnEditSubmit.setText("Submit");
        btnEditSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSubmitActionPerformed(evt);
            }
        });

        btnEditClear.setText("Clear");
        btnEditClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditClearActionPerformed(evt);
            }
        });

        btnEditClose.setText("Close");
        btnEditClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditCloseActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Room Type");
        jLabel16.setToolTipText("");

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Length of Stay");

        javax.swing.GroupLayout jframeEditBookingLineLayout = new javax.swing.GroupLayout(jframeEditBookingLine.getContentPane());
        jframeEditBookingLine.getContentPane().setLayout(jframeEditBookingLineLayout);
        jframeEditBookingLineLayout.setHorizontalGroup(
            jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEditTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtChangeInCost)
                                        .addComponent(txtAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                    .addGap(17, 17, 17)
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jdateCheckIn, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtBookingLineID, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jframeEditBookingLineLayout.createSequentialGroup()
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                    .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(jdateCheckOut, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                            .addGap(94, 94, 94)
                                            .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(txtLengthOfStay, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addComponent(comboRoomType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnUpdate)
                            .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                                .addComponent(btnEditSubmit)
                                .addGap(27, 27, 27)
                                .addComponent(btnEditClear)))
                        .addGap(28, 28, 28)
                        .addComponent(btnEditClose)))
                .addGap(29, 29, 29))
        );
        jframeEditBookingLineLayout.setVerticalGroup(
            jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeEditBookingLineLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(23, 23, 23)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtBookingLineID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jdateCheckIn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jdateCheckOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboRoomType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLengthOfStay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtRoomID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtEditTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(txtChangeInCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jframeEditBookingLineLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditSubmit)
                    .addComponent(btnEditClear)
                    .addComponent(btnEditClose))
                .addGap(22, 22, 22))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblBookingLines.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking Line ID", "Check In", "Check Out", "Room ID", "Room Type", "Breakfast", "Lunch", "Evening Meal", "Cost", "Checked In", "Checked Out"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblBookingLines);

        btnRemove.setText("Remove Booking Line");
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });

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
        btnConfirm.setText("Update Booking");
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
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(txtBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
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

        lblStaff.setBackground(new java.awt.Color(255, 255, 51));
        lblStaff.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblStaff.setForeground(new java.awt.Color(51, 51, 51));
        lblStaff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStaff.setText("STAFF");
        lblStaff.setOpaque(true);

        lblTitle.setBackground(new java.awt.Color(255, 255, 204));
        lblTitle.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 0, 0));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Drovers Lodge");

        jSeparator1.setEnabled(false);

        btnEdit.setText("Edit Booking Line");
        btnEdit.setToolTipText("");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnCheckIn.setText("Check In");
        btnCheckIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckInActionPerformed(evt);
            }
        });

        btnCheckOut.setText("Check Out");
        btnCheckOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckOutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(lblTitle)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCheckIn)
                                .addGap(26, 26, 26)
                                .addComponent(btnCheckOut)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEdit)
                                .addGap(18, 18, 18)
                                .addComponent(btnRemove)
                                .addGap(11, 11, 11))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                    .addComponent(btnEdit)
                    .addComponent(btnCheckIn)
                    .addComponent(btnCheckOut))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            BookingLine bookingLineToBeRemoved = db.getBookingLineFromBookingLineID(bookingLineID);
            int bookingID = bookingLineToBeRemoved.getBookingID();
            double deletedBookingLineCost = 0 - bookingLineToBeRemoved.getLineCost();
            db.editBookingRemoveBookingLine(bookingLineID);
            refreshCart(rowIndex);
            
            currentBooking = db.getBookingFromBookingID(bookingID);        
            currentBooking.populateBookingLines();
            clearCart();
            loadCart();
        }
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void comboPaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPaymentTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPaymentTypeActionPerformed

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmActionPerformed

    }//GEN-LAST:event_btnConfirmActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        loggedInUser = new LoggedInUser();
        loggedInUser.setIsLoggedIn(false);
        btnSignIn.setText("Sign In");
        btnSignIn.setEnabled(true);
        btnRegister.setText("Register");
        MainMenu rForm = new MainMenu();
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed

    }//GEN-LAST:event_btnSignInActionPerformed

      
    private void btnCartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCartActionPerformed
                Cart rForm = new Cart(loggedInUser);
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

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        BookingLine editableBookingLine = new BookingLine();
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        editableBookingLine = db.getBookingLineFromBookingLineID(bookingLineID);
        
        jframeEditBookingLine.setVisible(true);
        jframeEditBookingLine.setSize(370,620);
        jframeEditBookingLine.getContentPane().setBackground(Color.white);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframeEditBookingLine.setLocation(dim.width/2-jframeEditBookingLine.getSize().width/2, dim.height/2-jframeEditBookingLine.getSize().height/2);
        
        Date checkIn = editableBookingLine.getCheckInDate();
        Date checkOut = editableBookingLine.getCheckOutDate();
        boolean breakfast = editableBookingLine.getBreakfast();
        boolean lunch = editableBookingLine.getLunch();
        boolean eveningMeal = editableBookingLine.getEveningMeal();
        populateRoomTypeDropDown();
        int roomTypeID = db.getRoomTypeIDFromRoomID(editableBookingLine.getRoomID());
        
        txtBookingLineID.setText(String.valueOf(bookingLineID));
        txtBookingLineID.setEnabled(false);
        jdateCheckIn.setDate(checkIn);
        jdateCheckOut.setDate(checkOut);
        checkBreakfast.setSelected(breakfast);
        checkLunch.setSelected(lunch);
        checkEveningMeal.setSelected(eveningMeal);
        comboRoomType.setSelectedIndex(roomTypeID);
        
        Date editCheckIn = jdateCheckIn.getDate();
        Date editCheckOut = jdateCheckOut.getDate();
        int editRoomTypeID = comboRoomType.getSelectedIndex();
        String editRoomType = db.getRoomTypeFromRoomTypeID(editRoomTypeID);
        boolean editBreakfast = checkBreakfast.isSelected();
        boolean editLunch = checkLunch.isSelected();
        boolean editEveningMeal = checkEveningMeal.isSelected();
        boolean[] editMeals = new boolean[3];
        editMeals[0] = editBreakfast;
        editMeals[1] = editLunch;
        editMeals[2] = editEveningMeal;
        
        updateEditBookingLine(bookingLineID, editCheckIn, editCheckOut, editRoomType, editMeals);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnEditSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSubmitActionPerformed
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        int bookingID = db.getBookingLineFromBookingLineID(bookingLineID).getBookingID();        
        double originalLineCost = db.getBookingLineCostFromBookingLineID(bookingLineID);
        
        Date editCheckIn = jdateCheckIn.getDate();
        Date editCheckOut = jdateCheckOut.getDate();
        int editRoomTypeID = comboRoomType.getSelectedIndex();
        String editRoomType = db.getRoomTypeFromRoomTypeID(editRoomTypeID);
        boolean editBreakfast = checkBreakfast.isSelected();
        boolean editLunch = checkLunch.isSelected();
        boolean editEveningMeal = checkEveningMeal.isSelected();
        boolean[] editMeals = new boolean[3];
        editMeals[0] = editBreakfast;
        editMeals[1] = editLunch;
        editMeals[2] = editEveningMeal;
        
        int editRoomID = db.getEditAvailableRoomID(bookingLineID, editCheckIn, editCheckOut, editRoomType);
        long lengthOfStay = getLengthOfStay(editCheckIn, editCheckOut);
        double editLineCost = calculatePrice(lengthOfStay, editMeals, getRoomTypeID(editRoomType));
        double changeInCost = editLineCost - originalLineCost;
        
        BookingLine newBookingLineDetails = new BookingLine(editCheckIn, editCheckOut, bookingID, editRoomID, editMeals, editLineCost);
        db.updateBookingLine(bookingLineID, newBookingLineDetails, changeInCost);
        currentBooking = db.getBookingFromBookingID(bookingID);        
        currentBooking.populateBookingLines();
        
        int customerID = currentBooking.getCustomerID();
        String email = db.getCustomerFromCustomerID(customerID).getEmail();
        Email bookingEmail = new Email();
        bookingEmail.updateBookingEmail(email, currentBooking);
        

        int tblBookingLinesSelectedRow = tblBookingLines.getSelectedRow();
        clearCart();
        loadCart();
        tblBookingLines.setRowSelectionInterval(tblBookingLinesSelectedRow, tblBookingLinesSelectedRow);
    }//GEN-LAST:event_btnEditSubmitActionPerformed

    private void btnEditClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditClearActionPerformed
        BookingLine editableBookingLine = new BookingLine();
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        editableBookingLine = db.getBookingLineFromBookingLineID(bookingLineID);
        
        Date checkIn = editableBookingLine.getCheckInDate();
        Date checkOut = editableBookingLine.getCheckOutDate();
        boolean breakfast = editableBookingLine.getBreakfast();
        boolean lunch = editableBookingLine.getLunch();
        boolean eveningMeal = editableBookingLine.getEveningMeal();
        boolean[] meals = new boolean[3];
        meals[0] = breakfast;
        meals[1] = lunch;
        meals[2] = eveningMeal;
        populateRoomTypeDropDown();
        int roomTypeID = db.getRoomTypeIDFromRoomID(editableBookingLine.getRoomID());
        String roomType = db.getRoomTypeFromRoomTypeID(roomTypeID);
        
        txtBookingLineID.setText(String.valueOf(bookingLineID));
        txtBookingLineID.setEnabled(false);
        jdateCheckIn.setDate(checkIn);
        jdateCheckOut.setDate(checkOut);
        checkBreakfast.setSelected(breakfast);
        checkLunch.setSelected(lunch);
        checkEveningMeal.setSelected(eveningMeal);
        comboRoomType.setSelectedIndex(roomTypeID);
        txtChangeInCost.setText("");
        
        updateEditBookingLine(bookingLineID, checkIn, checkOut, roomType, meals);
    }//GEN-LAST:event_btnEditClearActionPerformed

    private void btnEditCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditCloseActionPerformed
        BookingLine editableBookingLine = new BookingLine();
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        editableBookingLine = db.getBookingLineFromBookingLineID(bookingLineID);
        
        Date checkIn = editableBookingLine.getCheckInDate();
        Date checkOut = editableBookingLine.getCheckOutDate();
        boolean breakfast = editableBookingLine.getBreakfast();
        boolean lunch = editableBookingLine.getLunch();
        boolean eveningMeal = editableBookingLine.getEveningMeal();
        boolean[] meals = new boolean[3];
        meals[0] = breakfast;
        meals[1] = lunch;
        meals[2] = eveningMeal;
        populateRoomTypeDropDown();
        int roomTypeID = db.getRoomTypeIDFromRoomID(editableBookingLine.getRoomID());
        String roomType = db.getRoomTypeFromRoomTypeID(roomTypeID);
        
        txtBookingLineID.setText(String.valueOf(bookingLineID));
        txtBookingLineID.setEnabled(false);
        jdateCheckIn.setDate(checkIn);
        jdateCheckOut.setDate(checkOut);
        checkBreakfast.setSelected(breakfast);
        checkLunch.setSelected(lunch);
        checkEveningMeal.setSelected(eveningMeal);
        comboRoomType.setSelectedIndex(roomTypeID);
        txtChangeInCost.setText("");
        
        updateEditBookingLine(bookingLineID, checkIn, checkOut, roomType, meals);
        jframeEditBookingLine.dispose();
    }//GEN-LAST:event_btnEditCloseActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        double lineCost = db.getBookingLineCostFromBookingLineID(bookingLineID);
        
        Date editCheckIn = jdateCheckIn.getDate();
        Date editCheckOut = jdateCheckOut.getDate();
        int editRoomTypeID = comboRoomType.getSelectedIndex();
        String editRoomType = db.getRoomTypeFromRoomTypeID(editRoomTypeID);
        boolean editBreakfast = checkBreakfast.isSelected();
        boolean editLunch = checkLunch.isSelected();
        boolean editEveningMeal = checkEveningMeal.isSelected();
        boolean[] editMeals = new boolean[3];
        editMeals[0] = editBreakfast;
        editMeals[1] = editLunch;
        editMeals[2] = editEveningMeal;
        
        updateEditBookingLine(bookingLineID, editCheckIn, editCheckOut, editRoomType, editMeals);
        
        long lengthOfStay = getLengthOfStay(editCheckIn, editCheckOut);
        txtLengthOfStay.setText(String.valueOf(lengthOfStay));
        double price = calculatePrice(lengthOfStay, editMeals, getRoomTypeID(editRoomType));
        double changeInCost = price - lineCost;
        txtChangeInCost.setText("£" + String.format("%.02f",changeInCost));
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        About rForm = new About(loggedInUser);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnAboutActionPerformed

    private void btnCheckInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckInActionPerformed
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        db.updateBookingLineCheckIn(bookingLineID, true);
    }//GEN-LAST:event_btnCheckInActionPerformed

    private void btnCheckOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckOutActionPerformed
        int bookingLineID = (Integer)tblBookingLines.getValueAt(tblBookingLines.getSelectedRow(), 0);
        DBManager db = new DBManager();
        db.updateBookingLineCheckOut(bookingLineID, true);
    }//GEN-LAST:event_btnCheckOutActionPerformed

    
    public void updateEditBookingLine(int bookingLineID, Date editCheckIn, Date editCheckOut, String editRoomType, boolean[] editMeals)
    {
        DBManager db = new DBManager();
        long lengthOfStay = getLengthOfStay(editCheckIn, editCheckOut);
        txtLengthOfStay.setText(String.valueOf(lengthOfStay));
        double price = calculatePrice(lengthOfStay, editMeals, getRoomTypeID(editRoomType));
        txtEditTotalCost.setText("£" + String.format("%.02f",price));
        txtAvailability.setText(String.valueOf(db.getEditAvailability(bookingLineID, editCheckIn, editCheckOut, editRoomType)));
        txtRoomID.setText(String.valueOf(db.getEditAvailableRoomID(bookingLineID, editCheckIn, editCheckOut, editRoomType)));
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
            java.util.logging.Logger.getLogger(EditBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditBooking.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new EditBooking().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnCheckIn;
    private javax.swing.JButton btnCheckOut;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnControlPanel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEditClear;
    private javax.swing.JButton btnEditClose;
    private javax.swing.JButton btnEditSubmit;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox checkBreakfast;
    private javax.swing.JCheckBox checkEveningMeal;
    private javax.swing.JCheckBox checkLunch;
    private javax.swing.JComboBox<String> comboCustomer;
    private javax.swing.JComboBox<String> comboPaymentType;
    private javax.swing.JComboBox<String> comboRoomType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JDateChooser jdateCheckIn;
    private com.toedter.calendar.JDateChooser jdateCheckOut;
    private javax.swing.JFrame jframeEditBookingLine;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblBookingLines;
    private javax.swing.JTextField txtAvailability;
    private javax.swing.JTextField txtBookingID;
    private javax.swing.JTextField txtBookingLineID;
    private javax.swing.JTextField txtChangeInCost;
    private javax.swing.JTextField txtEditTotalCost;
    private javax.swing.JTextField txtLengthOfStay;
    private javax.swing.JTextField txtRoomID;
    private javax.swing.JTextField txtTotalCost;
    // End of variables declaration//GEN-END:variables
}
