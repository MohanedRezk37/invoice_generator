package com.invoice.generator.controller;

import com.invoice.generator.InvoiceGenerator;
import com.invoice.generator.common.FileOperations;
import com.invoice.generator.model.InvoiceHeader;
import com.invoice.generator.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.invoice.generator.common.ActionNames.LOAD_FILE;
import static com.invoice.generator.common.ActionNames.SAVE_FILE;


public class MenuItemsActions  implements ActionListener{
    private final static Logger LOGGER = Logger.getLogger(MenuItemsActions.class.getName());

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case LOAD_FILE -> {
                LOGGER.log(Level.INFO, "Loading a file");

                FileOperations.ReadFile();
                InvoiceGenerator.readFileTest();
                MainView.updateTables();
                JOptionPane.showConfirmDialog(
                        null,
                        "Data Loaded Successfully",
                        "Success!",
                        JOptionPane.DEFAULT_OPTION);
            }
            case SAVE_FILE ->{
                LOGGER.log(Level.INFO, "Saving file");

                ArrayList<InvoiceHeader> ihs = FileOperations.getInvoices();
                FileOperations.WriteFile(ihs);
                JOptionPane.showConfirmDialog(
                        null,
                "Data Saved Successfully",
                        "Success!",
                        JOptionPane.DEFAULT_OPTION);
            }
        }
    }
    
}
