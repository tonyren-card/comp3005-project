import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        Scanner scanner = new Scanner(System.in);

        boolean continueRunning = true;

        while (continueRunning) {
            System.out.println("Fitness Management System.");
            System.out.println("1. Member Functions.");
            System.out.println("2. Trainer Functions.");
            System.out.println("3. Admin Functions.");
            System.out.println("4. Exit.");

            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    dbManager.memberFunctionManager();
                    break;
                case 4:
                    continueRunning = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        dbManager.closeConnection();
    }
}
