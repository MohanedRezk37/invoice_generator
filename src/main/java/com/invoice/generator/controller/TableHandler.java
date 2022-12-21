package com.invoice.generator.controller;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import com.invoice.generator.common.FileOperations;
import com.invoice.generator.view.MainView;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TableHandler extends AbstractTableModel implements ListSelectionListener,TableModelListener,TableModel{
    private final static Logger LOGGER = Logger.getLogger(TableHandler.class.getName());
    public static final int INVOICES_TABLE = 1;
    public static final int INVOICE_ITEMS_TABLE = 2;

    private Object[][] data;
    private String[] columnNames;

    private int tableType;
    private JTable table;

    public TableHandler(int type){
        tableType = type;
        
        if(FileOperations.hasInvoices()){
            data= new String [0][0];
            if(type == INVOICES_TABLE){
                columnNames = FileOperations.getInvoicesAsArray();
            } else if (type == INVOICE_ITEMS_TABLE){
                columnNames = FileOperations.getInvoicesItemsHeaders();
            }
            
            table = new JTable(this);
            table.getSelectionModel().addListSelectionListener(this);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getModel().addTableModelListener(this);
            table.setCellSelectionEnabled(true);
            return;
        }
        
        if(tableType == INVOICES_TABLE){
            data= FileOperations.getInvoicesAsMatrix();
            columnNames = FileOperations.getInvoicesAsArray();
        } else if (tableType == INVOICE_ITEMS_TABLE){
            data= FileOperations.getInvoicesItemsTableData();
            columnNames = FileOperations.getInvoicesItemsHeaders();
        }
        
        table = new JTable(this);
        table.getSelectionModel().addListSelectionListener(this);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getModel().addTableModelListener(this);
        table.setCellSelectionEnabled(true);
    }

    public TableHandler(String [] [] d, int type){
        tableType = type;
        
        if(type == INVOICES_TABLE){
            columnNames = FileOperations.getInvoicesAsArray();
        } else if (type == INVOICE_ITEMS_TABLE){
            columnNames = FileOperations.getInvoicesItemsHeaders();
        }
        
        data = d;
    }
    
    public JTable getTable(){
        return table;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
         
        String[] selectedData = null;

        int selectedRow = table.getSelectedRow();
        int columnCount = table.getColumnCount();
        selectedData = new String[columnCount];
        
        if(selectedRow == -1){
            return;
        }

          for (int j = 0; j < columnCount; j++)
            selectedData[j] = (String) table.getValueAt(selectedRow,j);
        
        if(tableType == INVOICE_ITEMS_TABLE){
            MainView.displayInvoicesTable.setModel(new TableHandler(FileOperations.getInvoicesAsMatrix(),INVOICES_TABLE));
        } else if (tableType == INVOICES_TABLE){
            MainView.updateTextFields(FileOperations.getInvoiceData(Integer.parseInt(selectedData[0])));
            MainView.displayInvoicesItemsTable.setModel(new TableHandler(FileOperations.getInvoicesItemsTableData(Integer.parseInt(selectedData[0])),INVOICE_ITEMS_TABLE));
        } 
      }

    @Override
    public void tableChanged(TableModelEvent e) {
        int column = e.getColumn();
        int row = e.getFirstRow();
        TableModel model = (TableModel)e.getSource();
        Object data = model.getValueAt(row, column);

        LOGGER.log(Level.INFO, String.format("Data %s", data.toString()));
    }
    
}
