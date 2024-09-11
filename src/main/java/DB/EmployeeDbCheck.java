package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeDbCheck {

    public static boolean isEmployeeInDatabase(int id) {
        String connectionString = "jdbc:postgresql://dpg-crgp14o8fa8c73aritj0-a.frankfurt-postgres.render.com/x_clients_db_75hr";
        String username = "x_clients_user";
        String password = "ypYaT7FBULZv2VxrJuOHVoe78MEElWlb";

        try (Connection connection = DriverManager.getConnection(connectionString, username, password)) {
            String query = "SELECT * FROM employee WHERE id = " + id;
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);

                return resultSet.next();  // Вернет true, если запись существует
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

