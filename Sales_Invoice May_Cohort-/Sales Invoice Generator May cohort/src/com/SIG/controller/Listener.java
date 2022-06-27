
package com.SIG.controller;

import com.SIG.model.HeaderTableModel;
import com.SIG.model.LinesTableModel;
import com.SIG.model.InvoiceHeader;
import com.SIG.model.InvoiceLine;
import com.SIG.view.InvoiceCustomerDialog;
import com.SIG.view.LineCustomerDialog;
import com.SIG.view.NewInvoiceFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class Listener  implements ActionListener , ListSelectionListener  {
    
  private NewInvoiceFrame frame;
  private InvoiceCustomerDialog invoiceDialog;
  private LineCustomerDialog lineDialog;

    
  public Listener (NewInvoiceFrame frame) {
      this.frame=frame;
      
      
  }  

    @Override
    public void actionPerformed(ActionEvent e) {
        
    String actionCommand = e.getActionCommand();
   
    System.out.println("Action"+actionCommand);
    
    switch (actionCommand) {
        
            case"Load File":
                loadFile();
                  break;
            
            case"Save File":
                saveFile();
                   break;
            
            case"Create New Invoice":
                createNewInvoice();
                    break;
                 
            case"Delete Invoice":
                 deleteInvoice();
                    break;
            
            case"Create New Item":
                createNewItem();
                    break;
            
            case"Delete Item":
                deleteItem();
                     break;
                    
            case "createLineOK":
                createLineOK();
                break;
            case "createLineCancel":
                createLineCancel();
                break;
            case "createInvoiceCancel":
                createInvoiceCancel();
                break;
            case "createInvoiceOK":
                createInvoiceOK();
                break;
                 
    }
    
    }

     @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = frame.getInvoiceTable().getSelectedRow();
                if (selectedIndex != -1) {
        System.out.println("You have selected row: "+selectedIndex );
        InvoiceHeader currentInvoice = frame.getInvoices().get(selectedIndex);
        frame.getInvoiceNumLabel().setText(""+currentInvoice.getIdNum());
        frame.getInvoiceDateLabel().setText(currentInvoice.getInvoiceDate());
        frame.getCustomerNameLabel().setText(currentInvoice.getCustomerName());
        frame.getInvoiceTotalLabel().setText(""+currentInvoice.getInvTotal());
        
        LinesTableModel linesTableModel = new LinesTableModel(currentInvoice.getLines());
        frame.getLineTable().setModel(linesTableModel);
        linesTableModel.fireTableDataChanged();
        
        
    }
    }
    private void loadFile() {
            JOptionPane.showMessageDialog(frame, "Please, select header file!", "Attention", JOptionPane.WARNING_MESSAGE);

        JFileChooser  fch =new JFileChooser();
        try{
       
        int result = fch.showOpenDialog(frame);
       if ( result==JFileChooser.APPROVE_OPTION){
    
           File headerFile=fch.getSelectedFile();
           Path headerPath=Paths.get(headerFile.getAbsolutePath());
          
          List<String> headerLines;
          headerLines = Files.readAllLines(headerPath); 
          
          System.out.println("Invoices have been read");
       

         ArrayList<InvoiceHeader> invoicesArray =new ArrayList<>();
         
          for (String headerLine : headerLines) {
         String[] headerParts = headerLine.split(","); 
         int invoiceNum = Integer.parseInt(headerParts[0]);
         String invoiceDate = headerParts[1];
         String customerName = headerParts[2];
         
         InvoiceHeader invoice = new InvoiceHeader(invoiceNum,invoiceDate,customerName );
         invoicesArray.add(invoice);
         }
          
          System.out.println("check");
         JOptionPane.showMessageDialog(frame, "Please, select lines file!", "Attention", JOptionPane.WARNING_MESSAGE);
          result=fch.showOpenDialog(frame);
          if(result==JFileChooser.APPROVE_OPTION){
              
          File lineFile = fch.getSelectedFile();
          Path linePath = Paths.get( lineFile.getAbsolutePath());
          List<String>lineLines=Files.readAllLines(linePath);
          System.out.println("Lines have been read");
          for (String lineLine : lineLines) {
          String[] lineParts = lineLine.split(",");
          int invoiceNum = Integer.parseInt( lineParts[0]);
          String itemName =lineParts[1];
          double itemPrice = Double.parseDouble(lineParts[2]);
          int count = Integer.parseInt(lineParts[3]);
                     
          InvoiceHeader  inv = null;
          for (InvoiceHeader invoice :invoicesArray) {
                        
                         
          if (invoice.getIdNum() == invoiceNum )  {
                       inv=invoice;
                       break;
                       
                                       
           }
                         
                             
                         
           }
         InvoiceLine line = new InvoiceLine(itemName,itemPrice, count, inv);
         
          inv.getLines().add(line);
          
          System.out.println("check");
     
          }
           frame.setInvoices(invoicesArray);
           
           
           HeaderTableModel headerTableModel = new HeaderTableModel (invoicesArray);
           frame.setHeaderTableModel(headerTableModel);
           frame.getInvoiceTable().setModel(headerTableModel);
           frame.getHeaderTableModel().fireTableDataChanged();
          }
        
         }
       
         }  
        catch (IOException ex) {
            ex.printStackTrace();
          JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
} catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        
            
              
    }    
 private void createNewInvoice() {
     invoiceDialog = new InvoiceCustomerDialog(frame);
     invoiceDialog.setVisible(true);
    
    }  

     private void saveFile() {
     ArrayList<InvoiceHeader> invoices = frame.getInvoices();
     String headers ="";
     String lines ="";

     for (InvoiceHeader invoice : invoices) {
         String invoiceCSV = invoice.getCSVFile();
         headers +=  invoiceCSV;
         headers += "\n";
         for (InvoiceLine line : invoice.getLines()) {
                String lineCSV = line.getCSVFile();
                lines += lineCSV;
                lines += "\n";
            }
        }
       System.out.println("Check point");
        try {
            JOptionPane.showMessageDialog(frame, "Please, select file to save header data!", "Attention", JOptionPane.WARNING_MESSAGE);
            JFileChooser fch = new JFileChooser();  
           int result = fch.showSaveDialog(frame);
             if (result == JFileChooser.APPROVE_OPTION) {
                 File headerFile = fch.getSelectedFile();
                  FileWriter hFileWriter = new FileWriter(headerFile);
            hFileWriter.write(headers);
                hFileWriter.flush();
                hFileWriter.close();
            JOptionPane.showMessageDialog(frame, "Please, select file to save lines data!", "Attention", JOptionPane.WARNING_MESSAGE);
              result = fch.showSaveDialog(frame);
             if (result == JFileChooser.APPROVE_OPTION) {
                 File lineFile = fch.getSelectedFile();
                    FileWriter lFileWriter = new FileWriter(lineFile);
                    lFileWriter.write(lines);
                    lFileWriter.flush();
                    lFileWriter.close();
               JOptionPane.showMessageDialog(null, "File Saved Successfully ! ");
           
             }
     }
     
        } catch (Exception ex) {
        }
        
     }
    private void deleteInvoice() {
     int selectedRow =   frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
           frame.getHeaderTableModel().fireTableDataChanged();
           frame.setLinesTableModel(new LinesTableModel(new ArrayList<InvoiceLine>()));
       frame.getLineTable().setModel(frame.getLinesTableModel());
        frame.getLinesTableModel().fireTableDataChanged();
        frame.getCustomerNameLabel().setText("");
        frame.getInvoiceDateLabel().setText("");
        frame.getInvoiceNumLabel().setText("");
        frame.getInvoiceTotalLabel().setText("");
       frame.getHeaderTableModel().fireTableDataChanged();
     JOptionPane.showMessageDialog(null, "Invoice Deleted Successfully ! ");

        }
    }

    private void createNewItem() {
        lineDialog = new LineCustomerDialog(frame);
        lineDialog.setVisible(true);
        
    }

    private void deleteItem() {
         int selectedRow =   frame.getLineTable().getSelectedRow();
        if (selectedRow != -1) {
            LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }
            
           
           JOptionPane.showMessageDialog(null, "Line Deleted Successfully ! ");

       
       
    }

    private void createLineOK() {
        String item = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            InvoiceHeader invoice = frame.getInvoices().get(selectedInvoice);
            InvoiceLine line = new InvoiceLine(item, price, count, invoice);
            invoice.getLines().add(line);
            LinesTableModel linesTableModel = (LinesTableModel) frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
   JOptionPane.showMessageDialog(null, "New Item has been added Successfully ! ");

    }

    private void createLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }

    private void createInvoiceCancel() {
        invoiceDialog.setVisible(false);
        invoiceDialog.dispose();
        invoiceDialog = null;
    }

    private void createInvoiceOK() {
    String date = invoiceDialog.getInvDateField().getText();
    String customer = invoiceDialog.getCustNameField().getText();
    int num = frame.getNextInvoiceNum();
    InvoiceHeader invoice = new InvoiceHeader (num, date, customer);
    frame.getInvoices().add(invoice);
   frame.getHeaderTableModel().fireTableDataChanged();
   invoiceDialog.setVisible(false);
   invoiceDialog.dispose();
   invoiceDialog = null;
  JOptionPane.showMessageDialog(null, "New Invoice has been added Successfully ! ");

    }

    
}
