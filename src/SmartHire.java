import projectPack.Authentication;
/**
 * SmartHire IQ Team Project
 * Team: Roksana Blazejczyk, Marek Cudak, Robert Sneddon, Daniel Virlan
 */

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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Random;

import java.util.Optional;
import static java.sql.DriverManager.getConnection;

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
    private JButton aLbl;
    private JButton bLbl;
    private JButton cLbl;
    private JButton dLbl;
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
        //Hide components initially until certain screens
        profilePic.setVisible(false);
        profileName.setVisible(false);
        nextButton.setVisible(false);
        // Generate 100 random 4-digit passwords
        generatePasswords();
        BGMusicButton.setEnabled(true);



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
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cards = (CardLayout) mainPanel.getLayout();
                cards.show(mainPanel, "Card4"); //Show questions panel
                loadRandomQuestion();
            }
        });
        readRules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (readRules.isSelected()) {
                    nextButton.setVisible(true);
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
        createAccountBtn.setEnabled(false);
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

                    //Show user and picture in top left
                    profileName.setVisible(true);
                    profilePic.setVisible(true);

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

















    //DATABASE//

    public static class SQLiteConnection {
        private static final String DB_URL = "jdbc:sqlite:questions.db";

        public static Connection getConnection() throws SQLException {
            Connection conn = DriverManager.getConnection(DB_URL);
            initializeDatabase(conn);
            return conn;
        }


        private static void initializeDatabase(Connection conn) throws SQLException {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS questions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "question_text TEXT NOT NULL,"
                    + "option_a TEXT NOT NULL,"
                    + "option_b TEXT NOT NULL,"
                    + "option_c TEXT NOT NULL,"
                    + "option_d TEXT NOT NULL,"
                    + "correct_answer TEXT NOT NULL"
                    + ")";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }
        }
    }


    public static class CheckDBPath {
        public static void main(String[] args) {
            File dbFile = new File("C:/Users/pasar/OneDrive - New College Lanarkshire/HNC Computing NextGen/TeamProjectHNC/questions.db");

            if (dbFile.exists()) {
                System.out.println("✅ Database found at: " + dbFile.getAbsolutePath());
            } else {
                System.err.println("❌ Database NOT FOUND at: " + dbFile.getAbsolutePath());
            }
        }
    }


    public static class DatabaseTest {
        public void main(String[] args) {
            // Test connection and table creation
            try (Connection conn = SQLiteConnection.getConnection()) {
                System.out.println("✅ Database connection successful");

                // Verify table exists
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='questions'")) {
                    if (rs.next()) {
                        System.out.println("✅ Questions table exists");
                    } else {
                        System.err.println("❌ Questions table missing");
                    }
                }

                // Test data retrieval
                QuestionDAO dao = new QuestionDAO();
                Optional<List<Question>> questions = dao.getAllQuestions();

                if (questions.isPresent() && !questions.get().isEmpty()) {
                    System.out.println("✅ Found " + questions.get().size() + " questions");
                    questions.get().forEach(q -> System.out.println(q.getQuestionText()));
                } else {
                    System.err.println("❌ No questions found in database");
                }

            } catch (SQLException e) {
                System.err.println("❌ Database error: " + e.getMessage());
            }
        }
    }


    public static class TestConnection {
        public static void main(String[] args) {
            try (Connection conn = SQLiteConnection.getConnection()) {
                System.out.println("✅ Connected to the database successfully!");
            } catch (SQLException e) {
                System.err.println("❌ Failed to connect to the database: " + e.getMessage());
            }
        }
    }


    public static class Question {
        private int id;
        private String questionText;
        private String optionA;
        private String optionB;
        private String optionC;
        private String optionD;
        private char correctAnswer;

        // Constructor
        public Question(int id, String questionText, String optionA, String optionB, String optionC, String optionD, char correctAnswer) {
            this.id = id;
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
            this.correctAnswer = correctAnswer;
        }

        // Getters (and setters if needed)
        public String getQuestionText() { return questionText; }
        public String getOptionA() { return optionA; }
        public String getOptionB() { return optionB; }
        public String getOptionC() { return optionC; }
        public String getOptionD() { return optionD; }
        public char getCorrectAnswer() { return correctAnswer; }
    }


    public static class QuestionDAO {
        public Optional<List<Question>> getAllQuestions() {
            List<Question> questions = new ArrayList<>();
            String query = "SELECT * FROM questions";

            try (Connection connection = SQLiteConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Question question = new Question(
                            resultSet.getInt("id"),
                            resultSet.getString("question_text"),
                            resultSet.getString("option_a"),
                            resultSet.getString("option_b"),
                            resultSet.getString("option_c"),
                            resultSet.getString("option_d"),
                            resultSet.getString("correct_answer").charAt(0)
                    );
                    questions.add(question);
                }
            } catch (SQLException e) {
                System.err.println("Error fetching questions: " + e.getMessage());
                return Optional.empty();
            }
            return Optional.of(questions);
        }
    }


    private void loadRandomQuestion() {
        // SQL query to fetch a random question
        String query = "SELECT * FROM questions ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = SQLiteConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                String question = rs.getString("question_text");
                String option1 = rs.getString("option_a");
                String option2 = rs.getString("option_b");
                String option3 = rs.getString("option_c");
                String option4 = rs.getString("option_d");
                String correctAnswer = rs.getString("correct_answer");

                // Display the question and options on your Swing UI
                questionLbl.setText(question);  // Assume you have a JLabel named questionLbl for displaying the question
                aLbl.setText(option1);          // Assume aLbl, bLbl, cLbl, dLbl are for options
                bLbl.setText(option2);
                cLbl.setText(option3);
                dLbl.setText(option4);

                // Store the correct answer for later use (for checking user input)
                // You may want to create a variable for this if needed
                System.out.println("Correct Answer: " + correctAnswer);
            } else {
                JOptionPane.showMessageDialog(SmartHireHub, "No questions found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(SmartHireHub, "Error loading question from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



}