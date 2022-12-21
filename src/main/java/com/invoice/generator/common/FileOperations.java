package com.invoice.generator.common;

import com.invoice.generator.model.InvoiceHeader;
import com.invoice.generator.model.InvoiceLine;
import com.invoice.generator.view.MainView;

import javax.swing.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileOperations {

	private static final String[] INVOICES_HEADERS = {"Invoice Number", "Date", "Customer Name", "Invoice Total"};
	private static final String[] INVOICES_ITEMS_HEADERS = {"Invoice Number", "Item Name", "Item Price", "Item Count", "Total"};
	private final static Logger LOGGER = Logger.getLogger(FileOperations.class.getName());

	private static ArrayList<InvoiceHeader> INVOICES = new ArrayList<>();
	private static String invoicesItemsPath = "C:\\Users\\eltanany shop\\Desktop\\Output-files";
	private static String invoicesPath = "C:\\Users\\eltanany shop\\Desktop\\Output-files";


	public static ArrayList<InvoiceHeader> ReadFile() {
		ArrayList<InvoiceHeader> invoices = new ArrayList<>();
		String line;
		BufferedReader bufferReader = null;
		JFileChooser jfc = new JFileChooser();
		JOptionPane.showConfirmDialog(null,
				"Please Choose the 'InvoiceHeader.csv' file!", "Choose File", JOptionPane.DEFAULT_OPTION);
		jfc.showOpenDialog(jfc);
		invoicesPath = jfc.getSelectedFile().getPath();

		try {
			if (!jfc.getSelectedFile().getName().equals("InvoiceHeader.csv")) {
				throw new Exception("Please make sure to choose a file with the correct name and format of InvoiceHeader.csv");
			}
			bufferReader = new BufferedReader(new FileReader(invoicesPath));

			while ((line = bufferReader.readLine()) != null) {
				String[] invoiceRow = line.split(",");
				new SimpleDateFormat("DDmmyyyy").parse(invoiceRow[1]);
				invoices.add(new InvoiceHeader(Integer.parseInt(invoiceRow[0]), invoiceRow[1], invoiceRow[2]));
			}
		} catch (IOException ex) {
			JOptionPane.showConfirmDialog(null, new JLabel("No header file found 'InvoiceHeader.csv'"), "File reading error", JOptionPane.DEFAULT_OPTION);
			LOGGER.log(Level.SEVERE, null, ex);
			System.exit(0);
		} catch (ParseException ex) {
			JOptionPane.showConfirmDialog(null, new JLabel("Please check format inside 'InvoiceHeader.csv' file"), "Data format error", JOptionPane.OK_CANCEL_OPTION);
			LOGGER.log(Level.SEVERE, null, ex);
			System.exit(0);
		} catch (Exception ex) {
			JOptionPane.showConfirmDialog(null, new JLabel(ex.getMessage()), "File format error", JOptionPane.DEFAULT_OPTION);
			return null;
		}


		JOptionPane.showConfirmDialog(null,
				"Choose 'InvoicesItems.csv' file!", "Choose File", JOptionPane.DEFAULT_OPTION);
		jfc.showOpenDialog(jfc);
		invoicesItemsPath = jfc.getSelectedFile().getPath();

		try {
			if (!jfc.getSelectedFile().getName().equals("InvoicesItems.csv")) {
				throw new Exception("Please select file with the following name and format \n InvoicesItems.csv");
			}
			bufferReader = new BufferedReader(new FileReader(invoicesItemsPath));

			while ((line = bufferReader.readLine()) != null) {
				String[] invoiceLineRow = line.split(",");

				for (int i = 0; i < invoices.size(); i++) {
					if (invoices.get(i).getInvoiceNumber() == Integer.parseInt(invoiceLineRow[0])) {
						invoices.get(i).addInvoiceItem(
								new InvoiceLine(invoices.get(i), Integer.parseInt(invoiceLineRow[0]), invoiceLineRow[1], Double.parseDouble(invoiceLineRow[2]), Integer.parseInt(invoiceLineRow[3]))
						);
					}
				}
			}
		} catch (IOException ex) {
			JOptionPane.showConfirmDialog(null, new JLabel("No file found 'InvoiceLine.csv'"), "Error", JOptionPane.DEFAULT_OPTION);
			LOGGER.log(Level.SEVERE, null, ex);
			System.exit(0);
		} catch (Exception ex) {
			JOptionPane.showConfirmDialog(null, new JLabel(ex.getMessage()), "Error in file format", JOptionPane.DEFAULT_OPTION);
		}
		INVOICES = invoices;

		return INVOICES;
	}

	public static void WriteFile(ArrayList<InvoiceHeader> dataRows) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(new File(invoicesPath));

			for (InvoiceHeader data : dataRows) {
				StringBuilder line = new StringBuilder();
				line.append(data.getInvoiceNumber());
				line.append(",");
				line.append(data.getInvoiceDate());
				line.append(",");
				line.append(data.getCustomerName());
				line.append(",");
				line.append("\n");
				fileWriter.write(line.toString());
			}

			fileWriter.close();
		} catch (IOException ex) {
			JOptionPane.showConfirmDialog(null, new JLabel("No file found with name 'Invoices.csv'"), "File not found error", JOptionPane.OK_CANCEL_OPTION);
			LOGGER.log(Level.SEVERE, null, ex);
			System.exit(0);
		}

		try {
			fileWriter = new FileWriter(new File(invoicesItemsPath));

			for (InvoiceHeader data : dataRows) {
				for (InvoiceLine lineDetails : data.getInvoiceItems()) {
					StringBuilder line = new StringBuilder();
					line.append(lineDetails.getInvoiceNumber());
					line.append(",");
					line.append(lineDetails.getItemName());
					line.append(",");
					line.append(lineDetails.getItemPrice());
					line.append(",");
					line.append(lineDetails.getItemCount());
					line.append(",");
					line.append("\n");
					fileWriter.write(line.toString());
				}
			}

			fileWriter.close();
		} catch (IOException ex) {
			JOptionPane.showConfirmDialog(null, new JLabel("No file found with name 'InvoiceLine.csv'"), "No file error", JOptionPane.OK_CANCEL_OPTION);
			LOGGER.log(Level.SEVERE, null, ex);
			System.exit(0);
		}


	}

	public static ArrayList<InvoiceHeader> getInvoices() {
		return INVOICES;
	}

	public static boolean hasInvoices(){
		if (INVOICES != null && INVOICES.size() > 0)
			return true;

		return false;
	}

	public static InvoiceHeader getInvoice(int invoiceNo) {
		return INVOICES.stream()
				.filter(invoice -> invoice.getInvoiceNumber() == invoiceNo)
				.findFirst()
				.orElse(null);
	}

	public static String[] getInvoicesAsArray() {
		return INVOICES_HEADERS;
	}

	public static String[][] getInvoicesAsMatrix() {
		String[][] data;
		data = new String[INVOICES.size()][INVOICES_HEADERS.length];

		for (int i = 0; i < INVOICES.size(); i++)
			data[i] = INVOICES.get(i).getTableFormat();

		return data;
	}


	public static String[] getInvoicesItemsHeaders() {
		return INVOICES_ITEMS_HEADERS;
	}

	public static String[][] getInvoicesItemsTableData() {
		String[][] data;
		int itemsCount = 0;
		for (InvoiceHeader invoice : INVOICES) {
			itemsCount += invoice.getInvoiceItems().size();
		}
		data = new String[itemsCount][INVOICES_ITEMS_HEADERS.length];
		itemsCount = 0;

		for (int i = 0; i < INVOICES.size(); i++) {
			InvoiceHeader ih = INVOICES.get(i);
			for (InvoiceLine il : ih.getInvoiceItems()) {
				data[itemsCount] = il.getTableFormat();
				itemsCount++;
			}
		}

		return data;
	}

	public static String[][] getInvoicesItemsTableData(int invoiceNo) {
		String[][] data;
		int itemsCount = 0;
		InvoiceHeader ih = null;

		for (InvoiceHeader invoice : INVOICES) {
			if (invoice.getInvoiceNumber() == invoiceNo) {
				ih = invoice;
				itemsCount = ih.getInvoiceItems().size();
			}
		}

		data = new String[itemsCount][INVOICES_ITEMS_HEADERS.length];
		itemsCount = 0;

		for (InvoiceLine il : ih.getInvoiceItems()) {
			data[itemsCount] = il.getTableFormat();
			itemsCount++;
		}
		return data;
	}

	public static String[] getInvoiceData(int invoiceNo) {
		String[] data = null;

		for (InvoiceHeader invoice : INVOICES) {
			if (invoice.getInvoiceNumber() == invoiceNo) {
				data = invoice.getTableFormat();
			}
		}

		return data;
	}

	public static void updateInvoice(String[] data) {
		for (InvoiceHeader ih : INVOICES) {
			if (ih.getInvoiceNumber() == Integer.parseInt(data[0])) {
				ih.setInvoiceDate(data[1]);
				ih.setCustomerName(data[2]);
			}
		}
		MainView.updateTables();
	}


	public static InvoiceHeader addInvoice(String[] data) {
		InvoiceHeader invoice = new InvoiceHeader(
				getInvoiceCount(), data[0], data[1]);

		INVOICES.add(invoice);
		return invoice;
	}

	private static int getInvoiceCount() {
		if (INVOICES == null)
			INVOICES = new ArrayList<>();

		if (INVOICES.size() == 0)
			return 1;

		return INVOICES.get(INVOICES.size() - 1).getInvoiceNumber() + 1;
	}

	public static void deleteInvoice(String invoiceNo) {
		for (InvoiceHeader invoice : INVOICES) {
			if (invoice.getInvoiceNumber() == Integer.parseInt(invoiceNo)) {
				INVOICES.remove(invoice);
			}
		}
		MainView.updateTables();
	}

	public static void deleteInvoiceByTableIndex(int invoiceNo) {
		INVOICES.remove(invoiceNo);
		MainView.updateTables();
	}

	public static void deleteInvoiceItemByTableIndex(String invoiceIndex, String invoiceItem) {
		for (InvoiceHeader ih : INVOICES) {
			if (ih.getInvoiceNumber() == Integer.parseInt(invoiceIndex)) {
				for (InvoiceLine il : ih.getInvoiceItems()) {
					if (il.getItemName().equals(invoiceItem)) {
						ih.removeInvoiceLine(il);
						break;
					}
				}
			}
		}
		MainView.updateTables();
	}


	public static void createInvoiceDialog() {
		JTextField dateField = new JTextField(10);
		JTextField nameField = new JTextField(50);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("date in DD-MM-YYYY format:"));
		myPanel.add(dateField);
		myPanel.add(Box.createHorizontalStrut(15)); // a spacer
		myPanel.add(new JLabel("Customer Name:"));
		myPanel.add(nameField);

		String[] data = new String[2];
		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please Enter the invoice data and Customer Name", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			data[0] = dateField.getText();
			data[1] = nameField.getText();
			userInvoiceItemDialog(addInvoice(data));
			MainView.updateTables();
		}
	}


	public static void userInvoiceItemDialog(InvoiceHeader invoice) {
		JTextField itemNameField = new JTextField(50);
		JTextField itemPriceField = new JTextField(4);
		JTextField itemCountField = new JTextField(5);

		JPanel myPanel = new JPanel();
		myPanel.add(new JLabel("Item name:"));
		myPanel.add(itemNameField);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(new JLabel("Item Price:"));
		myPanel.add(itemPriceField);
		myPanel.add(Box.createHorizontalStrut(15));
		myPanel.add(new JLabel("Item Count:"));
		myPanel.add(itemCountField);

		String[] data = new String[3];

		int result = JOptionPane.showOptionDialog(null,
				myPanel,
				"Please input the purchase details",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE,
				null,
				new String[]{"input another after this?", "last item."},
				"default");

		data[0] = itemNameField.getText();
		data[1] = itemPriceField.getText();
		data[2] = itemCountField.getText();

		InvoiceLine il = new InvoiceLine(invoice, invoice.getInvoiceNumber(), data[0], Double.parseDouble(data[1]), Integer.parseInt(data[2]));
		invoice.addInvoiceItem(il);

		if (result == JOptionPane.OK_OPTION) {
			userInvoiceItemDialog(invoice);
		}
	}

}
