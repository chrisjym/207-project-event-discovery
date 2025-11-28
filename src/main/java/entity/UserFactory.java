package entity;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    /**
     * Create a user without location.
     */
    public User create(String name, String password) {
        return new User(name, password);
    }

    /**
     * Create a user with location.
     */
    public User create(String name, String password, String address, Location location) {
        return new User(name, password, address, location);
    }
}