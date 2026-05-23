package com.myprojects.customer.management.desktop.ui;

import com.myprojects.customer.management.desktop.model.Customer;
import com.myprojects.customer.management.desktop.client.CustomerApiClient;
import javax.swing.*;
import java.awt.*;

public class CustomerDialog extends JDialog {
    private final CustomerApiClient apiClient = new CustomerApiClient();
    private final Customer originalCustomer;   
    private boolean dataChanged = false;

    private JTextField txtName, txtEmail, txtPhone;

    public CustomerDialog(Frame parent, Customer customer) {
        super(parent, customer == null ? "Add Customer" : "Edit Customer", true);
        this.originalCustomer = customer;
        initUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtName = new JTextField(20);
        formPanel.add(txtName, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        formPanel.add(txtEmail, gbc);

        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(20);
        formPanel.add(txtPhone, gbc);

        
        if (originalCustomer != null) {
            txtName.setText(originalCustomer.getName());
            txtEmail.setText(originalCustomer.getEmail());
            txtPhone.setText(originalCustomer.getPhone());
        }

        
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        btnSave.addActionListener(e -> saveCustomer());
        btnCancel.addActionListener(e -> dispose());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void saveCustomer() {
        
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Email are required.");
            return;
        }

        Customer customerToSave = new Customer(name, email, phone);

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<Customer, Void> worker = new SwingWorker<>() {
            @Override
            protected Customer doInBackground() throws Exception {
                if (originalCustomer == null) {
                    
                    return apiClient.createCustomer(customerToSave);
                } else {
                    
                    return apiClient.updateCustomer(originalCustomer.getId(), customerToSave);
                }
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                try {
                    get(); 
                    dataChanged = true;
                    JOptionPane.showMessageDialog(CustomerDialog.this,
                            originalCustomer == null ? "Customer added successfully." : "Customer updated successfully.");
                    dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerDialog.this,
                            "Save failed: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public boolean isDataChanged() {
        return dataChanged;
    }
}

