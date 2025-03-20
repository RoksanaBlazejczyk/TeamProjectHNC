package projectPack;

import javax.swing.*;
import java.awt.*;

public class Authentication {
    private static JPanel SmartHireHub;
    private static JTextField firstNameTxt;
    private static JTextField surnameTxt;
    /**
     * Method to handle account creation logic
     */
    public static void createAccount() {
        //Disable create button until everything and avatar selected
        // Assuming firstNameTxt and surnameTxt are JTextField components
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();

        // Validation Logic

        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please enter your first name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (firstName.length() < 2) {
            JOptionPane.showMessageDialog(SmartHireHub, "First name must be at least 2 characters.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (!firstName.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(SmartHireHub, "Invalid first name! Use letters and spaces only.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please enter your surname!", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else if (!surname.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(SmartHireHub, "Invalid surname! Use letters and spaces only.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(SmartHireHub,
                    "Account created successfully!\nWelcome, " + firstName + "_" + surname,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
