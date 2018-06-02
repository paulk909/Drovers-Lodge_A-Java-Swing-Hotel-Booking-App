/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Paul
 */
public class ExcelWriter {
    
    public void getExcelBookingData(JTable tblBookingData,File file) throws IOException
    {
      TableModel model = tblBookingData.getModel();
      FileWriter out = new FileWriter(file);
     
      BufferedWriter bw = new BufferedWriter(out);
      for (int i = 0; i < model.getColumnCount(); i++)
      {
        bw.write(model.getColumnName(i) + "\t");
      }
     
      bw.write("\n");
     
      for (int i = 0; i < model.getRowCount(); i++)
      {
        for (int j = 0; j < model.getColumnCount(); j++)
        {
          bw.write(model.getValueAt(i,j).toString()+"\t");
        }        
        bw.write("\n");
      }
     
      bw.close();
     
   System.out.print("Write out to" + file);
}
   
    //Method for creating a new Excel File. File will be overwritten with jTable input above.
    public void newExcelFile(String name)
    {
        try
        {
            String filename = "src\\reports\\Booking\\staff\\Booking_Report_Booking_ID_" + name + ".xls" ;
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Sheet");  
 
            HSSFRow rowhead = sheet.createRow((short)0);
            rowhead.createCell(0).setCellValue("No.");
 
 
            HSSFRow row = sheet.createRow((short)1);
            row.createCell(0).setCellValue("1");
 
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Your excel file has been generated!");
 
        }
        catch ( Exception ex )
        {
            ex.printStackTrace();
        }
 
    }
    
}
