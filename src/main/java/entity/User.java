package entity;

/**
 * a simple entity representing a user.
 * users have a username, password, and optionally a saved location.
 */
public class User {

    private final String name;
    private final String password;
    private Location location;  // User's saved location for distance calculations
    private String address;     // Human-readable address string

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.location = null;
        this.address = "";
    }

    /**
     * Creates a new user with location information.
     * @param name the username
     * @param password the password
     * @param address the user's address string
     * @param location the user's location coordinates
     */
    public User(String name, String password, String address, Location location) {
        this(name, password);
        this.address = address != null ? address : "";
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address != null ? address : "";
    }

    /**
     * Check if user has a saved location.
     */
    public boolean hasLocation() {
        return location != null;
    }
}