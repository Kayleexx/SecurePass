import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:securepass.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDB() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS credentials (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "website TEXT UNIQUE, " +
                    "username TEXT, " +
                    "password TEXT)";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
