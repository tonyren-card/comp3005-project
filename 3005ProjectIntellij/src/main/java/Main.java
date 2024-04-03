import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    // USER CREDENTIALS
    static String url = "jdbc:postgresql://localhost:5432/Fitness";
    static String user = "postgres";
    static String pw = "cs19DB22sql!";
    // Connection and statement declaration
    static Connection connection;
    static Statement statement;
    public static void main(String[] args) {
        try {
            // connect to PostgreSQL server
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pw);
            if (connection != null) {
                System.out.println("Connected to the database\n");
            } else {
                System.out.println("Failed to connect to the database");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
