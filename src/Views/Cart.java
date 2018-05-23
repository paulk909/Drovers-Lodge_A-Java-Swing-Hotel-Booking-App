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
import Models.PaymentType;
import Models.RoomType;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Paul
 */
public class Cart extends javax.swing.JFrame {
    
    private Customer loggedInCustomer = new Customer();
    private Booking currentBooking = loggedInCustomer.getCurrentBooking();
    

    /**
     * Creates new form Cart
     */
    public Cart(Booking currentBooking) {
        currentBooking.populateBookingLines();
        initComponents();
        this.getContentPane().setBackground(Color.white); 
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        
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
        txtTotalCost.setText("£" + String.valueOf(currentBooking.getTotalCost()));
        populatePaymentTypeDropDown();
    }
    
    
    public void refreshCart(int rowIndex)
    {
        ((DefaultTableModel)tblBookingLines.getModel()).removeRow(rowIndex);
        currentBooking = loggedInCustomer.getCurrentBooking();
        txtBookingID.setText(String.valueOf(currentBooking.getBookingID()));
        txtTotalCost.setText("£" + String.format("%.02f",(currentBooking.getTotalCost())));
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
        btnRemove = new javax.swing.JButton();
        btnAddMoreRooms = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
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

        txtTotalCost.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(txtBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnConfirm)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtBookingID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(comboPaymentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtTotalCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfirm))
                .addGap(20, 20, 20))
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddMoreRooms)
                .addGap(18, 18, 18)
                .addComponent(btnRemove)
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRemove)
                    .addComponent(btnAddMoreRooms))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSignInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignInActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSignInActionPerformed

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
        CheckAvailability rForm = new CheckAvailability();
        this.dispose();
        rForm.setVisible(true);
    }//GEN-LAST:event_btnAddMoreRoomsActionPerformed

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
    private javax.swing.JButton btnAddMoreRooms;
    private javax.swing.JButton btnCart;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSignIn;
    private javax.swing.JComboBox<String> comboPaymentType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblBookingLines;
    private javax.swing.JTextField txtBookingID;
    private javax.swing.JTextField txtTotalCost;
    // End of variables declaration//GEN-END:variables
}
