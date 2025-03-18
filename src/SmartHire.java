package projectPack;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */
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
    private JButton loginButton;
    private JButton createAccountButton;
    private JPanel background;
    private JTextField firstNameTxt;
    private JTextField surnameTxt;
    private JButton createAccountBtn;
    private JButton chooseAvatarBtn;
    private JButton BGMusicButton;
    private JButton settingsButton;


    public SmartHire() {
        String firstnameTxt = "";
        String surnameTxt = "";
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        // Launch the GUI
        JFrame myApp = new JFrame("SmartHire App");
        myApp.setContentPane(new SmartHire().SmartHireHub); // Set the main panel
        myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on window close
        myApp.pack(); // Automatically resize components
        myApp.setSize(800, 480);
        myApp.setVisible(true);
    }

}
