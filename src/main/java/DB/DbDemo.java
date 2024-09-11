package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbDemo {
    public static void main(String[] args) throws SQLException {
        String connectionString = "jdbc:postgresql://dpg-crgp14o8fa8c73aritj0-a.frankfurt-postgres.render.com/x_clients_db_75hr";
        String username = "x_clients_user";
        String password = "ypYaT7FBULZv2VxrJuOHVoe78MEElWlb";
        Connection connection = DriverManager.getConnection(connectionString, username, password);

        String SELECT_ALL = "select id, first_name from employee e";
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL);

        boolean next = resultSet.next();
        System.out.println(next);

        int id = resultSet.getInt("id");
        String name = resultSet.getString("first_name");

        System.out.println(id + " " + name);

    }
}
