/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
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
    private JButton BGMusicButton;
    private JButton settingsButton;
    private JTextField outputPasswordTxt;
    private JPanel avatarPanel;
    private JRadioButton avatar1RadioBtn;
    private JRadioButton avatar2RadioBtn;
    private JRadioButton avatar3RadioBtn;
    private JRadioButton avatar4RadioBtn;
    private JRadioButton avatar5RadioBtn;
    private JRadioButton avatar6RadioBtn;
    private JRadioButton avatar7RadioBtn;
    private JRadioButton avatar8RadioBtn;
    private JRadioButton avatar9RadioBtn;
    private JRadioButton avatar10RadioBtn;
    private JRadioButton avatar11RadioBtn;
    private JRadioButton avatar12RadioBtn;
    private JTextPane rulesTxt;
    private JPanel settingsPanel;
    private JPanel topPanel;
    private JPanel profilePanel;
    private ButtonGroup AvatarButtonGroup;

    // Array to store usernames and passwords
    private String[] usernames = new String[100];
    private String[] passwords = new String[100];
    private int usernameCount = 0;

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
        else if (mainPanel.getComponent(2).isVisible()) {
            return "rulesScreen";
        }
        else if (mainPanel.getComponent(3).isVisible()) {
            return "questionsScreen";
        }
        else if (mainPanel.getComponent(4).isVisible()) {
            return "resultsScreen";
        }
        // If no panel is visible or another panel is showing, you can return a default value or error
        else {
            return "Unknown Panel";
        }
    }

    public void goToNextScreen() {
        String currentPanel = getCurrentPanel();
        if (currentPanel.equals("loginScreen")) {
            createAccount();
        }
    }

    public SmartHire() {
        // Generate 100 random 4-digit passwords
        generatePasswords();

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });

        createAccountBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel card = null;
                for (Component comp : mainPanel.getComponents()) {
                    if (comp.isVisible() == true) {
                        card = (JPanel) comp;
                    }
                }
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                if (card.getName().equals(loginScreen.getName())) {
                    cards.next(mainPanel);
                }
                goToNextScreen();
            }
        });

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createUsername();
            }
        });
    }

    /**
     * Generates 100 unique random 4-digit passwords and associates them with usernames
     */
    private void generatePasswords() {
        Set<String> uniquePasswords = new HashSet<>();
        Random random = new Random();

        while (uniquePasswords.size() < 100) {
            int randomPassword = 1000 + random.nextInt(9000);  // Generates a 4-digit number
            uniquePasswords.add(String.valueOf(randomPassword));
        }

        passwords = uniquePasswords.toArray(new String[0]);
        outputPasswordTxt.setText("Passwords generated successfully!");
    }

    /**
     * Generates a username in the format name_surname and displays a generated password
     */
    private void createUsername() {
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();

        // Validation for first name and surname
        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please enter both first name and surname!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if validation fails
        }

        // Validation for avatar selection
        if (AvatarButtonGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please select an avatar!", "No avatar selected!", JOptionPane.ERROR_MESSAGE);
            return; // Exit the method if no avatar is selected
        }

        // Format the username
        String username = firstName + "_" + surname;

        // Randomly assign a password to the username
        String password = passwords[usernameCount % passwords.length]; // Assign a password
        passwords[usernameCount] = password;

        // Display the generated username and password
        JOptionPane.showMessageDialog(SmartHireHub, "Username Created: " + username + "\nPassword: " + password, "Success", JOptionPane.INFORMATION_MESSAGE);

        // Store the username in the array
        usernames[usernameCount] = username;
        usernameCount++;

        // Optionally set the username to a text field or store it in a variable
        usernameTxt.setText(username);  // Set the generated username in the text field
        outputPasswordTxt.setText(password);  // Set the generated password in the password field

        // Navigate to the previous screen using CardLayout
        CardLayout cards = (CardLayout) mainPanel.getLayout();
        cards.previous(mainPanel);
    }

    /**
     * Method to handle account creation logic
     */
    private void createAccount() {
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
            JOptionPane.showMessageDialog(SmartHireHub, "Account created successfully!\nWelcome, " + firstName + "_" + surname, "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Method to log in with firstName_surname and provided password
     */
    public void logIn() {
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();

        // Construct the username in the format firstName_surname
        String Username = firstName + "_" + surname;

        // Validate if both firstName and surname are provided (non-empty and non-null)
        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Invalid username. Please make sure both first name and surname are provided.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Authentication logic: Check if the username exists and if the password matches
            boolean found = false;
            for (int i = 0; i < usernameCount; i++) {
                if (usernames[i].equals(Username)) {
                    // If username matches, check the password
                    String inputPassword = passwordTxt.getText().trim();
                    String correctPassword = passwords[i];

                    if (inputPassword.equals(correctPassword)) {
                        JOptionPane.showMessageDialog(SmartHireHub, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        found = true;
                        break;
                    } else {
                        JOptionPane.showMessageDialog(SmartHireHub, "Invalid password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(SmartHireHub, "Username not found. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}