package projectPack;

import javax.swing.*;
import java.awt.*;


public void credentialsAuthentication() {
    // Get the current panel name
    String currentPanel = getCurrentPanelName();

    if (currentPanel.equals("WelcomePanel")) {
        // Will remove leading and trailing spaces
        String name = txtName.getText().trim();

        // Validates that name field is not blank when user tries to go next
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(detailPanel, "Please enter your name to proceed!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Checks that name is at least 2 letters long
        else if (name.length() < 2) {
            JOptionPane.showMessageDialog(detailPanel, "Name must be at least 2 characters long.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Check that name contains only letters and spaces
        else if (!name.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(detailPanel, "Enter a valid name! (Letters and spaces only)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Proceed with age validation and transition to the next panel
        validateAndProceed();
    }
    private String getCurrentPanelName() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        //Used to check what card you're on for the next and back buttons to be enabled or disabled
        if (detailPanel.getComponent(0).isVisible()) {
            return "WelcomePanel";  //WelcomePanel is at index 0
        } else if (detailPanel.getComponent(1).isVisible()) {
            return "HabitPanel";  //HabitPanel is at index 1
        } else if (detailPanel.getComponent(2).isVisible()) {
            return "AdditionalPanel";  //AdditionalPanel is at index 2
        }
        return "";
    }