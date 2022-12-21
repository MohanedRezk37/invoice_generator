package com.invoice.generator.controller;

import com.invoice.generator.common.ActionNames;
import com.invoice.generator.common.FileOperations;
import com.invoice.generator.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.invoice.generator.common.ActionNames.*;


public class ButtonsActions implements ActionListener {
	private static final Logger LOGGER = Logger.getLogger(FileOperations.class.getName());
	private ActionNames actionNames = new ActionNames();

	@Override
	public void actionPerformed(ActionEvent e) {

		switch (e.getActionCommand()) {
			case CREATE_NEW_INVOICE -> {
				FileOperations.createInvoiceDialog();
				LOGGER.log(Level.INFO, "Create Invoice");
			}
			case DELETE_INVOICE -> {
				int indexInvoice = MainView.displayInvoicesTable.getSelectedRow();
				if (indexInvoice == -1) {
					break;
				} else {
					FileOperations.deleteInvoiceByTableIndex(indexInvoice);
				}
				LOGGER.log(Level.INFO, "Delete Invoice");
			}

			case SAVE_EDIT_INVOICE -> {
				String invoiceNumberString = MainView.getInvoiceNumberAsString();
				int invoiceNumberInt = Integer.parseInt(invoiceNumberString);
				if (invoiceNumberString.isEmpty()) {
					JOptionPane.showConfirmDialog(
							null,
							new JLabel("please pick the invoice you want to assign the item to ..."),
							"Item error",
							JOptionPane.DEFAULT_OPTION);
					break;
				} else {
					FileOperations.userInvoiceItemDialog(FileOperations.getInvoice(invoiceNumberInt));
					MainView.updateTables();
				}
				LOGGER.log(Level.INFO, "Created new invoice item");
			}
			case CANCEL_INVOICE -> {
				int indexInvoiceItem = MainView.displayInvoicesItemsTable.getSelectedRow();

				if (indexInvoiceItem == -1) {
					JOptionPane.showConfirmDialog(null,
							new JLabel(
									"Select the invoice item to be deleted"),
							"Error",
							JOptionPane.OK_CANCEL_OPTION);
					break;
				} else {

					FileOperations.deleteInvoiceItemByTableIndex(
							(String) MainView.displayInvoicesItemsTable.getValueAt(indexInvoiceItem, 0),
							(String) MainView.displayInvoicesItemsTable.getValueAt(indexInvoiceItem, 1)
					);
				}
				LOGGER.log(Level.INFO, "Deleted Selected invoice item");

			}
		}
	}

}
