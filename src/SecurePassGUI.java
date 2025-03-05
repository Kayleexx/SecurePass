import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SecurePassGUI {
    private JFrame frame;
    private JTextField websiteField, usernameField;
    private JPasswordField passwordField;

    public SecurePassGUI() {
        // Create main frame
        frame = new JFrame("SecurePass - Password Manager");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // Padding

        // Labels & Input Fields
        JLabel websiteLabel = new JLabel("Website:");
        websiteField = new JTextField(20);

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);

        // Buttons
        JButton generateButton = new JButton("Generate Password");
        JButton saveButton = new JButton("Save");
        JButton retrieveButton = new JButton("Retrieve");

        // Styling Buttons
        generateButton.setBackground(new Color(70, 130, 180));
        generateButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        retrieveButton.setBackground(new Color(255, 140, 0));
        retrieveButton.setForeground(Color.WHITE);

        // Action Listeners
        generateButton.addActionListener(e -> passwordField.setText(PasswordGenerator.generatePassword()));
        saveButton.addActionListener(e -> savePassword());
        retrieveButton.addActionListener(e -> retrievePassword());

        // Layout Configuration
        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(websiteLabel, gbc);
        gbc.gridx = 1;
        frame.add(websiteField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(usernameLabel, gbc);
        gbc.gridx = 1;
        frame.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        frame.add(passwordLabel, gbc);
        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        frame.add(generateButton, gbc);

        gbc.gridy = 4;
        frame.add(saveButton, gbc);

        gbc.gridy = 5;
        frame.add(retrieveButton, gbc);

        frame.setVisible(true);
    }

    private void savePassword() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO credentials (website, username, password) VALUES (?, ?, ?)")) {
            pstmt.setString(1, websiteField.getText());
            pstmt.setString(2, usernameField.getText());
            pstmt.setString(3, EncryptionUtil.encrypt(new String(passwordField.getPassword())));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Password Saved!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error Saving Password");
        }
    }

    private void retrievePassword() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, password FROM credentials WHERE website = ?")) {
            pstmt.setString(1, websiteField.getText());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                passwordField.setText(EncryptionUtil.decrypt(rs.getString("password")));
                JOptionPane.showMessageDialog(frame, "Credentials Retrieved!");
            } else {
                JOptionPane.showMessageDialog(frame, "No password found for this website");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error Retrieving Password");
        }
    }

    public static void main(String[] args) {
        DatabaseManager.initializeDB();
        new SecurePassGUI();
    }
}
