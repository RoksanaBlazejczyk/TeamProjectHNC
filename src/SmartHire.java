/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */


import projectPack.Authentication;
import projectPack.Database;
import projectPack.Questions;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;





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
    private JRadioButton aLbl;
    private JRadioButton bLbl;
    private JRadioButton cLbl;
    private JRadioButton dLbl;
    private JLabel photoLbl;
    private JLabel questionLbl;
    private JLabel profilePic;
    private JLabel profileName;
    private JCheckBox readRules;
    private JPanel leaderboardPanel;
    private JPanel resultBG;
    private JLabel figureImg;
    private JLabel scoreTxt;
    private JCheckBox optBtn;
    private JButton leaderboardBtn;
    private JButton printToFileBtn;
    private JButton finishBtn;
    private JProgressBar progressBar;
    private ButtonGroup AvatarButtonGroup;
    private Clip backgroundMusicClip; // Music player
    private Timer timer;
    private int secondsElapsed = 0; // Track elapsed seconds
    private JLabel timerLbl;// Label to display the timer
    private List<String> sessionTimes = new ArrayList<>();
    // Array to store usernames and passwords
    private String[] usernames = new String[100];
    private String[] passwords = new String[100];
    private int usernameCount = 0;
    // Store correct answers for both questions
    private String correctAnswer;




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
    public void music() {
        try {
            File musicFile = new File("src/music/music.wav"); // Adjust path if needed
            if (!musicFile.exists()) {
                System.out.println("Music file not found: " + musicFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Loop the music indefinitely
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            // Start playing
            clip.start();
            System.out.println("Music is playing in a loop...");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Method to start the timer once user logs in.
     */
    private void startTimer(){
        if (timer != null){
            timer.cancel(); //cancel any existing one
        }
        secondsElapsed = 0; //reset timer

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                secondsElapsed++;
                timerLbl.setText("Time: " + formatTime(secondsElapsed));
            }
        }, 0, 1000);

    }
    /**
     * Formats the time into mm:ss format.
     */
    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Displays all session times for the admin.
     */
    public void displayAdminTimes() {
        if (sessionTimes.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "No session times recorded.", "Admin Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder times = new StringBuilder("Recorded Session Times:\n");
            for (int i = 0; i < sessionTimes.size(); i++) {
                times.append("Session ").append(i + 1).append(": ").append(sessionTimes.get(i)).append("\n");
            }
            JOptionPane.showMessageDialog(SmartHireHub, times.toString(), "Admin Info", JOptionPane.INFORMATION_MESSAGE);
        }
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
        } else if (mainPanel.getComponent(2).isVisible()) {
            return "rulesScreen";
        } else if (mainPanel.getComponent(3).isVisible()) {
            return "questionsScreen";
        } else if (mainPanel.getComponent(4).isVisible()) {
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
            Authentication.createAccount();
        }
    }



    public SmartHire() {
        // Generate 100 random 4-digit passwords
        generatePasswords();
        BGMusicButton.setEnabled(true);



        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });

        // Set up action listeners for answer buttons
        aLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(aLbl.getText());
            }
        });
        bLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(bLbl.getText());
            }
        });
        cLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(cLbl.getText());
            }
        });
        dLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer(dLbl.getText());
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
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                cards.show(mainPanel, "Card4"); //Show questions panel
                startTimer();
                // Display random questions for each difficulty
                displayRandomQuestions("easy");
                displayRandomQuestions("medium");
                displayRandomQuestions("hard");

            }
        });
        readRules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readRules.isSelected()) {
                    nextButton.setEnabled(true);
                } else {
                    nextButton.setEnabled(false);
                }
            }
        });
        progressBar.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
                new Thread(() -> {
                    for (int i = 0; i <= 100; i++) {
                        try {
                            Thread.sleep(50); // Simulate work
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        progressBar.setValue(i);
                    }
                }).start();


            }
        });

        BGMusicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button clicked! Playing music...");
                music();

            }

        });
    }

    /**
     * Fetches and displays two random questions based on the given difficulty.
     * @param difficulty The difficulty level (e.g., "easy", "medium", "hard")
     */
    private void displayRandomQuestions(String difficulty) {
        // Get 2 random questions for the given difficulty from the database
        List<Questions> questionsList = Database.getRandomQuestionsWithOptions(difficulty);

        if (questionsList != null && questionsList.size() == 2) {
            // Display both questions in sequence
            Questions question1 = questionsList.get(0);


            // Show both questions - rename variable if u wish :)
            String QuestionMain = question1.getQuestionText() ;
            questionLbl.setText(QuestionMain);

            // Set options for first question
            aLbl.setText(question1.getOptionA());
            bLbl.setText(question1.getOptionB());
            cLbl.setText(question1.getOptionC());
            dLbl.setText(question1.getOptionD());

            // Store the correct answer for checking (first question)
            correctAnswer = question1.getCorrectAnswer();


        } else {
            JOptionPane.showMessageDialog(SmartHireHub, "Not enough questions found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Checks the answer selected by the user.
     * @param selectedAnswer The answer selected by the user
     */
    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(correctAnswer)) {
            JOptionPane.showMessageDialog(SmartHireHub, "Correct Answer!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(SmartHireHub, "Incorrect Answer. The correct answer was: " + correctAnswer, "Try Again", JOptionPane.ERROR_MESSAGE);
        }
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
     * Method to log in with firstName_surname and provided password
     */
    public void logIn() {
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();
        String Username = firstName + "_" + surname;

        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub,
                    "Invalid username. Please make sure both first name and surname are provided.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;

        for (int i = 0; i < usernameCount; i++) {
            if (usernames[i].equals(Username)) {
                String inputPassword = passwordTxt.getText().trim();
                String correctPassword = passwords[i];

                if (inputPassword.equals(correctPassword)) {
                    JOptionPane.showMessageDialog(SmartHireHub,
                            "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    found = true;

                    // Switch to rulesScreen
                    CardLayout cards = (CardLayout) mainPanel.getLayout();
                    cards.show(mainPanel, "Card3");
                    BGMusicButton.setEnabled(true);
                    settingsButton.setEnabled(true);

                    // Force UI refresh
                    mainPanel.revalidate();
                    mainPanel.repaint();
                    return;
                } else {
                    JOptionPane.showMessageDialog(SmartHireHub,
                            "Invalid password. Please try again.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    found = true;
                    break;
                }
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(SmartHireHub,
                    "Username not found. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}