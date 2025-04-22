/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */

import projectPack.*;

import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.sound.sampled.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
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
    private JPanel timerPanel;
    private JPanel profilePanel;
    private JRadioButton aLbl, bLbl, cLbl, dLbl;
    private JLabel photoLbl;
    private JTextArea questionLbl;
    private JLabel profilePic;
    private JLabel profileName;
    private JCheckBox readRules;
    private JPanel resultBG;
    private JLabel figureImg;
    private JLabel scoreTxt;
    private JCheckBox optBtn;
    private boolean optOut = false;
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
    private JPanel iconPanel;
    private List<String> sessionTimes = new ArrayList<>();
    private String[] usernames = new String[100];
    private String[] passwords = new String[100];
    private int usernameCount = 0;
    private String Username;
    private String correctAnswer;
    private List<Questions> allQuestions = new ArrayList<>();
    private List<Questions> currentQuestionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int totalScore = 85;
    private String finalTimeTaken;
    private int currentQuestion = 1;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame myApp = new JFrame("SmartHire App");

            // Create an instance of SmartHire and set it to the frame
            SmartHire app = new SmartHire();
            myApp.setContentPane(app.SmartHireHub);
            myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myApp.pack();
            myApp.setSize(800, 600);
            myApp.setVisible(true);
        });
    }

    /**
     * Method for background music
     */
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

            //Loop the music indefinitely
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            //Start playing
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

    /**
     * @return
     */
    public void showQuestion(int questionNumber) {
        if (questionNumber >= 1 && questionNumber <= currentQuestionList.size()) {
            Questions q = currentQuestionList.get(questionNumber - 1); // index starts at 0

        }
    }

    public String getCurrentPanel() {
        //Get the CardLayout from the mainPanel
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();

        //Check if the first component (loginScreen) is visible
        if (mainPanel.getComponent(0).isVisible()) {
            return "loginScreen";
        }
        //Check if another panel (e.g., "createAccountScreen") is visible
        else if (mainPanel.getComponent(1).isVisible()) {
            return "createAccountScreen";
        } else if (mainPanel.getComponent(2).isVisible()) {
            return "rulesScreen";
        } else if (mainPanel.getComponent(3).isVisible()) {
            return "questionsScreen";
        } else if (mainPanel.getComponent(4).isVisible()) {
            return "resultsScreen";
        }
        //If no panel is visible or another panel is showing, you can return a default value or error
        else {
            return "Unknown Panel";
        }
    }

    /**
     * Navigate to creation screen
     */
    public void goToCreateScreen() {
        CardLayout cards = (CardLayout) mainPanel.getLayout();
        cards.show(mainPanel, "Card2");
    }

    /**
     * Genetare random passwords, pulling the questions randomly
     */
    public SmartHire() {
        //Generate 100 random 4-digit passwords
        generatePasswords();

        try {
            DatabaseConnection.getConnection(); // Ensure the connection is established

            //Load specific number of random questions for each difficulty
            List<Questions> easyQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("easy", 8);
            List<Questions> mediumQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("medium", 12);
            List<Questions> hardQuestions = DatabaseConnection.getRandomQuestionsByDifficulty("hard", 5);

            //Combine all questions into one list and shuffle
            allQuestions.addAll(easyQuestions);
            allQuestions.addAll(mediumQuestions);
            allQuestions.addAll(hardQuestions);
            currentQuestionList.addAll(allQuestions);
            Collections.shuffle(currentQuestionList);
            currentQuestion = 1; // Add this field if not declared already
            showQuestion(currentQuestion);
            updateProgress(currentQuestion);


        } catch (SQLException e) {
            System.err.println("SQL Server JDBC driver not found.");
            System.exit(0);
        }

        BGMusicButton.setEnabled(true);
        nextButton.setVisible(false);

        //Action listeners for buttons
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Show the question panel (Card4)
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                cards.show(mainPanel, "Card4");
                startTimer(); //Start the timer
                nextButton.setVisible(true); //Make the Next button visible
                displayQuestionAtIndex(currentQuestionIndex); //Display the first question
            }
        });


        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logIn();
            }
        });

        //Set up action listeners for answer buttons
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
                goToCreateScreen();
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
                if (currentQuestion < 25) {
                    currentQuestion++;
                    showQuestion(currentQuestion); // your method to load next question
                    updateProgress(currentQuestion); // ✅ this updates the progress bar
                }
                //Disable next button until answer is checked
                nextButton.setEnabled(false);


                //Get the selected answer from radio buttons
                String selectedAnswer = null;
                if (aLbl.isSelected()) selectedAnswer = "A";
                if (bLbl.isSelected()) selectedAnswer = "B";
                if (cLbl.isSelected()) selectedAnswer = "C";
                if (dLbl.isSelected()) selectedAnswer = "D";

                //Check if an answer is selected
                if (selectedAnswer != null) {
                    //Get the current question based on the current question index
                    Questions currentQuestion = currentQuestionList.get(currentQuestionIndex);


                    //Call checkAnswer with selectedAnswer and currentQuestion
                    checkAnswer(selectedAnswer, currentQuestion);  //Checks if the answer is correct

                    currentQuestionIndex++; //Move on to the next question
                    displayNextQuestion();  //Update the UI with the next question

                    //Clear the selection on the radio buttons for the next question
                    AnswersBtnGroupRadio.clearSelection();

                    //Re-enable the next button for the next question
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
        progressBar.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 && progressBar.isShowing()) {
                progressBar.setMinimum(1); // start from 1
                progressBar.setMaximum(25);
                progressBar.setValue(1); // default value
                progressBar.setStringPainted(true);
                progressBar.setString("Question 1 of 25");

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
        leaderboardBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<LeaderboardEntry> topEntries = DatabaseLeaderboard.getTopLeaderboard(15); // Top 15
                String[] columnNames = {"Rank", "Name", "IQ Score", "Time Taken"};
                //Fill out table
                Object[][] data = new Object[topEntries.size()][4];
                for (int i = 0; i < topEntries.size(); i++) {
                    LeaderboardEntry entry = topEntries.get(i);
                    data[i][0] = entry.getRank() > 0 ? entry.getRank() : i + 1;
                    data[i][1] = entry.getName();
                    data[i][2] = entry.getIqScore();
                    data[i][3] = entry.getTimeTaken();
                }
                //Create table for leaderboard
                JTable table = new JTable(data, columnNames);
                table.setEnabled(false);
                table.setFillsViewportHeight(true);
                //Set widths of columns
                TableColumnModel columnModel = table.getColumnModel();
                columnModel.getColumn(0).setPreferredWidth(20);  //Rank
                columnModel.getColumn(1).setPreferredWidth(200); //Name
                columnModel.getColumn(2).setPreferredWidth(40);  //IQ Score
                columnModel.getColumn(3).setPreferredWidth(60);  //Time Taken
                //Center align all data
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
                for (int i = 0; i < columnModel.getColumnCount(); i++) {
                    columnModel.getColumn(i).setCellRenderer(centerRenderer);
                }
                table.setRowHeight(25); //Set row height if you wish
                JScrollPane scrollPane = new JScrollPane(table); //Add table to scroll pane
                scrollPane.setPreferredSize(new Dimension(600, 400));
                JOptionPane.showMessageDialog(SmartHireHub, scrollPane, "Top 15 Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        finishBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        null,
                        "Thank you for taking the SmartHire IQ Test! Send donations to xxxxxxxx xx-xx-xx to help fund our empire!\nHave you finished with the leaderboard and saving your results to a file?",
                        "Finish Confirmation",
                        JOptionPane.YES_NO_OPTION
                );
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0); // Exit the program
                } else {
                    // Stay on the current screen (do nothing)
                    JOptionPane.showMessageDialog(null, "You can continue with the leaderboard or save your results at the top right.");
                }
            }
        });
        optBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // If the checkbox is selected, show the confirmation dialog
                if (optBtn.isSelected()) {
                    int confirm = JOptionPane.showConfirmDialog(SmartHireHub,
                            "Are you sure you want to opt out of the leaderboard?\nYour score will be submitted anonymously.",
                            "Confirm Opt-Out",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        optOut = true;  // Confirm opt-out
                        System.out.println("Opt-out status: " + optOut); // Debugging log
                    } else {
                        optOut = false; // Cancel opt-out if user pressed "No"
                        optBtn.setSelected(false);  // Uncheck the checkbox
                        System.out.println("Opt-out status: " + optOut); // Debugging log
                    }
                } else {
                    optOut = false; // If the checkbox is unchecked
                    System.out.println("Opt-out status: " + optOut); // Debugging log
                }
            }
        });
    }
    // This goes OUTSIDE the constructor or any other method — just inside your class
    public void updateProgress(int questionNumber) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(questionNumber);
            progressBar.setString("Question " + questionNumber + " of 25");
        });
    }


    /**
     * Method to check if any radio button is selected
     */
    private void checkIfAnyRadioButtonSelected() {
        if (aLbl.isSelected() || bLbl.isSelected() || cLbl.isSelected() || dLbl.isSelected()) {
            nextButton.setEnabled(true);  // Enable the next button
        } else {
            nextButton.setEnabled(false); // Disable the next button if no selection
        }
    }

    /**
     * Generates a username in the format name_surname and displays a generated password
     * V2 has validation to make sure only letters for names
     */
    private void createUsername() {
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();

        //Validation for first name and surname
        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please enter both first name and surname!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Only allow letters (both upper and lower case)
        if (!firstName.matches("[a-zA-Z]+") || !surname.matches("[a-zA-Z]+")) {
            JOptionPane.showMessageDialog(SmartHireHub, "First name and surname must only contain letters (no numbers or symbols)!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Validation for avatar selection
        if (AvatarButtonGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(SmartHireHub, "Please select an avatar!", "No avatar selected!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Format the username
        String username = firstName + "_" + surname;

        //Randomly assign a password to the username
        String password = passwords[usernameCount % passwords.length];
        passwords[usernameCount] = password;

        //Display the generated username and password
        JOptionPane.showMessageDialog(SmartHireHub, "Username Created: " + username + "\nPassword: " + password, "Success", JOptionPane.INFORMATION_MESSAGE);

        //Store the username in the array
        usernames[usernameCount] = username;
        usernameCount++;

        usernameTxt.setText(username);  // Set the generated username in the text field

        //Navigate to the previous screen using CardLayout
        CardLayout cards = (CardLayout) mainPanel.getLayout();
        cards.previous(mainPanel);
    }

    /**
     * Will be called to set next question into Labels
     *Can be moved to Questions class
     * @param index
     */
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

            // If image is missing, use default logo
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                imageUrl = "https://quizblobstorage.blob.core.windows.net/quizblobcontainer/SmartHireLogo.png";
            }

            try {
                ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                Image image = imageIcon.getImage(); // Transform it
                Image scaledImage = image.getScaledInstance(250, 250, Image.SCALE_SMOOTH); // Scale the image
                photoLbl.setIcon(new ImageIcon(scaledImage));
                photoLbl.setText(""); // Clear any previous error message
            } catch (MalformedURLException e) {
                System.out.println("Invalid image URL: " + imageUrl);
                photoLbl.setText("Image not available");
                photoLbl.setIcon(null);
            }

            // Store the correct answer for validation
            correctAnswer = question.getCorrectAnswer();
        } else {
            // End of the quiz
            JOptionPane.showMessageDialog(SmartHireHub, "You have completed the quiz!", "Quiz Completed", JOptionPane.INFORMATION_MESSAGE);
            navigateToNextCard();
        }
    }


    private void displayNextQuestion() {
        if (currentQuestionIndex < currentQuestionList.size()) {
            displayQuestionAtIndex(currentQuestionIndex);
        } else {
            if (timer != null) {
                timer.cancel(); // Stop the timer
                finalTimeTaken = formatTime(secondsElapsed); // Capture the time when the quiz ends
            }

            // Ask opt-out confirmation at the end (optional - or move to button handler if you prefer)
            if (optOut) {
                int confirm = JOptionPane.showConfirmDialog(SmartHireHub,
                        "Are you sure you want to opt out of the leaderboard?\nYour results will still be submitted, but marked as opted out.",
                        "Confirm Opt-Out",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm != JOptionPane.YES_OPTION) {
                    optOut = false;  // Reset optOut flag if user cancels
                }
            }

            String username = Username; // Always use the real username
            int score = totalScore;
            String timeTaken = finalTimeTaken;

            System.out.println("Uploading results: " + username + ", Score: " + score + ", Time: " + timeTaken + ", Opt-out: " + optOut);
            DatabaseLeaderboard.uploadResults(username, score, timeTaken, optOut);

            // Show the results screen
            navigateToNextCard();
            printToFileBtn.setVisible(true);
            iqTxt.setText(String.valueOf(totalScore));

            // Optional message
            if (optOut) {
                JOptionPane.showMessageDialog(SmartHireHub, "Your opt-out has been recorded.", "Opted Out", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Method to navigate to the next screen
     */
    private void navigateToNextCard() {
        CardLayout cards = (CardLayout) mainPanel.getLayout();
        cards.next(mainPanel);
    }

    /**
     * Fetches and displays randomised questions and set choices into relevant fields
     *
     * @param difficulty The difficulty level (e.g., "easy", "medium", "hard")
     */
    private void displayRandomQuestions(String difficulty) {
        // Use the pre-fetched allQuestions list, and filter by difficulty
        List<Questions> filteredQuestions = allQuestions.stream()
                .filter(q -> q.getDifficulty().equals(difficulty))
                .collect(Collectors.toList());

        if (filteredQuestions != null && !filteredQuestions.isEmpty()) {
            // Get the first question for this session
            Questions question = filteredQuestions.get(0);

            // Display the question and options
            questionLbl.setText(question.getQuestionText());
            aLbl.setText("A. " + question.getOptionA());
            bLbl.setText("B. " + question.getOptionB());
            cLbl.setText("C. " + question.getOptionC());
            dLbl.setText("D. " + question.getOptionD());

            String imageUrl = question.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                //Display images from database so photoLbl
                try {
                    ImageIcon imageIcon = new ImageIcon(new URL(imageUrl));
                    Image image = imageIcon.getImage(); // Transform it
                    Image scaledImage = image.getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    photoLbl.setIcon(new ImageIcon(scaledImage));
                    photoLbl.setText(""); // Clear any previous text
                } catch (MalformedURLException e) {
                    System.out.println("Invalid image URL: " + imageUrl);
                    photoLbl.setIcon(null);
                    photoLbl.setText("Image not available");
                }
            } else {
                photoLbl.setText("");
                photoLbl.setIcon(null);
            }

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
        //Check if the selected answer is correct
        if (selectedAnswer.equals(question.getCorrectAnswer())) {
            totalScore += question.getScore(); // Add the score from the database to the total score
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
    }

    /**
     * Method to log in with firstName_surname and provided password
     */
    public void logIn() {
        String firstName = firstNameTxt.getText().trim();
        String surname = surnameTxt.getText().trim();
        this.Username = firstName + "_" + surname;  // Use this.Username to refer to the instance variable

        if (firstName.isEmpty() || surname.isEmpty()) {
            JOptionPane.showMessageDialog(SmartHireHub,
                    "Invalid username. Please make sure both first name and surname are provided.",
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean found = false;

        for (int i = 0; i < usernameCount; i++) {
            if (usernames[i].equals(this.Username)) {  // Use this.Username to refer to the instance variable
                String inputPassword = passwordTxt.getText().trim();
                String correctPassword = passwords[i];

                if (inputPassword.equals(correctPassword)) {
                    JOptionPane.showMessageDialog(SmartHireHub,
                            "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    found = true;

                    profileName.setText(this.Username.replace("_", " "));  // Use this.Username

                    // Set the selected avatar's icon to profilePic label
                    ButtonModel selectedModel = AvatarButtonGroup.getSelection();
                    if (selectedModel != null) {
                        for (Enumeration<AbstractButton> buttons = AvatarButtonGroup.getElements(); buttons.hasMoreElements(); ) {
                            AbstractButton button = buttons.nextElement();
                            if (button.getModel() == selectedModel) {
                                profilePic.setIcon(button.getIcon());
                                break;
                            }
                        }
                    }
                    CardLayout cards = (CardLayout) mainPanel.getLayout();
                    cards.show(mainPanel, "Card3");
                    BGMusicButton.setVisible(true);
                    settingsButton.setVisible(true);
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