/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Models.Booking;
import Models.CardType;
import Models.Customer;
import Models.DBManager;
import Models.ExcelWriter;
import Models.LoggedInUser;
import Models.Payment;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Paul
 */
public class StaffViewBookings extends javax.swing.JFrame {

    private LoggedInUser loggedInUser = new LoggedInUser();    
    private double refundDue = 0;
    private int refundBookingID = 0;
    private double amountDue = 0;
    private int paymentBookingID = 0;
    
    /**
     * Creates new form StaffViewBookings
     */
    public StaffViewBookings(LoggedInUser loggedInUser) {
        this.loggedInUser = loggedInUser;
        loadFrame();
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
        btnSignIn.setText("Logged in as " + loggedInUser.getUsername());
        btnSignIn.setEnabled(false);
        btnRegister.setText("Logout");
        loadBookingTable();
    }
    
    
    public void loadBookingTable()
    {
        tblBookings.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        DefaultTableModel model = (DefaultTableModel)tblBookings.getModel();
        HashMap<Integer, Booking> bookings = new HashMap<Integer, Booking>();
        DBManager db = new DBManager();
        bookings = db.getBookings();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for(Map.Entry<Integer, Booking> bookingEntry : 
                bookings.entrySet())
        {
            Booking currentBooking = bookingEntry.getValue();            
            model.addRow(new Object[]{currentBooking.getBookingID(), currentBooking.getCustomerID(), currentBooking.getIsPaid(), String.format("%.02f",currentBooking.getOutstandingBalance()), 
                String.format("%.02f",currentBooking.getTotalCost()), dateFormat.format(currentBooking.getDateBooked()) });
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
    
    public void populateRefundCardTypeDropDown()
    {
        comboRefundCardType.removeAllItems();
        DBManager db = new DBManager();        
        HashMap<Integer, CardType> cardTypes = new HashMap<Integer, CardType>();
        cardTypes = db.getCardTypes(); 
        comboRefundCardType.addItem("--Please Select--");
        for (Map.Entry<Integer, CardType> cardTypeEntry : cardTypes.entrySet())
        {
            comboRefundCardType.addItem(cardTypeEntry.getValue().getCardType());
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

        jframePayment = new javax.swing.JFrame();
        btnPaymentSubmit = new javax.swing.JButton();
        btnPaymentClear = new javax.swing.JButton();
        btnPaymentClose = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        txtPayeeName = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtCardNo = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtSecurityNo = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        comboCardType = new javax.swing.JComboBox<>();
        txtPaymentTotalCost = new javax.swing.JTextField();
        lblPaymentTotalCost = new javax.swing.JLabel();
        lblPaymentTitle = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jMonthExpiry = new com.toedter.calendar.JMonthChooser();
        jYearExpiry = new com.toedter.calendar.JYearChooser();
        jframeRefund = new javax.swing.JFrame();
        btnRefundSubmit = new javax.swing.JButton();
        btnRefundClear = new javax.swing.JButton();
        btnRefundClose = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        txtRefundPayeeName = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtRefundCardNo = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtRefundSecurityNo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        comboRefundCardType = new javax.swing.JComboBox<>();
        txtRefundTotalRefund = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jMonthRefundExpiry = new com.toedter.calendar.JMonthChooser();
        jYearRefundExpiry = new com.toedter.calendar.JYearChooser();
        btnEditBooking = new javax.swing.JButton();
        btnDeleteBooking = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAbout = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnSignIn = new javax.swing.JButton();
        btnCart = new javax.swing.JButton();
        btnControlPanel = new javax.swing.JButton();
        btnPayBooking = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBookings = new javax.swing.JTable();
        lblStaff = new javax.swing.JLabel();
        btnStaffExportBookingReport = new javax.swing.JButton();

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

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Card Holder's Name");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Card No");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Security No");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Card Type");

        txtPaymentTotalCost.setEditable(false);

        lblPaymentTotalCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPaymentTotalCost.setText("Total Cost");

        lblPaymentTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblPaymentTitle.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        lblPaymentTitle.setForeground(new java.awt.Color(51, 0, 0));
        lblPaymentTitle.setText("Payment Details");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Expiry Date");

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
                .addComponent(jLabel24)
                .addGap(70, 70, 70))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMonthExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jYearExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addComponent(lblPaymentTitle))
                    .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframePaymentLayout.createSequentialGroup()
                            .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20)
                                .addGroup(jframePaymentLayout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(txtSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel18)))
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
                                .addComponent(jLabel21)
                                .addComponent(lblPaymentTotalCost))
                            .addGap(32, 32, 32)
                            .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtPaymentTotalCost, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboCardType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jframePaymentLayout.setVerticalGroup(
            jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframePaymentLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblPaymentTitle)
                .addGap(49, 49, 49)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(txtPayeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframePaymentLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jframePaymentLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(comboCardType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPaymentTotalCost)
                    .addComponent(txtPaymentTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jframePaymentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPaymentSubmit)
                    .addComponent(btnPaymentClear)
                    .addComponent(btnPaymentClose))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        btnRefundSubmit.setText("Submit");
        btnRefundSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundSubmitActionPerformed(evt);
            }
        });

        btnRefundClear.setText("Clear");
        btnRefundClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundClearActionPerformed(evt);
            }
        });

        btnRefundClose.setText("Close");
        btnRefundClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefundCloseActionPerformed(evt);
            }
        });

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Card Holder's Name");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Card No");

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Security No");

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Card Type");

        txtRefundTotalRefund.setEditable(false);

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Total Refund");

        jLabel28.setBackground(new java.awt.Color(255, 255, 255));
        jLabel28.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 0, 0));
        jLabel28.setText("Refund Details");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Expiry Date");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jMonthRefundExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jYearRefundExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(70, 70, 70))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel29)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jMonthRefundExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jYearRefundExpiry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jframeRefundLayout = new javax.swing.GroupLayout(jframeRefund.getContentPane());
        jframeRefund.getContentPane().setLayout(jframeRefundLayout);
        jframeRefundLayout.setHorizontalGroup(
            jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeRefundLayout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeRefundLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(jLabel28))
                    .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframeRefundLayout.createSequentialGroup()
                            .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel25)
                                .addGroup(jframeRefundLayout.createSequentialGroup()
                                    .addGap(10, 10, 10)
                                    .addComponent(txtRefundSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel22)))
                            .addGap(32, 32, 32)
                            .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtRefundPayeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtRefundCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jframeRefundLayout.createSequentialGroup()
                            .addComponent(btnRefundSubmit)
                            .addGap(54, 54, 54)
                            .addComponent(btnRefundClear)
                            .addGap(61, 61, 61)
                            .addComponent(btnRefundClose))
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jframeRefundLayout.createSequentialGroup()
                            .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel26)
                                .addComponent(jLabel27))
                            .addGap(32, 32, 32)
                            .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtRefundTotalRefund, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(comboRefundCardType, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(66, Short.MAX_VALUE))
        );
        jframeRefundLayout.setVerticalGroup(
            jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jframeRefundLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel28)
                .addGap(49, 49, 49)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtRefundPayeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRefundCardNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jframeRefundLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jframeRefundLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRefundSecurityNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(comboRefundCardType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtRefundTotalRefund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jframeRefundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefundSubmit)
                    .addComponent(btnRefundClear)
                    .addComponent(btnRefundClose))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnEditBooking.setText("Edit Booking");
        btnEditBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditBookingActionPerformed(evt);
            }
        });

        btnDeleteBooking.setText("Delete Booking");
        btnDeleteBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBookingActionPerformed(evt);
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

        btnPayBooking.setText("Pay For Booking");
        btnPayBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayBookingActionPerformed(evt);
            }
        });

        jLabel1.setText("All Bookings");

        lblTitle.setBackground(new java.awt.Color(255, 255, 255));
        lblTitle.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(102, 0, 0));
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("Drovers Lodge");

        tblBookings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking ID", "Customer ID", "Is Paid", "Outstanding Balance", "Total Cost", "Date Booked"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblBookings);

        lblStaff.setBackground(new java.awt.Color(255, 255, 51));
        lblStaff.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblStaff.setForeground(new java.awt.Color(51, 51, 51));
        lblStaff.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStaff.setText("STAFF");
        lblStaff.setOpaque(true);

        btnStaffExportBookingReport.setText("Export Booking Report");
        btnStaffExportBookingReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStaffExportBookingReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(lblTitle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 132, Short.MAX_VALUE)
                        .addComponent(lblStaff, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnStaffExportBookingReport)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPayBooking)
                                .addGap(18, 18, 18)
                                .addComponent(btnEditBooking)
                                .addGap(18, 18, 18)
                                .addComponent(btnDeleteBooking)))
                        .addGap(32, 32, 32))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStaff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteBooking)
                    .addComponent(btnEditBooking)
                    .addComponent(btnPayBooking)
                    .addComponent(btnStaffExportBookingReport))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditBookingActionPerformed
        int bookingID = (Integer)tblBookings.getValueAt(tblBookings.getSelectedRow(), 0);
        EditBooking rForm = new EditBooking(bookingID, loggedInUser);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnEditBookingActionPerformed

    private void btnDeleteBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBookingActionPerformed

        int bookingID = (Integer)tblBookings.getValueAt(tblBookings.getSelectedRow(), 0);
        DBManager db = new DBManager();
        Booking selectedBooking = db.getBookingFromBookingID(bookingID);
        if(selectedBooking.getIsPaid())
        {
            refundDue = selectedBooking.getTotalCost() - selectedBooking.getOutstandingBalance();
            refundBookingID = selectedBooking.getBookingID();
            db.removeBooking(bookingID);

            jframeRefund.setVisible(true);
            jframeRefund.setSize(440,600);
            jframeRefund.getContentPane().setBackground(Color.white);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            jframeRefund.setLocation(dim.width/2-jframeRefund.getSize().width/2, dim.height/2-jframeRefund.getSize().height/2);
            jframeRefund.setVisible(true);
            populateRefundCardTypeDropDown();
            txtRefundTotalRefund.setText("£" + String.format("%.02f",(refundDue)));
        } else
        {
            double totalCost = selectedBooking.getTotalCost();
            double outstandingBalance = selectedBooking.getOutstandingBalance();

            if((totalCost - outstandingBalance) > 0)
            {
                refundDue = totalCost - outstandingBalance;
                refundBookingID = selectedBooking.getBookingID();
                db.removeBooking(bookingID);

                jframeRefund.setVisible(true);
                jframeRefund.setSize(440,600);
                jframeRefund.getContentPane().setBackground(Color.white);
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                jframeRefund.setLocation(dim.width/2-jframeRefund.getSize().width/2, dim.height/2-jframeRefund.getSize().height/2);
                jframeRefund.setVisible(true);
                populateRefundCardTypeDropDown();
                txtRefundTotalRefund.setText("£" + String.format("%.02f",(refundDue)));
            } else
            {
                db.removeBooking(bookingID);
            }
        }
    }//GEN-LAST:event_btnDeleteBookingActionPerformed

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

    private void btnPayBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayBookingActionPerformed
        int bookingID = (Integer)tblBookings.getValueAt(tblBookings.getSelectedRow(), 0);
        DBManager db = new DBManager();
        Booking selectedBooking = db.getBookingFromBookingID(bookingID);
        if(selectedBooking.getOutstandingBalance() < 0)
        {
            refundDue = selectedBooking.getTotalCost() - selectedBooking.getOutstandingBalance();
            refundBookingID = selectedBooking.getBookingID();

            jframeRefund.setVisible(true);
            jframeRefund.setSize(440,600);
            jframeRefund.getContentPane().setBackground(Color.white);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            jframeRefund.setLocation(dim.width/2-jframeRefund.getSize().width/2, dim.height/2-jframeRefund.getSize().height/2);
            jframeRefund.setVisible(true);
            populateRefundCardTypeDropDown();
            txtRefundTotalRefund.setText("£" + String.format("%.02f",(refundDue)));
        } else if(selectedBooking.getOutstandingBalance() > 0)
        {
            amountDue = selectedBooking.getOutstandingBalance();
            paymentBookingID = selectedBooking.getBookingID();

            jframePayment.setVisible(true);
            jframePayment.setSize(440,600);
            jframePayment.getContentPane().setBackground(Color.white);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            jframePayment.setLocation(dim.width/2-jframePayment.getSize().width/2, dim.height/2-jframePayment.getSize().height/2);
            jframePayment.setVisible(true);
            populateCardTypeDropDown();
            txtPaymentTotalCost.setText("£" + String.format("%.02f",(amountDue)));
        }
    }//GEN-LAST:event_btnPayBookingActionPerformed

    private void btnPaymentSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentSubmitActionPerformed
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

        Payment payment = new Payment(payeeName, cardNo, securityNo, expiryDate, cardTypeID, amountDue, paymentBookingID);
        db.takePayment(payment);
        db.setOutstandingPaymentZeroIsPaidTrue(paymentBookingID);
        amountDue = 0;
        paymentBookingID = 0;
        jframePayment.dispose();
        loadBookingTable();
    }//GEN-LAST:event_btnPaymentSubmitActionPerformed

    private void btnPaymentClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentClearActionPerformed
        txtPayeeName.setText("");
        txtCardNo.setText("");
        txtSecurityNo.setText("");
    }//GEN-LAST:event_btnPaymentClearActionPerformed

    private void btnPaymentCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaymentCloseActionPerformed
        jframePayment.dispose();
    }//GEN-LAST:event_btnPaymentCloseActionPerformed

    private void btnRefundSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundSubmitActionPerformed
        DBManager db = new DBManager();
        String refundPayeeName = txtRefundPayeeName.getText();
        String refundCardNo = txtRefundCardNo.getText();
        String refundSecurityNo = txtRefundSecurityNo.getText();
        int refundExpiryMonth = jMonthRefundExpiry.getMonth();
        String refundMonthString;
        switch (refundExpiryMonth) {
            case 0:  refundMonthString = "Jan";
            break;
            case 1:  refundMonthString = "Feb";
            break;
            case 2:  refundMonthString = "Mar";
            break;
            case 3:  refundMonthString = "Apr";
            break;
            case 4:  refundMonthString = "May";
            break;
            case 5:  refundMonthString = "Jun";
            break;
            case 6:  refundMonthString = "Jul";
            break;
            case 7:  refundMonthString = "Aug";
            break;
            case 8:  refundMonthString = "Sep";
            break;
            case 9: refundMonthString = "Oct";
            break;
            case 10: refundMonthString = "Nov";
            break;
            case 11: refundMonthString = "Dec";
            break;
            default: refundMonthString = "Invalid month";
            break;
        }
        int refundExpiryYear = jYearRefundExpiry.getYear();
        Date refundExpiryDate = new Date();
        try
        {
            SimpleDateFormat refundDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
            refundExpiryDate = refundDateFormat.parse(refundExpiryYear + "-" + refundMonthString + "-01 00:00:00");
        }
        catch (ParseException ex)
        {
            Logger.getLogger(Cart.class.getName()).log(Level.SEVERE, null, ex);
        }
        int cardTypeID = db.getCardTypeIDFromCardType(String.valueOf(comboCardType.getSelectedItem()));
        double totalRefund = 0 - refundDue;

        Payment refund = new Payment(refundPayeeName, refundCardNo, refundSecurityNo, refundExpiryDate, cardTypeID, totalRefund, refundBookingID);
        db.takePayment(refund);
        refundDue = 0;
        refundBookingID = 0;

        jframeRefund.dispose();
        loadFrame();
    }//GEN-LAST:event_btnRefundSubmitActionPerformed

    private void btnRefundClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundClearActionPerformed
        txtPayeeName.setText("");
        txtCardNo.setText("");
        txtSecurityNo.setText("");
    }//GEN-LAST:event_btnRefundClearActionPerformed

    private void btnRefundCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefundCloseActionPerformed
        jframePayment.dispose();
    }//GEN-LAST:event_btnRefundCloseActionPerformed

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

    private void btnStaffExportBookingReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStaffExportBookingReportActionPerformed
        int bookingID = (Integer)tblBookings.getValueAt(tblBookings.getSelectedRow(), 0);
        DBManager db = new DBManager();
        Booking selectedBooking = db.getBookingFromBookingID(bookingID);
        Customer bookingCustomer = db.getCustomerFromCustomerID(selectedBooking.getCustomerID());
        String strBookingID = String.valueOf(selectedBooking.getBookingID());        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");       
        SimpleDateFormat fileFormat = new SimpleDateFormat("dd_MM_yyyy");
        String dateBooked = dateFormat.format(selectedBooking.getDateBooked());
        String outstandingBalance = String.valueOf(selectedBooking.getOutstandingBalance());
        String totalCost = String.valueOf(selectedBooking.getTotalCost());
        String customerName = bookingCustomer.getFirstName() + " " + bookingCustomer.getLastName();
        Date today = new Date();
        String todaysDate = fileFormat.format(today);
        

            try {
                String fileName = strBookingID + "_" + todaysDate;

                Document doc = new Document();
                
                PdfWriter.getInstance(doc, new FileOutputStream("src\\reports\\Booking\\staff\\Booking_Report_Booking_ID_" +fileName +".pdf"));
                doc.open();



                Paragraph heading1 = new Paragraph();
                heading1.add("Booking Report");
                heading1.setAlignment(Element.ALIGN_CENTER);
                Paragraph heading2 = new Paragraph();
                heading2.add("Booking ID " + bookingID);
                heading2.setAlignment(Element.ALIGN_CENTER);
                Paragraph heading3 = new Paragraph();
                heading3.add("Date Booked " + dateBooked );
                heading3.setAlignment(Element.ALIGN_CENTER);
                Paragraph heading4 = new Paragraph();
                heading4.add("Outstanding Balance " + outstandingBalance );
                heading4.setAlignment(Element.ALIGN_CENTER);
                Paragraph heading5 = new Paragraph();
                heading5.add("Total Cost " + totalCost );
                heading5.setAlignment(Element.ALIGN_CENTER);
                Paragraph heading6 = new Paragraph();
                heading6.add("Customer Name " + customerName );
                heading6.setAlignment(Element.ALIGN_CENTER);
                doc.add(heading1);
                doc.add(heading2);
                doc.add(heading3);
                doc.add(heading4);
                doc.add(heading5);
                doc.add(heading6);
                doc.close();

                JOptionPane.showMessageDialog(null, "Successfully Exported");
                
                
            } catch (DocumentException ex) {
                Logger.getLogger(StaffViewRooms.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(StaffViewRooms.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }//GEN-LAST:event_btnStaffExportBookingReportActionPerformed

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        About rForm = new About(loggedInUser);
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnAboutActionPerformed

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
            java.util.logging.Logger.getLogger(StaffViewBookings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StaffViewBookings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StaffViewBookings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StaffViewBookings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new StaffViewBookings().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnControlPanel;
    private javax.swing.JButton btnDeleteBooking;
    private javax.swing.JButton btnEditBooking;
    private javax.swing.JButton btnPayBooking;
    private javax.swing.JButton btnPaymentClear;
    private javax.swing.JButton btnPaymentClose;
    private javax.swing.JButton btnPaymentSubmit;
    private javax.swing.JButton btnRefundClear;
    private javax.swing.JButton btnRefundClose;
    private javax.swing.JButton btnRefundSubmit;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JButton btnStaffExportBookingReport;
    private javax.swing.JComboBox<String> comboCardType;
    private javax.swing.JComboBox<String> comboRefundCardType;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private com.toedter.calendar.JMonthChooser jMonthExpiry;
    private com.toedter.calendar.JMonthChooser jMonthRefundExpiry;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JYearChooser jYearExpiry;
    private com.toedter.calendar.JYearChooser jYearRefundExpiry;
    private javax.swing.JFrame jframePayment;
    private javax.swing.JFrame jframeRefund;
    private javax.swing.JLabel lblPaymentTitle;
    private javax.swing.JLabel lblPaymentTotalCost;
    private javax.swing.JLabel lblStaff;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblBookings;
    private javax.swing.JTextField txtCardNo;
    private javax.swing.JTextField txtPayeeName;
    private javax.swing.JTextField txtPaymentTotalCost;
    private javax.swing.JTextField txtRefundCardNo;
    private javax.swing.JTextField txtRefundPayeeName;
    private javax.swing.JTextField txtRefundSecurityNo;
    private javax.swing.JTextField txtRefundTotalRefund;
    private javax.swing.JTextField txtSecurityNo;
    // End of variables declaration//GEN-END:variables
}
