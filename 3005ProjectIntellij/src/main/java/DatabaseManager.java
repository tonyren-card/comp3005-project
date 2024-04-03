import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseManager {
    // USER CREDENTIALS
    private String url = "jdbc:postgresql://localhost:5432/Fitness";
    private String user = "postgres";
    private String pw = "cs19DB22sql!";
    private Connection connection;

    public DatabaseManager() {
        try {
            // Connect to PostgreSQL server
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pw);
            if (connection != null) {
                System.out.println("Connected to the database.\n");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public void memberFunctionManager() {
        try {
            Scanner scanner = new Scanner(System.in);

            boolean continueRunning = true;

            while (continueRunning) {
                System.out.println("Member management.");
                System.out.println("1. User Registration.");
                System.out.println("2. Profile Management.");
                System.out.println("3. Dashboard Display.");
                System.out.println("4. Schedule Management.");
                System.out.println("5. Exit.");

                int select = scanner.nextInt();
                scanner.nextLine();

                switch (select) {
                    case 1:
                        userRegistration();
                        break;
                    case 2:
                        profileManagement();
                        break;
                    case 3:
                        dashboardDisplay();
                        break;
                    case 5:
                        continueRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void userRegistration() {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get user info
            System.out.println("Enter first name:");
            String firstName = scanner.nextLine();
            System.out.println("Enter last name:");
            String lastName = scanner.nextLine();
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            //TODO: check if email and password combo is already in database

            // Prepare SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Users (FirstName, LastName, Email, Password) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            // Execute SQL statement
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully.");
            } else {
                System.out.println("Failed to register user.");
            }

            // Close prepared statement
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void profileManagement() {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get user info
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            // Prepare SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");;
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute SQL statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if anything returned
            if (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String userEmail = resultSet.getString("Email");
                String userPassword = resultSet.getString("Password");

                System.out.println("User Info:");
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + userEmail);
                System.out.println("Password: " + userPassword);

                boolean continueRunning = true;

                while (continueRunning) {
                    System.out.println("What would you like to update?");
                    System.out.println("1. Personal Information.");
                    System.out.println("2. Fitness Goals.");
                    System.out.println("3. Health Metrics.");
                    System.out.println("4. Return.");

                    int select = scanner.nextInt();
                    scanner.nextLine();

                    switch (select) {
                        case 1:
                            informationUpdate(email, password);
                            break;
                        case 2:
                            goalsUpdate(email, password);
                            break;
                        case 3:
                            metricsUpdate(email, password);
                            break;
                        case 4:
                            continueRunning = false;
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                }
            } else {
                System.out.println("Incorrect email and/or password.");
            }

            // Close result set and prepared statement
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void informationUpdate(String email, String password) {
        Scanner scanner = new Scanner(System.in);

        try {
            boolean continueRunning = true;

            while (continueRunning) {
                System.out.println("What would you like to update?");
                System.out.println("1. First Name.");
                System.out.println("2. Last Name.");
                System.out.println("3. Email.");
                System.out.println("4. Password.");
                System.out.println("5. Return.");

                int select = scanner.nextInt();
                scanner.nextLine();
                PreparedStatement preparedStatement = null;
                String emailNew = null;
                String passwordNew = null;

                switch (select) {
                    case 1:
                        System.out.println("Enter new first name:");
                        String firstNameNew = scanner.nextLine();
                        preparedStatement = connection.prepareStatement("UPDATE Users SET FirstName = ? WHERE Email = ? AND Password = ?");
                        preparedStatement.setString(1, firstNameNew);
                        break;
                    case 2:
                        System.out.println("Enter new last name:");
                        String lastNameNew = scanner.nextLine();
                        preparedStatement = connection.prepareStatement("UPDATE Users SET LastName = ? WHERE Email = ? AND Password = ?");
                        preparedStatement.setString(1, lastNameNew);
                        break;
                    case 3:
                        System.out.println("Enter new email:");
                        emailNew = scanner.nextLine();
                        preparedStatement = connection.prepareStatement("UPDATE Users SET Email = ? WHERE Email = ? AND Password = ?");
                        preparedStatement.setString(1, emailNew);
                        break;
                    case 4:
                        System.out.println("Enter new password:");
                        passwordNew = scanner.nextLine();
                        preparedStatement = connection.prepareStatement("UPDATE Users SET Password = ? WHERE Email = ? AND Password = ?");
                        preparedStatement.setString(1, passwordNew);
                        break;
                    case 5:
                        continueRunning = false;
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
                if (preparedStatement != null) {
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, password);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Update successful.");
                        if (emailNew != null) { email = emailNew; }
                        if (passwordNew != null) { password = passwordNew; }
                    } else {
                        System.out.println("Update failed.");
                    }
                    preparedStatement.close();
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void goalsUpdate(String email, String password) {

    }

    public void metricsUpdate(String email, String password) {

    }

    public void dashboardDisplay() {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get user info
            System.out.println("Enter email:");
            String email = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            // Prepare SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");;
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute SQL statement
            ResultSet resultSet = preparedStatement.executeQuery();

            //TODO: check if user is a member and not a trainer or admin

            // Check if anything returned
            if (resultSet.next()) {
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String userEmail = resultSet.getString("Email");
                String userPassword = resultSet.getString("Password");

                System.out.println("User Info:");
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Email: " + userEmail);
                System.out.println("Password: " + userPassword);

                boolean continueRunning = true;

                while (continueRunning) {
                    System.out.println("What would you like to display?");
                    System.out.println("1. Exercise Routines.");
                    System.out.println("2. Fitness Achievements.");
                    System.out.println("3. Health Statistics.");
                    System.out.println("4. Return.");

                    int select = scanner.nextInt();
                    scanner.nextLine();

                    switch (select) {
                        case 1:
                            routineDisplay(email, password);
                            break;
                        case 2:
                            achievementDisplay(email, password);
                            break;
                        case 3:
                            statisticDisplay(email, password);
                            break;
                        case 4:
                            continueRunning = false;
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                }
            } else {
                System.out.println("Incorrect email and/or password.");
            }

            // Close result set and prepared statement
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void routineDisplay(String email, String password) {

    }

    public void achievementDisplay(String email, String password) {

    }

    public void statisticDisplay(String email, String password) {

    }

    public void scheduleManagement() {

    }

//    public void authenticate() {
//    //function for possible use, to clean up code for email/password from member functions
//    //instead of repeating code for email/password checking in the functions themselves
//        Scanner scanner = new Scanner(System.in);
//
//        // Get user info
//        System.out.println("Enter email:");
//        String email = scanner.nextLine();
//        System.out.println("Enter password:");
//        String password = scanner.nextLine();
//
//        // Prepare SQL statement
//        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");;
//        preparedStatement.setString(1, email);
//        preparedStatement.setString(2, password);
//
//        // Execute SQL statement
//        ResultSet resultSet = preparedStatement.executeQuery();
//
//        // Check if anything returned
//        if (resultSet.next()) {
//            String firstName = resultSet.getString("FirstName");
//            String lastName = resultSet.getString("LastName");
//            String userEmail = resultSet.getString("Email");
//            String userPassword = resultSet.getString("Password");
//
//            System.out.println("User Info:");
//            System.out.println("First Name: " + firstName);
//            System.out.println("Last Name: " + lastName);
//            System.out.println("Email: " + userEmail);
//            System.out.println("Password: " + userPassword);
//        }
}
