package com.myprojects.customer.management.desktop.ui;

import com.myprojects.customer.management.desktop.client.CustomerApiClient;
import com.myprojects.customer.management.desktop.model.Customer;
import com.myprojects.customer.management.desktop.ui.CustomerDialog;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final CustomerApiClient apiClient = new CustomerApiClient();
    private final CustomerTableModel tableModel = new CustomerTableModel();
    private JTable customerTable;

    public MainFrame() {
        setTitle("Customer Manager - Desktop Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        initUI();
        loadCustomers();  
    }

    private void initUI() {
        
        customerTable = new JTable(tableModel);
        customerTable.setFillsViewportHeight(true);
        customerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(customerTable);

        
        JButton btnAdd = new JButton("Add Customer");
        JButton btnEdit = new JButton("Edit");
        JButton btnDelete = new JButton("Delete");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> openCustomerDialog(null));
        btnEdit.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a customer to edit.");
                return;
            }
            Customer selected = tableModel.getCustomerAt(selectedRow);
            openCustomerDialog(selected);
        });
        btnDelete.addActionListener(e -> deleteCustomer());
        btnRefresh.addActionListener(e -> loadCustomers());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    
    private void loadCustomers() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<List<Customer>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Customer> doInBackground() throws Exception {
                return apiClient.getAllCustomers();
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    List<Customer> customers = get();
                    tableModel.setCustomers(customers);
                    if (customers.isEmpty()) {
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "No customers found. Add one using the Add button.");
                    }
                } catch (Exception ex) {
                    showError("Failed to load customers", ex);
                }
            }
        };
        worker.execute();
    }

   
    private void openCustomerDialog(Customer existingCustomer) {
        CustomerDialog dialog = new CustomerDialog(this, existingCustomer);
        dialog.setVisible(true);
        
        if (dialog.isDataChanged()) {
            loadCustomers();
        }
    }

    
    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
            return;
        }
        Customer toDelete = tableModel.getCustomerAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete customer \"" + toDelete.getName() + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                apiClient.deleteCustomer(toDelete.getId());
                return null;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    get(); 
                    loadCustomers(); 
                    JOptionPane.showMessageDialog(MainFrame.this, "Customer deleted successfully.");
                } catch (Exception ex) {
                    showError("Delete failed", ex);
                }
            }
        };
        worker.execute();
    }

    
    private void showError(String operation, Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                operation + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    
}

