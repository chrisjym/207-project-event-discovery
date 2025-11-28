package data_access;

import entity.Location;
import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.update_location.UpdateLocationUserDataAccessInterface;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 * Now includes location storage for each user.
 *
 * CSV Format: username,password,address,latitude,longitude
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        UpdateLocationUserDataAccessInterface {

    private static final String HEADER = "username,password,address,latitude,longitude";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();
    private final UserFactory userFactory;

    private String currentUsername;

    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {
        this.userFactory = userFactory;
        csvFile = new File(csvPath);

        headers.put("username", 0);
        headers.put("password", 1);
        headers.put("address", 2);
        headers.put("latitude", 3);
        headers.put("longitude", 4);

        if (csvFile.length() == 0) {
            save();
        } else {
            loadFromFile();
        }
    }

    private void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String header = reader.readLine();

            // Handle old format (username,password) or new format (username,password,address,latitude,longitude)
            String row;
            while ((row = reader.readLine()) != null) {
                String[] col = row.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // Handle commas in addresses

                final String username = col[0].trim();
                final String password = col[1].trim();

                User user;

                // Check if we have location data (new format)
                if (col.length >= 5 && !col[2].trim().isEmpty()) {
                    String address = col[2].trim().replace("\"", ""); // Remove quotes
                    try {
                        double latitude = Double.parseDouble(col[3].trim());
                        double longitude = Double.parseDouble(col[4].trim());
                        Location location = new Location(address, latitude, longitude);
                        user = userFactory.create(username, password, address, location);
                    } catch (NumberFormatException e) {
                        // Invalid coordinates, create user without location
                        user = userFactory.create(username, password);
                    }
                } else {
                    // Old format or no location
                    user = userFactory.create(username, password);
                }

                accounts.put(username, user);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading users file: " + ex.getMessage(), ex);
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            writer.write(HEADER);
            writer.newLine();

            for (User user : accounts.values()) {
                StringBuilder line = new StringBuilder();
                line.append(user.getName()).append(",");
                line.append(user.getPassword()).append(",");

                if (user.hasLocation()) {
                    // Wrap address in quotes to handle commas
                    line.append("\"").append(user.getAddress()).append("\",");
                    line.append(user.getLocation().getLatitude()).append(",");
                    line.append(user.getLocation().getLongitude());
                } else {
                    line.append(",,");  // Empty location fields
                }

                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error saving users file: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        accounts.put(user.getName(), user);
        save();
    }

    /**
     * Update the user's location.
     */
    @Override
    public void updateUserLocation(String username, String address, Location location) {
        User existingUser = accounts.get(username);
        if (existingUser != null) {
            existingUser.setAddress(address);
            existingUser.setLocation(location);
            save();
        }
    }

    /**
     * Get the current user's location.
     */
    @Override
    public Location getCurrentUserLocation() {
        if (currentUsername == null) {
            return null;
        }
        User user = accounts.get(currentUsername);
        return user != null ? user.getLocation() : null;
    }

    /**
     * Get the current user's address string.
     */
    @Override
    public String getCurrentUserAddress() {
        if (currentUsername == null) {
            return "";
        }
        User user = accounts.get(currentUsername);
        return user != null ? user.getAddress() : "";
    }
}