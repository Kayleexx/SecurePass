import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private final Map<String, String[]> database = new HashMap<>();

    public void saveCredentials(String website, String username, String encryptedPassword) {
        database.put(website, new String[]{username, encryptedPassword});
    }

    public String[] retrieveCredentials(String website) {
        return database.get(website);
    }
}
