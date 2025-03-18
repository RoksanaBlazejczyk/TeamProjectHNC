//package projectPack;
/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;


public class SmartHire {

    private JPanel SmartHireHub;
    private JPanel loginScreen;
    private JPanel createScreen;
    private JPanel rulesScreen;
    private JPanel questionsScreen;
    private JPanel resultsScreen;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel nextPanel;
    private JButton nextButton;
    private JTextField passwordTxt;
    private JTextField usernameTxt;
    private JButton loginBtn;
    private JButton createAccountBtn;
    private JPanel background;
    private JTextField firstNameTxt;
    private JTextField surnameTxt;
    private JButton createBtn;
    private JButton chooseAvatarBtn;
    private JButton BGMusicButton;
    private JButton settingsButton;

    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame myApp = new JFrame("SmartHire App");

            // Create an instance of SmartHire and set it to the frame
            SmartHire app = new SmartHire();

            myApp.setContentPane(app.SmartHireHub);
            myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myApp.pack();
            myApp.setSize(800, 480);
            myApp.setVisible(true);
        });
    }

    public String getCurrentPanel() {
        // Get the CardLayout from the mainPanel
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        // Check if the first component (loginScreen) is visible
        if (mainPanel.getComponent(0).isVisible()) {
            return "loginScreen";
        }
        // Check if another panel (e.g., "createAccountScreen") is visible
        else if (mainPanel.getComponent(1).isVisible()) {
            return "createAccountScreen";
        }
        // If no panel is visible or another panel is showing, you can return a default value or error
        else {
            return "Unknown Panel";
        }
    }
    public void goToNextScreen(){
        String currentPanel = getCurrentPanel();
        if (currentPanel.equals("loginScreen")) {
            createAccount();
        }
    }


    public SmartHire() {

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logIn();

            }
        });
        createAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToNextScreen();

            }
        });
    }

    /**
     * Method to handle account creation logic
     */
    private void createAccount() {
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

    /**
     * Method to log in with firstName_surname and provided password
     */
    public void logIn() {
        // Assuming firstNameTxt and surnameTxt are JTextField components
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();

        // Construct the username in the format firstName_surname
        String Username = firstName + "_" + surname;

        // Validate if both firstName and surname are provided (non-empty and non-null)
        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Invalid username. Please make sure both first name and surname are provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Proceed with username validation
            System.out.println("Username is valid: " + Username);
            // You can add additional logic here to verify the username and password (authentication)
        }
    }

}