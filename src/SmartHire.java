/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */

import projectPack.Authentication;
import projectPack.Questions;
import projectPack.DatabaseConnection;
import projectPack.Settings;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;


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
    private JRadioButton avatar1RadioBtn, avatar2RadioBtn, avatar3RadioBtn, avatar4RadioBtn, avatar5RadioBtn, avatar6RadioBtn;
    private JRadioButton avatar7RadioBtn, avatar8RadioBtn, avatar9RadioBtn, avatar10RadioBtn, avatar11RadioBtn, avatar12RadioBtn;
    private JTextPane rulesTxt;
    private JPanel settingsPanel;
    private JPanel topPanel;
    private JPanel profilePanel;
    private JRadioButton aLbl, bLbl, cLbl, dLbl;
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
    private ButtonGroup AvatarButtonGroup, AnswersBtnGroupRadio;
    private Clip backgroundMusicClip; // Music player
    private Timer timer;
    private int secondsElapsed = 0; // Track elapsed seconds
    private JLabel timerLbl;// Label to display the timer
    private JButton startBtn;
    private JLabel iqTxt;
    private List<String> sessionTimes = new ArrayList<>();
    // Array to store usernames and passwords
    private String[] usernames = new String[100];
    private String[] passwords = new String[100];
    private int usernameCount = 0;
    private String correctAnswer;
    private List<Questions> allQuestions = new ArrayList<>();
    private List<Questions> currentQuestionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private int correctAnswersCount = 0;


    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame myApp = new JFrame("SmartHire App");

            // Create an instance of SmartHire and set it to the frame
            SmartHire app = new SmartHire();

            myApp.setContentPane(app.SmartHireHub);
            myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myApp.pack();
            myApp.setSize(1000, 480);
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
    private void startTimer() {
        if (timer != null) {
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

        try {
            DatabaseConnection.getConnection(); // Ensure the connection is established

            // Load specific number of random questions for each difficulty
            List<Questions> easyQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("easy", 8);
            List<Questions> mediumQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("medium", 12);
            List<Questions> hardQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("hard", 5);

            // Combine all questions into one list and shuffle
            allQuestions.addAll(easyQuestions);
            allQuestions.addAll(mediumQuestions);
            allQuestions.addAll(hardQuestions);
            currentQuestionList.addAll(allQuestions);
            Collections.shuffle(currentQuestionList);

        } catch (SQLException e) {
            System.err.println("SQL Server JDBC driver not found.");
            System.exit(0);
        }

        BGMusicButton.setEnabled(true);
        nextButton.setVisible(false);

        // Action listeners for buttons
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the question panel (Card4)
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                cards.show(mainPanel, "Card4");
                startTimer(); // Start the timer (if required)
                nextButton.setVisible(true); // Make the Next button visible
                displayQuestionAtIndex(currentQuestionIndex); // Display the first question
            }
        });

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
                checkIfAnyRadioButtonSelected();
            }
        });
        bLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAnyRadioButtonSelected();
            }
        });
        cLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAnyRadioButtonSelected();
            }
        });
        dLbl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAnyRadioButtonSelected();
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
                // Disable next button until answer is checked
                nextButton.setEnabled(false);

                // Get the selected answer from radio buttons
                String selectedAnswer = null;
                if (aLbl.isSelected()) selectedAnswer = "A";
                if (bLbl.isSelected()) selectedAnswer = "B";
                if (cLbl.isSelected()) selectedAnswer = "C";
                if (dLbl.isSelected()) selectedAnswer = "D";

                // Check if an answer is selected
                if (selectedAnswer != null) {
                    // Get the current question based on the current question index
                    Questions currentQuestion = currentQuestionList.get(currentQuestionIndex);


                    // Call checkAnswer with selectedAnswer and currentQuestion
                    checkAnswer(selectedAnswer, currentQuestion);  // Checks if the answer is correct

                    currentQuestionIndex++; // Move on to the next question
                    displayNextQuestion();  // Update the UI with the next question

                    // Clear the selection on the radio buttons for the next question
                    AnswersBtnGroupRadio.clearSelection();

                    // Re-enable the next button for the next question
                    nextButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(SmartHireHub, "Please select an answer!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        readRules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readRules.isSelected()) {
                    startBtn.setEnabled(true);
                } else {
                    startBtn.setEnabled(false);
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
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                cards.show(mainPanel, "Card4"); //Show questions panel
                startTimer();
                nextButton.setVisible(true);
                displayRandomQuestions("easy");
                displayRandomQuestions("medium");
                displayRandomQuestions("hard");
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SmartHireHub);
                Settings.showSettingsDialog(frame);
            }
        });
    }

    // Method to check if any radio button is selected
    private void checkIfAnyRadioButtonSelected() {
        if (aLbl.isSelected() || bLbl.isSelected() || cLbl.isSelected() || dLbl.isSelected()) {
            nextButton.setEnabled(true);  // Enable the next button
        } else {
            nextButton.setEnabled(false); // Disable the next button if no selection
        }
    }

    // Method to display the question at the current index
    private void displayQuestionAtIndex(int index) {
        if (index < currentQuestionList.size()) {
            Questions question = currentQuestionList.get(index);

            // Display the question and options
            questionLbl.setText(question.getQuestionText());
            aLbl.setText("A. " + question.getOptionA());
            bLbl.setText("B. " + question.getOptionB());
            cLbl.setText("C. " + question.getOptionC());
            dLbl.setText("D. " + question.getOptionD());

            // Set the image URL to the photoLbl
            String imageUrl = question.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Load and display the image in the photoLbl
                try {
                    ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                    Image image = imageIcon.getImage(); // Transform it
                    Image scaledImage = image.getScaledInstance(200, 200, Image.SCALE_SMOOTH); // Optional: Scale the image to fit
                    photoLbl.setIcon(new ImageIcon(scaledImage));
                } catch (MalformedURLException e) {
                    System.out.println("Invalid image URL: " + imageUrl);
                    photoLbl.setText("Image not available"); // Display error message if image fails to load
                }
            } else {
                photoLbl.setText("");
                photoLbl.setIcon(null);
            }
            //Check to see if it's correct
            correctAnswer = question.getCorrectAnswer();
        } else {
            //JOptionPane.showMessageDialog(SmartHireHub, "You have completed the quiz!", "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
            navigateToNextCard();
        }
    }


    private void displayNextQuestion() {
        if (currentQuestionIndex < currentQuestionList.size()) {
            displayQuestionAtIndex(currentQuestionIndex); //Next question in index
        } else {
            if (timer != null) {
                timer.cancel(); //Stop the time if it's running
            }
            JOptionPane.showMessageDialog(SmartHireHub, "You have completed the quiz!", "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
            navigateToNextCard();
            scoreTxt.setText(String.valueOf(correctAnswersCount));
            iqTxt.setText(String.valueOf(totalScore));
        }
    }

private void navigateToNextCard() {
    CardLayout cards = (CardLayout) mainPanel.getLayout();
    cards.next(mainPanel);
}

/**
 * Fetches and displays two random questions based on the given difficulty.
 *
 * @param difficulty The difficulty level (e.g., "easy", "medium", "hard")
 */
private void displayRandomQuestions(String difficulty) {
    // Use the pre-fetched allQuestions list, and filter by difficulty
    List<Questions> filteredQuestions = allQuestions.stream()
            .filter(q -> q.getDifficulty().equals(difficulty))
            .collect(Collectors.toList());

    if (filteredQuestions != null && filteredQuestions.size() > 0) {
        // Get the first question for this session (you can modify this logic for shuffling)
        Questions question = filteredQuestions.get(0);

        // Display question and options
        photoLbl.setText(question.getImageUrl());
        questionLbl.setText(question.getQuestionText());
        aLbl.setText("A. " + question.getOptionA());
        bLbl.setText("B. " + question.getOptionB());
        cLbl.setText("C. " + question.getOptionC());
        dLbl.setText("D. " + question.getOptionD());

        // Store the correct answer
        correctAnswer = question.getCorrectAnswer();
    } else {
        JOptionPane.showMessageDialog(SmartHireHub, "Not enough questions found!", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

/**
 * Checks the answer selected by the user.
 *
 * @param selectedAnswer The answer selected by the user
 */
private void checkAnswer(String selectedAnswer, Questions question) {
    // Check if the selected answer is correct
    if (selectedAnswer.equals(question.getCorrectAnswer())) {
        totalScore += question.getScore();
        correctAnswersCount++;
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
    //  outputPasswordTxt.setText("Passwords generated successfully!");
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