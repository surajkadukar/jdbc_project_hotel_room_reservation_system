import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "root";

    public static void main(String[] args) throws ClassNotFoundException {

        Scanner scanner = new Scanner(System.in);
        String userId = "admin";
        String pass = "password";
        int attempts = 3;
        boolean loggedIn = false;
        do {
            System.out.println("-------- Login -------");
            System.out.print("Enter username: ");
            String enteredUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String enteredPassword = scanner.nextLine();
            if (enteredUsername.equals(userId) && enteredPassword.equals(pass)) {
                System.out.println("Login successful!");
                loggedIn = true;

                // Load All the Drivers
                try {
                    // Load all the drivers for jdbc
                    // package: com.mysql.cj in this package all the JDBC drivers are present
                    Class.forName("com.mysql.cj.jdbc.Driver");

                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }

                // Conncection Establish
                try {
                    Connection connection = DriverManager.getConnection(url, username, password);
                    while (true) {
                        System.out.println();
                        System.out.println("==================== HOTEL NISARG ====================");
                        Scanner sc = new Scanner(System.in);
                        System.out.print("Loading");
                        int loading = 8;
                        while (loading != 0) {
                            System.out.print(".");
                            Thread.sleep(400);
                            loading--;
                        }
                        System.out.print("\r"); // Move the cursor to the beginning of the line
                        System.out.print("                           "); // Overwrite the previous output with spaces
                        System.out.print("\r"); // Move the cursor to the beginning of the line again
                        System.out.println();
                        System.out.println(" 1. Reserve a Room.");
                        System.out.println(" 2. View Reservations.");
                        System.out.println(" 3. Get Room Number.");
                        System.out.println(" 4. Update Reservations");
                        System.out.println(" 5. Delete Reservations");
                        System.out.println(" 0. Logout");
                        System.out.println("Choose one option: ");

                        int choice = sc.nextInt();
                        switch (choice) {
                            case 1:
                                reserveRoom(connection, sc);
                                break;

                            case 2:
                                viewReservations(connection);
                                break;

                            case 3:
                                getRoomNumber(connection, sc);
                                break;

                            case 4:
                                updateReservation(connection, sc);
                                break;

                            case 5:
                                deleteReservation(connection, sc);
                                break;

                            case 0:
                                exit();
                                sc.close();
                                return;
                            default:
                                System.out.println("Invalid Choice. Try Again!!");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("Incorrect username or password. " + attempts + " attempts remaining.");
                } else {
                    System.out.println("Too many attempts. Please try again later.");
                }
            }
        } while (attempts > 0);

        scanner.close();
        scanner.close();
    }

    public static void reserveRoom(Connection connection, Scanner sc) {
        try {
            System.out.println(" Enter Guest Name: ");
            String guestName = sc.next();
            sc.nextLine();

            System.out.println("Enter Room Number: ");
            int roomNumber = sc.nextInt();

            System.out.println("Enter Contact Number: ");
            Long contactNumber = sc.nextLong();

            String sql = "INSERT INTO RESERVATIONS (GUEST_NAME, ROOM_NUMBER, CONTACT_NUMBER) VALUES('" + guestName
                    + "', " + roomNumber + ", '" + contactNumber + "')";

            // Statement interface : It helps us to run sql queries in java
            /*
             * Statement interface method : .executeUpdate() - It is used to perform INSERT,
             * UPDATE, DELETE operations. It displays how many rows affected/ created, it
             * returns int
             */

            /*
             * Statement interface method : .executeQuery() - It is used to perform retrive
             * from the table operations
             */
            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Successful!");
                } else {
                    System.out.println("Reservation Failed!");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewReservations(Connection connection) throws SQLException {
        String sql = "SELECT *FROM RESERVATIONS;";

        try (Statement statement = connection.createStatement();
                /*
                 * ResultSet it is an interface
                 * Statement interface method : .executeQuery() - It is used to perform retrive
                 * from the table operations
                 */
                ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Resetvations: ");
            System.out.println(
                    " +----------------+------------------------+-------------+----------------+----------------------+");
            System.out.println(
                    " | RESERVATION ID |       GUEST NAME       | ROOM NUMBER | CONTACT NUMBER |   RESERVATION DATE   |");
            System.out.println(
                    " +----------------+------------------------+-------------+----------------+----------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("RESERVATION_ID");
                String guestName = resultSet.getString("GUEST_NAME");
                int roomNumber = resultSet.getInt("ROOM_NUMBER");
                Long contactNumber = resultSet.getLong("CONTACT_NUMBER");
                String reservationDate = resultSet.getString("RESERVATION_DATE").toString();

                // format and display the reservation details in table format
                System.out.printf(" | %-14d | %-18s     | %-12d| %-15s| %-10s  | \n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);

                System.out.println(
                        " +----------------+------------------------+-------------+----------------+----------------------+");

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getRoomNumber(Connection connection, Scanner sc) {
        try {
            System.out.println(" Enter Reservation Id: ");
            int reservationId = sc.nextInt();

            System.out.println("Enter Guest Name: ");
            String guestName = sc.next();

            String sql = "SELECT ROOM_NUMBER FROM RESERVATIONS " +
                    "WHERE RESERVATION_ID = " + reservationId + " AND GUEST_NAME = '" + guestName + "'";

            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("ROOM_NUMBER");
                    System.out.println();
                    System.out.println("Room Number for RESERVATION_ID " + reservationId + " and GUEST " + guestName
                            + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation details not found for this Reservation ID amd Room Number!");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Connection connection, Scanner sc) {
        try {
            System.out.println(" Enter Reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID...");
            }

            System.out.println(" Enter new Guest Name: ");
            String newGuestName = sc.nextLine();

            System.out.println(" Enter new Room Number: ");
            int newRoomNumber = sc.nextInt();

            System.out.println(" Enter new Contact Number: ");
            long newContactNumber = sc.nextLong();

            String sql = "UPDATE RESERVATIONS SET GUEST_NAME = '" + newGuestName + "', " + "ROOM_NUMBER = "
                    + newRoomNumber + ", " + "CONTACT_NUMBER= '" + newContactNumber + "' " + "WHERE RESERVATION_ID = "
                    + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Update Successfully!");
                } else {
                    System.out.println("Reservation Update Failed!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter Resrvation ID to Delete: ");
            int reservationId = sc.nextInt();

            if (!reservationExists(connection, reservationId)) {
                System.out.println("Reservation not found for the given ID...");
            }

            String sql = "DELETE FROM RESERVATIONS WHERE RESERVATION_ID= " + reservationId;

            try (Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation Update Successfully!");
                } else {
                    System.out.println("Reservation Update Failed!");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection connection, int reservationId) {
        try {
            String sql = "SELECT RESERVATION_ID FROM RESERVATIONS WHERE RESERVATION_ID= " + reservationId;

            try (Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql)) {
                return resultSet.next();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Logout!");
        int loading = 5;
        while (loading != 0) {
            System.out.print(".");
            Thread.sleep(400);
            loading--;
        }
        System.out.println();
        System.out.println("Thank You, For using Hotel Nisarg Reservation System..!");
    }

}
