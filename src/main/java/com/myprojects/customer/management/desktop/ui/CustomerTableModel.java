package com.myprojects.customer.management.desktop.ui;

import com.myprojects.customer.management.desktop.model.Customer;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CustomerTableModel extends AbstractTableModel {
    private List<Customer> customers = new ArrayList<>();
    private final String[] columnNames = {"ID", "Name", "Email", "Phone", "Created At"};

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
        fireTableDataChanged();   
    }

    @Override
    public int getRowCount() {
        return customers.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Customer c = customers.get(rowIndex);
        switch (columnIndex) {
            case 0: return c.getId();
            case 1: return c.getName();
            case 2: return c.getEmail();
            case 3: return c.getPhone();
            case 4: return c.getCreatedAt() != null ? c.getCreatedAt().toString() : "";
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

   
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public Customer getCustomerAt(int row) {
        return customers.get(row);
    }
}

