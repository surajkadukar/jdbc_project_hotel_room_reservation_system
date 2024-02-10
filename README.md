# jdbc_project_hotel_room_reservation_system
The name of the project could be "Hotel Nisarg Reservation System".


**Login System:**
Users are prompted to enter a username and password. In this code, the default username is "admin" and the password is "password".
If the correct username and password are entered, the user is logged in. Otherwise, they have three attempts to enter the correct credentials.


**Database Connection:**
Once logged in, the program connects to a MySQL database named "hotel_db" located at "jdbc:mysql://127.0.0.1:3306/hotel_db".
It establishes a connection using the provided username and password.


**Menu System:**
After successful login and database connection, the user is presented with a menu of options.
Options include reserving a room, viewing existing reservations, getting a room number for a reservation, updating reservations, deleting reservations, and logging out.


**Functions:**

**1. reserveRoom:** Allows users to reserve a room by providing guest name, room number, and contact number. It inserts this information into the database.

**2. viewReservations:** Displays all existing reservations stored in the database.

**3. getRoomNumber:** Retrieves the room number for a given reservation ID and guest name from the database.

**4. updateReservation:** Updates an existing reservation by modifying guest name, room number, and contact number.

**5. deleteReservation:** Deletes a reservation based on the reservation ID.

**6. reservationExists:** Checks if a reservation exists in the database based on the provided reservation ID.


**Logout:**
When the user chooses to log out, a logout message is displayed, and the program terminates.



**Summary of this project:**
Overall, this program provides a simple command-line interface for managing hotel reservations, including adding, viewing, updating, and deleting reservations. It interacts with a MySQL database to store and retrieve reservation information.
