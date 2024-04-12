import java.sql.*;
import java.util.Scanner;

public class DatabaseManager {
    // USER CREDENTIALS
    private String url = "jdbc:postgresql://localhost:5432/Fitness";
    private String user = "postgres";
    private String pw = "cs19DB22sql!";
    private Connection connection;

    private int dbAdminID = 0;

    // Constructor
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

    //Close connection once quit
    public void closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }


    // FUNCTION MANAGERS
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
                    case 4:
                        scheduleManagement();
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

    public void adminFunctionManager() {
        try {
            Scanner scanner = new Scanner(System.in);

            // Get admin login
            System.out.println("Enter the email:");
            String email = scanner.nextLine();
            System.out.println("Enter the password:");
            String pw = scanner.nextLine();
            System.out.println();

            // Prepare SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *\n" +
                                                            "FROM Admins JOIN Users ON Admins.adminid = Users.userid\n" +
                                                            "WHERE Users.email = ? AND Users.password = ?;");
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pw);

            // Execute SQL statement
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if anything returned
            if (resultSet.next()) {
                this.dbAdminID = resultSet.getInt("AdminID");

                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String title = resultSet.getString("Title");
                String userEmail = resultSet.getString("Email");

                System.out.println("User Info:");
                System.out.println("First Name: " + firstName);
                System.out.println("Last Name: " + lastName);
                System.out.println("Title: " + title);
                System.out.println("Email: " + userEmail);
                System.out.println();

                int select;

                do {
                    System.out.println("\nSelect an option (type a number for selection):");
                    System.out.println("\t1: Room Booking Management");
                    System.out.println("\t2: Equipment Maintenance Monitoring");
                    System.out.println("\t3: Class Schedule Updating");
                    System.out.println("\t4: Billing and Payment Processing");
                    System.out.println("\t5: Exit");

                    select = scanner.nextInt();
                    scanner.nextLine();

                    switch (select) {
                        case 1:
                            roomBookingManagement();
                            break;
                        case 2:
                            equipMaintenance();
                            break;
                        case 3:
                            updateClassSchedule();
                            break;
                        case 4:
                            processBilling();
                            break;
                        case 5:
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                } while (select != 5);
            } else {
                System.out.println("Incorrect email and/or password.");
            }

            // Close result set and prepared statement
            resultSet.close();
            preparedStatement.close();

        } catch (Exception e){
            System.out.println(e);
        }
    }


    // MEMBER FUNCTIONS
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

                    //TODO: add pause and require user input before redisplaying menu options

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
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");;
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("UserId");

                preparedStatement = connection.prepareStatement("SELECT * FROM Routine WHERE MemberID = ?");
                preparedStatement.setInt(1, userId);
                resultSet = preparedStatement.executeQuery();

                System.out.println("Routines:");

                while (resultSet.next()) {
                    String category = resultSet.getString("Category");
                    int reps = resultSet.getInt("Reps");
                    int sets = resultSet.getInt("Sets");

                    System.out.println("Category: " + category);
                    System.out.println("Reps: " + reps);
                    System.out.println("Sets: " + sets);
                }

            } else {
                System.out.println("No routines found.");
            }

            // Close result set and prepared statement
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    public void achievementDisplay(String email, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");;
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("UserId");

                preparedStatement = connection.prepareStatement("SELECT * FROM FitnessAchievement WHERE MemberID = ?");
                preparedStatement.setInt(1, userId);
                resultSet = preparedStatement.executeQuery();

                System.out.println("Achievements:");

                while (resultSet.next()) {
                    String description = resultSet.getString("Description");
                    String date = resultSet.getString("DateAchieved");

                    System.out.println("Description: " + description);
                    System.out.println("Date Achieved: " + date);
                }

            } else {
                System.out.println("No achievements found.");
            }

            // Close result set and prepared statement
            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
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

    // ADMIN FUNCTIONS
    // Room booking management manager and action functions
    public void roomBookingManagement(){
        Scanner scanner = new Scanner(System.in);
        int select;
        do {
            System.out.println("\nRoom Booking Management - select an option:");
            System.out.println("\t1: Make a new booking");
            System.out.println("\t2: Modify an existing reservation");
            System.out.println("\t3: Delete a reservation");
            System.out.println("\t4: Exit");

            select = scanner.nextInt();
            scanner.nextLine();

            switch (select){
                case 1:
                    bookNewRoom();
                    break;
                case 2:
                    modifyRoomBooking();
                    break;
                case 3:
                    deleteRoomBooking();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }while (select != 4);
    }

    public void bookNewRoom(){
        //tables needed: Schedule, ManageSchedule, Timeslot, Room
        Scanner scanner = new Scanner(System.in);

        // variables to insert new data
        int scheduleID; // can be existing or new
        int roomID; // only existing
        String dayOfWeek;
        String startTime;
        String endTime;

        // USER PROMPTS
        // Step 1: Output rooms available on database and get user input
        try {
            ResultSet resultSet;
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM Room;");
            resultSet = statement.getResultSet();

            System.out.println("Select the room to reserve (enter the id, in #):");
            while (resultSet.next()) {
                System.out.print("ID=" + resultSet.getInt("roomID") + "\t");
                System.out.print(resultSet.getString("roomName") + "\t");
                System.out.println("(Capacity: " + resultSet.getInt("capacity") + ")");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        roomID = scanner.nextInt();
        scanner.nextLine();

        // Step 2: List existing bookings by roomID to avoid overbooking
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *\n" +
                    "FROM (Schedule NATURAL INNER JOIN Timeslot), Room\n" +
                    "WHERE Schedule.entitytype = 'Room' \n" +
                    "\tAND Schedule.EntityID = Room.RoomID\n" +
                    "\tAND Room.RoomID = ?;");
            preparedStatement.setInt(1,roomID);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Existing reservations for RoomID=" + roomID + ":");
            if (!resultSet.next()) {
                System.out.println("No existing reservations.");
            }else {
                do {
                    System.out.print("\t");
                    System.out.print(resultSet.getString("dayofweek") + "\t");
                    System.out.print(resultSet.getString("StartTime") + " - ");
                    System.out.println(resultSet.getString("EndTime"));
                } while (resultSet.next());
            }
        }catch (Exception e) {
            System.out.println(e);
        }

        // Step 3: User inputs for the reservation
        System.out.println("\nEnter the day of the week (eg. 'Monday'):");
        dayOfWeek = scanner.nextLine();
        System.out.println("Enter the start time of the time slot, in 24-hour format (eg. '13:00'):");
        startTime = scanner.nextLine();
        System.out.println("Enter the end time of the time slot, in 24-hour format (eg. '13:00'), later than the start time:");
        endTime = scanner.nextLine();

        // Step 4: prepare and execute the queries
        try {
            // See if there is an existing scheduleid from Schedule
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                                                                                    "FROM Schedule " +
                                                                            "WHERE EntityID = ? AND EntityType = 'Room' AND DayOfWeek = ?::day_of_week;");
            preparedStatement.setInt(1,roomID);
            preparedStatement.setString(2,dayOfWeek);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if inserting is needed
            if (!resultSet.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO Schedule(EntityID,EntityType,DayOfWeek)\n" +
                        "VALUES (?,'Room',?::day_of_week);");
                preparedStatement.setInt(1, roomID);
                preparedStatement.setString(2, dayOfWeek);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Failed to add new reservation.");
                    preparedStatement.close();
                    return;
                }
                preparedStatement = connection.prepareStatement("SELECT * " +
                        "FROM Schedule " +
                        "WHERE EntityID = ? AND EntityType = 'Room' AND DayOfWeek = ?::day_of_week;");
                preparedStatement.setInt(1, roomID);
                preparedStatement.setString(2, dayOfWeek);

                resultSet = preparedStatement.executeQuery();
                resultSet.next();
            }
            scheduleID = resultSet.getInt("ScheduleID");

            // insert into Timeslot
            preparedStatement = connection.prepareStatement("INSERT INTO TimeSlot(ScheduleID,StartTime,EndTime)\n" +
                                                                "VALUES (?,?,?)");
            preparedStatement.setInt(1, scheduleID);
            preparedStatement.setTime(2, Time.valueOf(startTime + ":00"));
            preparedStatement.setTime(3, Time.valueOf(endTime + ":00"));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully added new reservation.");
            }else{
                System.out.println("Failed to add new reservation.");
                preparedStatement.close();
                return;
            }

            // insert into Manageschedule (where if not already)
            preparedStatement = connection.prepareStatement("INSERT INTO Manageschedule\n" +
                    "SELECT ?, ?\n" +
                    "WHERE NOT EXISTS(SELECT * FROM Manageschedule WHERE Adminid=? AND Scheduleid=?);");
            preparedStatement.setInt(1, this.dbAdminID);
            preparedStatement.setInt(2, scheduleID);
            preparedStatement.setInt(3, this.dbAdminID);
            preparedStatement.setInt(4, scheduleID);

            preparedStatement.executeUpdate();

            // Close prepared statement
            preparedStatement.close();

        }catch (Exception e) {
            System.out.println(e);
        }
    }

    public void modifyRoomBooking(){
        //tables needed: ManageSchedule, Schedule, Room, Timeslot
        Scanner scanner = new Scanner(System.in);

        int slotID;
        int roomID;
        int scheduleID;
        String dayOfWeek;
        String startTime;
        String endTime;

        // USER PROMPTS
        //Step 1: List existing reservations where admin has access
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *\n" +
                    "FROM ((Manageschedule NATURAL INNER JOIN Schedule) NATURAL INNER JOIN Timeslot), Room\n" +
                    "WHERE Adminid=? \n" +
                    "\tAND Entitytype='Room' \n" +
                    "\tAND Schedule.entityid = Room.roomid;");
            preparedStatement.setInt(1, this.dbAdminID);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Existing reservations under this admin:");
            if (!resultSet.next()) {
                System.out.println("No existing reservations.");
                return;
            }else {
                do {
                    System.out.print("ID=" + resultSet.getInt("SlotID") + "\t");
                    System.out.print(resultSet.getString("RoomName") + "\t");
                    System.out.print(resultSet.getString("DayOfWeek") + " ");
                    System.out.print(resultSet.getString("StartTime") + " - ");
                    System.out.println(resultSet.getString("EndTime"));
                } while (resultSet.next());
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("\nEnter the ID of the existing reservation:");
        slotID = scanner.nextInt();
        scanner.nextLine();

        // Step 2: Select the room
        try {
            ResultSet resultSet;
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT * FROM Room;");
            resultSet = statement.getResultSet();

            System.out.println("Select the room to update to (or keep):");
            while (resultSet.next()) {
                System.out.print("ID=" + resultSet.getInt("roomID") + "\t");
                System.out.print(resultSet.getString("roomName") + "\t");
                System.out.println("(Capacity: " + resultSet.getInt("capacity") + ")");
            }
        }catch(Exception e){
            System.out.println(e);
        }
        roomID = scanner.nextInt();
        scanner.nextLine();

        //Step 3: List existing reservations by roomID and provide user inputs
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *\n" +
                    "FROM Schedule NATURAL INNER JOIN Timeslot\n" +
                    "WHERE Schedule.entitytype = 'Room' \n" +
                    "\tAND Schedule.EntityID = ?;");
            preparedStatement.setInt(1,roomID);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Existing reservations for RoomID=" + roomID + ":");
            if (!resultSet.next()) {
                System.out.println("No existing reservations.");
            }else {
                do {
                    System.out.print("\t");
                    System.out.print(resultSet.getString("dayofweek") + "\t");
                    System.out.print(resultSet.getString("StartTime") + " - ");
                    System.out.println(resultSet.getString("EndTime"));
                } while (resultSet.next());
            }
            preparedStatement.close();
        }catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\nEnter the day of the week (eg. 'Monday'):");
        dayOfWeek = scanner.nextLine();
        System.out.println("Enter the start time of the time slot, in 24-hour format (eg. '13:00'):");
        startTime = scanner.nextLine();
        System.out.println("Enter the end time of the time slot, in 24-hour format (eg. '13:00'), later than the start time:");
        endTime = scanner.nextLine();

        // Step 4: prepare modify queries and execute accordingly
        try {
            // See if there is an existing scheduleid from Schedule
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * " +
                    "FROM Schedule " +
                    "WHERE EntityID = ? AND EntityType = 'Room' AND DayOfWeek = ?::day_of_week;");
            preparedStatement.setInt(1,roomID);
            preparedStatement.setString(2,dayOfWeek);

            ResultSet resultSet = preparedStatement.executeQuery();

            // if inserting is needed
            if (!resultSet.next()) {
                preparedStatement = connection.prepareStatement("INSERT INTO Schedule(EntityID,EntityType,DayOfWeek)\n" +
                        "VALUES (?,'Room',?::day_of_week);");
                preparedStatement.setInt(1, roomID);
                preparedStatement.setString(2, dayOfWeek);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 0) {
                    System.out.println("Failed to add new reservation.");
                    preparedStatement.close();
                    return;
                }


                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement("SELECT * " +
                        "FROM Schedule " +
                        "WHERE EntityID = ? AND EntityType = 'Room' AND DayOfWeek = ?::day_of_week;");
                preparedStatement.setInt(1, roomID);
                preparedStatement.setString(2, dayOfWeek);

                resultSet = preparedStatement.executeQuery();
                resultSet.next();
            }
            scheduleID = resultSet.getInt("ScheduleID");

            // insert into Manageschedule (if not exists)
            preparedStatement = connection.prepareStatement("INSERT INTO Manageschedule\n" +
                    "SELECT ?, ?\n" +
                    "WHERE NOT EXISTS(SELECT * FROM Manageschedule WHERE Adminid=? AND Scheduleid=?);");
            preparedStatement.setInt(1, this.dbAdminID);
            preparedStatement.setInt(2, scheduleID);
            preparedStatement.setInt(3, this.dbAdminID);
            preparedStatement.setInt(4, scheduleID);

            preparedStatement.executeUpdate();

            // update Timeslot
            preparedStatement = connection.prepareStatement("UPDATE Timeslot\n" +
                                                                "SET ScheduleID=?,\n" +
                                                                    "\tStartTime=?,\n" +
                                                                    "\tEndTime=?\n" +
                                                                "WHERE slotid=?;");
            preparedStatement.setInt(1, scheduleID);
            preparedStatement.setTime(2, Time.valueOf(startTime + ":00"));
            preparedStatement.setTime(3, Time.valueOf(endTime + ":00"));
            preparedStatement.setInt(4, slotID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the reservation.");
            }else{
                System.out.println("Failed to update the reservation.");
            }

            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void deleteRoomBooking(){
        //tables needed: ManageSchedule, Schedule, Room, Timeslot
        Scanner scanner = new Scanner(System.in);

        int slotID;

        // USER PROMPTS
        //Step 1: List existing reservations where admin has access
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *\n" +
                    "FROM ((Manageschedule NATURAL INNER JOIN Schedule) NATURAL INNER JOIN Timeslot), Room\n" +
                    "WHERE Adminid=? \n" +
                    "\tAND Entitytype='Room' \n" +
                    "\tAND Schedule.entityid = Room.roomid;");
            preparedStatement.setInt(1, this.dbAdminID);

            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Existing reservations under this admin:");
            if (!resultSet.next()) {
                System.out.println("No existing reservations.");
                preparedStatement.close();
                return;
            }else {
                do {
                    System.out.print("ID=" + resultSet.getInt("SlotID") + "\t");
                    System.out.print(resultSet.getString("RoomName") + "\t");
                    System.out.print(resultSet.getString("DayOfWeek") + " ");
                    System.out.print(resultSet.getString("StartTime") + " - ");
                    System.out.println(resultSet.getString("EndTime"));
                } while (resultSet.next());
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("\nEnter the ID of the existing reservation:");
        slotID = scanner.nextInt();
        scanner.nextLine();

        // Step 2: delete the data
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Timeslot\n" +
                                                                                    "WHERE slotid=?;");
            preparedStatement.setInt(1, slotID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully deleted the reservation.");
            }else{
                System.out.println("Failed to delete the reservation.");
            }

            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // Equipment maintenance manager and action functions
    // - View, update, insert, delete equipments
    public void equipMaintenance(){
        Scanner scanner = new Scanner(System.in);
        int select;

        do {
            System.out.println("\nList of equipments:");
            // List equipments maintained - SELECT
            try {
                ResultSet resultSet;
                Statement statement = connection.createStatement();
                statement.executeQuery("SELECT * FROM Equipment;");
                resultSet = statement.getResultSet();

                while (resultSet.next()) {
                    System.out.print("ID=" + resultSet.getInt("EquipmentID"));
                    System.out.print("\tName:" + resultSet.getString("EquipmentName"));
                    System.out.print("\t\tPurchased on:" + resultSet.getDate("PurchaseDate"));
                    System.out.println("\t\tLast maintained:" + resultSet.getDate("LastMaintenanceDate"));
                }
                System.out.println();
            }catch (Exception e){
                System.out.println(e);
            }
            System.out.println("Equipment maintenance - select an option:");
            System.out.println("\t1: Update maintenance date");
            System.out.println("\t2: Insert new equipment entry");
            System.out.println("\t3: Delete equipment entry");
            System.out.println("\t4: Exit");

            select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    updateMtnceDate();
                    break;
                case 2:
                    insertEquip();
                    break;
                case 3:
                    deleteEquip();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while(select != 4);
    }

    public void updateMtnceDate(){
        //tables needed: Equipment, EquipManaged
        Scanner scanner = new Scanner(System.in);

        int equipID;
        String newDate;

        // Step 1: Select the ID of equipment
        System.out.print("Enter the ID of the equipment:");
        equipID = scanner.nextInt();
        scanner.nextLine();

        // Step 2: Get new date
        System.out.print("\nEnter the new maintenance date of the equipment, in format 'YYYY-MM-DD':");
        newDate = scanner.nextLine();

        // Step 3: prepare update queries and execute
        try {
            // Update action
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Equipment\n" +
                                                                        "SET LastMaintenanceDate=?\n" +
                                                                        "WHERE EquipmentID=?;");
            preparedStatement.setDate(1,Date.valueOf(newDate));
            preparedStatement.setInt(2, equipID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the equipment maintenance date.");
            }else {
                System.out.println("Failed to update the maintenance date.");
            }

            // Add admin to the equipment id if not already
            preparedStatement = connection.prepareStatement("INSERT INTO EquipManaged\n" +
                                                                    "SELECT ?, ?\n" +
                                        "WHERE NOT EXISTS(SELECT * FROM EquipManaged WHERE AdminID=? AND EquipID=?);");
            preparedStatement.setInt(1, this.dbAdminID);
            preparedStatement.setInt(2, equipID);
            preparedStatement.setInt(3, this.dbAdminID);
            preparedStatement.setInt(4, equipID);

            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void insertEquip(){
        Scanner scanner = new Scanner(System.in);

        String equipName;
        String purchaseDate;
        String lastMtceDate;

        // Step 1: Get inputs for name, purchase date, last maintenance date:
        System.out.print("Enter the equipment name:");
        equipName = scanner.nextLine();
        System.out.print("Enter the purchase date in format 'YYYY-MM-DD':");
        purchaseDate = scanner.nextLine();
        System.out.print("Enter the last mtce date in format 'YYYY-MM-DD':");
        lastMtceDate = scanner.nextLine();

        // Step 2: prepare insert statement
        try {
            // insert equipment
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Equipment(EquipmentName, PurchaseDate, LastMaintenanceDate)\n" +
                                                                                "VALUES (?, ?, ?);");
            preparedStatement.setString(1, equipName);
            preparedStatement.setDate(2, Date.valueOf(purchaseDate));
            preparedStatement.setDate(3, Date.valueOf(lastMtceDate));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully inserted the equipment entry.");
            }else{
                System.out.println("Failed to insert the equipment entry.");
            }

            // assign new equipment id to admin
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT max(equipmentid) FROM Equipment;");
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()){
                int newEquipID = resultSet.getInt(1);
                preparedStatement = connection.prepareStatement("INSERT INTO EquipManaged\n" +
                                                                    "SELECT ?, ?\n" +
                                                "WHERE NOT EXISTS(SELECT * FROM EquipManaged WHERE AdminID=? AND EquipID=?);");
                preparedStatement.setInt(1, this.dbAdminID);
                preparedStatement.setInt(2, newEquipID);
                preparedStatement.setInt(3, this.dbAdminID);
                preparedStatement.setInt(4, newEquipID);

                preparedStatement.executeUpdate();
            }else{
                System.out.println("Failed to assign new equipment to admin.");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void deleteEquip(){
        // tables needed: Equipment, ManageEquip
        Scanner scanner = new Scanner(System.in);

        int equipID;

        // Step 1: get user input
        System.out.print("Enter the ID of the equipment:");
        equipID = scanner.nextInt();

        // Step 2: prepare delete queries and execute accordingly
        try {
            // first delete from equipmanaged
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM EquipManaged WHERE equipid=?;");
            preparedStatement.setInt(1, equipID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to delete the equipment entry.");
                preparedStatement.close();
                return;
            }

            // delete from equipment
            preparedStatement = connection.prepareStatement("DELETE FROM Equipment WHERE equipmentid=?;");
            preparedStatement.setInt(1, equipID);

            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully deleted the equipment entry.");
            }else {
                System.out.println("Failed to delete the equipment entry.");
                preparedStatement.close();
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // Class scheduling management manager and actions
    // - Update a class to a different date and time (assumes the trainer is available)
    // - Update class status ('OPEN', 'FULL', 'FINISHED')
    public void updateClassSchedule(){
        // tables needed: schedule, timeslot, fitnessclass, user, room
        Scanner scanner = new Scanner(System.in);

        int select;

        do {
            //Step 1: list all available classes and details
            try {
                // get column names
                Statement statement = connection.createStatement();
                statement.executeQuery(
                        "SELECT ClassID, ClassName, Status, RoomName, FirstName, LastName, DayOfWeek, StartTime, EndTime\n" +
                                "FROM ((FitnessClass NATURAL INNER JOIN Room) JOIN Users on FitnessClass.trainerid = Users.userid), \n" +
                                "\t(Schedule NATURAL INNER JOIN Timeslot)\n" +
                                "WHERE Schedule.entitytype = 'Class'\n" +
                                "\tAND FitnessClass.classid = Schedule.entityid;");

                ResultSet resultSet = statement.getResultSet();
                ResultSetMetaData md = resultSet.getMetaData();

                System.out.println();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(md.getColumnName(i) + "\t\t");
                }
                System.out.println("\n");

                while (resultSet.next()) {
                    System.out.print(resultSet.getInt("ClassID") + "\t\t");
                    System.out.print(resultSet.getString("ClassName") + "\t\t");
                    System.out.print(resultSet.getString("Status") + "\t\t");
                    System.out.print(resultSet.getString("RoomName") + "\t\t");
                    System.out.print(resultSet.getString("FirstName") + "\t\t");
                    System.out.print(resultSet.getString("LastName") + "\t\t");
                    System.out.print(resultSet.getString("DayOfWeek") + "\t\t");
                    System.out.print(resultSet.getTime("StartTime") + "\t\t");
                    System.out.print(resultSet.getTime("EndTime") + "\n");
                }

            } catch (Exception e) {
                System.out.println(e);
            }

            //Step 2: get user input for class update
            System.out.println("\nUpdate class schedule - select an option:");
            System.out.println("\t1: Update class date");
            System.out.println("\t2: Update class status");
            System.out.println("\t3: Exit");

            select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    updateClassDate();
                    break;
                case 2:
                    updateClassStatus();
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (select != 3);
    }

    public void updateClassDate(){
        // tables needed: Schedule, timeslot
        Scanner scanner = new Scanner(System.in);

        int classID;
        int scheduleID;
        String dayOfWeek;
        String startTime;
        String endTime;

        // Step 1: get class ID and new date inputs
        System.out.print("Enter the ID of the class: ");
        classID = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the day of the week (eg. 'Monday'): ");
        dayOfWeek = scanner.nextLine();
        System.out.print("Enter the start time: ");
        startTime = scanner.nextLine();
        System.out.print("Enter the end time later than start time: ");
        endTime = scanner.nextLine();

        //Step 2: prepare queries and execute
        try {
            // Update day of week for the schedule
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Schedule\n" +
                            "SET DayOfWeek = ?::day_of_week\n" +
                            "WHERE EntityType = 'Class'\n" +
                            "\tAND EntityID = ?;");
            preparedStatement.setString(1,dayOfWeek);
            preparedStatement.setInt(2,classID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to update the class date entry.");
                preparedStatement.close();
                return;
            }

            // Get scheduleID for the updated schedule (ie. entityID)
            preparedStatement = connection.prepareStatement(
                    "SELECT ScheduleID\n" +
                            "FROM Schedule\n" +
                            "WHERE entitytype = 'Class'\n" +
                            "\tAND EntityID = ?;");
            preparedStatement.setInt(1,classID);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            scheduleID = resultSet.getInt(1);

            // update Timeslot
            preparedStatement = connection.prepareStatement(
                    "UPDATE Timeslot\n" +
                            "SET StartTime=?,\n" +
                            "\tEndTime=?\n" +
                            "WHERE ScheduleID=?;");
            preparedStatement.setTime(1, Time.valueOf(startTime + ":00"));
            preparedStatement.setTime(2, Time.valueOf(endTime + ":00"));
            preparedStatement.setInt(3, scheduleID);

            rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the class schedule.");
            }else{
                System.out.println("Failed to update the class schedule.");
            }

            // insert into Manageschedule (if not exists)
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO Manageschedule\n" +
                    "SELECT ?, ?\n" +
                    "WHERE NOT EXISTS(SELECT * FROM Manageschedule WHERE Adminid=? AND Scheduleid=?);");
            preparedStatement.setInt(1, this.dbAdminID);
            preparedStatement.setInt(2, scheduleID);
            preparedStatement.setInt(3, this.dbAdminID);
            preparedStatement.setInt(4, scheduleID);

            preparedStatement.executeUpdate();

            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateClassStatus(){
        Scanner scanner = new Scanner(System.in);

        int classID;
        String classStatus="";

        // Step 1: get class ID
        System.out.print("Enter the ID of the class: ");
        classID = scanner.nextInt();

        // Step 2: get input for class status ('OPEN', 'FULL', 'FINISHED')
        boolean isValid;
        do {
            System.out.println("Select the status of the class (type a number): ");
            System.out.println("\t1. Open");
            System.out.println("\t2. Full");
            System.out.println("\t3. Finished");
            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    classStatus = "OPEN";
                    isValid = true;
                    break;
                case 2:
                    classStatus = "FULL";
                    isValid = true;
                    break;
                case 3:
                    classStatus = "FINISHED";
                    isValid = true;
                    break;
                default:
                    System.out.println("Invalid option.\n");
                    isValid = false;
            }
        } while (!isValid);

        // Step 3: prepare queries and execute
        try {
            // tables needed: FitnessClass
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Fitnessclass\n" +
                    "SET Status = ?::class_status\n" +
                    "WHERE Classid = ?;");
            preparedStatement.setString(1, classStatus);
            preparedStatement.setInt(2, classID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the class status entry.");
            }else{
                System.out.println("Failed to update the class status entry.");
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // Billing management manager and action functions
    public void processBilling(){
        // Add billing, process payment
        Scanner scanner = new Scanner(System.in);
        int select;

        do {
            //Step 1: list all bills on file, assigned only to adminID
            try {
                // get column names
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT Billid, Firstname, Lastname, Billdate, Amount, Status\n" +
                                "FROM Bill JOIN Users on Bill.Memberid = Users.Userid\n" +
                                "WHERE Adminid = ?;");
                preparedStatement.setInt(1, this.dbAdminID);

                ResultSet resultSet = preparedStatement.executeQuery();
                ResultSetMetaData md = resultSet.getMetaData();

                System.out.println();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(md.getColumnName(i) + "\t\t");
                }
                System.out.println("\n");

                while (resultSet.next()) {
                    System.out.print(resultSet.getInt("Billid") + "\t\t\t");
                    System.out.print(resultSet.getString("Firstname") + "\t\t\t");
                    System.out.print(resultSet.getString("Lastname") + "\t\t\t");
                    System.out.print(resultSet.getDate("Billdate") + "\t\t");
                    System.out.print("$" + resultSet.getFloat("Amount") + "\t\t");
                    System.out.println(resultSet.getString("Status"));
                }System.out.println();

                preparedStatement.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            //Step 2: get user input for selection - add new bill, update bill amount, change bill status
            System.out.println("\nBilling and payment processing - select an option:");
            System.out.println("\t1: Add new bill");
            System.out.println("\t2: Update bill amount");
            System.out.println("\t3: Update bill status");
            System.out.println("\t4: Exit");

            select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    addBill();
                    break;
                case 2:
                    updateBillAmount();
                    break;
                case 3:
                    updateBillStatus();
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (select != 4);
    }

    public void addBill(){
        Scanner scanner = new Scanner(System.in);
        int memID;
        String billDate;
        int amount;

        //Step 2: list members names' and get input for a member id
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Memberid, Firstname, Lastname, Email\n" +
                    "FROM Members JOIN Users on Members.memberid = Users.userid;");

            System.out.println("Enter the ID of the member to generate new bill of: \n");
            while (resultSet.next()){
                System.out.print("ID=" + resultSet.getInt("Memberid") + "\t\t");
                System.out.print(resultSet.getString("Firstname") + " ");
                System.out.print(resultSet.getString("Lastname") + "\t\t");
                System.out.println(resultSet.getString("Email"));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        memID = scanner.nextInt();
        scanner.nextLine();

        //Step 3: User inputs for bill date and amount
        System.out.print("Enter the date for the bill, in format 'YYYY-MM-DD': ");
        billDate = scanner.nextLine();
        System.out.print("Enter the amount to be paid: ");
        amount = scanner.nextInt();
        scanner.nextLine();

        //Step 4: prepare insert queries and execute
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO Bill(MemberID, AdminID, BillDate, Amount, Status)\n" +
                            "VALUES (?,?,?,?,'PENDING');");
            preparedStatement.setInt(1, memID);
            preparedStatement.setInt(2, this.dbAdminID);
            preparedStatement.setDate(3, Date.valueOf(billDate));
            preparedStatement.setInt(4, amount);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Successfully added the bill entry.");
            }else{
                System.out.println("Failed to add the bill entry.");
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateBillAmount(){
        Scanner scanner = new Scanner(System.in);
        int billID;
        float newAmount;

        //Step 1: get bill id
        System.out.print("Enter the ID of the bill to update: ");
        billID = scanner.nextInt();
        scanner.nextLine();

        //Step 2: enter new amount
        System.out.print("Enter the amount to be paid (eg. 50.00): $");
        newAmount = scanner.nextFloat();
        scanner.nextLine();

        //Step 3: prepare statements and run queries
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bill SET Amount=? WHERE BillID=?;");
            preparedStatement.setFloat(1, newAmount);
            preparedStatement.setInt(2, billID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the bill entry.");
            }else{
                System.out.println("Failed to update the bill entry.");
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateBillStatus(){
        Scanner scanner = new Scanner(System.in);
        int billID;
        String newStatus = "";

        //Step 1: get bill id
        System.out.print("Enter the ID of the bill to update: ");
        billID = scanner.nextInt();
        scanner.nextLine();

        // Step 2: get input for bill status ('PENDING', 'PAID', 'CANCELED')
        boolean isValid;
        do {
            System.out.println("Select the status of the bill (type a number): ");
            System.out.println("\t1. Pending");
            System.out.println("\t2. Paid");
            System.out.println("\t3. Canceled");
            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1:
                    newStatus = "PENDING";
                    isValid = true;
                    break;
                case 2:
                    newStatus = "PAID";
                    isValid = true;
                    break;
                case 3:
                    newStatus = "CANCELED";
                    isValid = true;
                    break;
                default:
                    System.out.println("Invalid option.\n");
                    isValid = false;
            }
        } while (!isValid);

        //Step 3: prepare statements and run queries
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Bill SET Status=?::bill_status WHERE BillID=?;");
            preparedStatement.setString(1, newStatus);
            preparedStatement.setInt(2, billID);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully updated the bill entry.");
            }else{
                System.out.println("Failed to update the bill entry.");
            }
            preparedStatement.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}