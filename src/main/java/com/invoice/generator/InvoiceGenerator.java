package com.invoice.generator;

import com.invoice.generator.common.FileOperations;
import com.invoice.generator.model.InvoiceHeader;
import com.invoice.generator.model.InvoiceLine;
import com.invoice.generator.view.MainView;

import java.util.ArrayList;

public class InvoiceGenerator {

    public static void main(String[] args) {
        MainView view = new MainView("InvoiceGenerator");
        view.setVisible(true);
    }
    
    
    public static void readFileTest(){
        ArrayList<InvoiceHeader> invoices = FileOperations.getInvoices();
        for(int i = 0; i<invoices.size();i++){
            InvoiceHeader ih = invoices.get(i);

            System.out.print("Invoice"+ih.getInvoiceNumber()+"Num");
            System.out.println();
            System.out.println("{");
            System.out.print("Invoice"+ih.getInvoiceNumber()+"Data (" + ih.getInvoiceDate() + "), " + ih.getCustomerName());
            System.out.println();


            for(InvoiceLine il: ih.getInvoiceItems()){
                System.out.print(il.getItemName() + ", "+il.getItemPrice() + ", "+ il.getItemCount());
                System.out.println();
            }
            System.out.println();
            System.out.println("}");
            System.out.println();
        }
    }
}
