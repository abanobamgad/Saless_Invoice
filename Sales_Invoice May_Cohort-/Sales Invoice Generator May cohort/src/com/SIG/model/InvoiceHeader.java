
package com.SIG.model;

import java.util.ArrayList;



public class InvoiceHeader {
    
    private int idNum;
    
    private String invoiceDate;
    
    private String customerName;
    
    private ArrayList<InvoiceLine>lines;
    
  
    
    

    public InvoiceHeader() {
    }

    public InvoiceHeader(int idNum, String invoiceDate, String customerName) {
        this.idNum = idNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
        
    }
    public double getInvTotal(){
    double total = 0.0;
    for (InvoiceLine line : getLines()){
    total += line.getLineTotal();
            
        }
        return total;
    }
    public ArrayList<InvoiceLine> getLines() {
        if (lines == null){
            lines = new ArrayList<>(); 
        }
        return lines;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getIdNum() {
        return idNum;
    }

    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String toString() {
        return "SalesInvoice{" + "idNum=" + idNum + ", invoiceDate=" + invoiceDate + ", customerName=" + customerName + '}';
    }
    
//    public String getCSVFile() {
//        return idNum + "," + invoiceDate + "," + customerName;
//    }

    public String getCSVFile() {
        return idNum + "," + invoiceDate + "," + customerName;
    }

   


   
    
  
}

   