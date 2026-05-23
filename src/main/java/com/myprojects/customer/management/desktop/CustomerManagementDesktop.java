package com.myprojects.customer.management.desktop;

import com.myprojects.customer.management.desktop.ui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class CustomerManagementDesktop {

    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
    
}
