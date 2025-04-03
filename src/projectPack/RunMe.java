package projectPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * IntelliJ IDEA:
 * Go to File -> Project Structure -> Libraries.
 * Click the + button and select Java.
 * Navigate to the directory where you saved the SQL Server JDBC driver JAR file and select it.
 * Click OK.
 */
public class RunMe {

    public static Connection getConnection() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://databasequestions.database.windows.net:1433;" +
                "database=QuestionsIQTest;" +
                "user=SHAdmin@databasequestions;" +
                "password=Pa$$w0rd;" + // Replace with your actual password
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;";

        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionUrl);

        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC driver not found.");
            throw new SQLException("SQL Server JDBC driver not found.", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e; // Rethrow the SQLException to propagate it
        }
    }

    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = getConnection();
            if (connection != null) {
                System.out.println("Database connection successful!");
                // Perform database operations here...
                    //WE ARE WRITING HEREEEEEEEEEE sql//


            }
        } catch (SQLException e) {
            System.err.println("An error occurred during database operations: " + e.getMessage());

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}