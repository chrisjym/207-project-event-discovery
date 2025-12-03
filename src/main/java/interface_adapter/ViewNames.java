package interface_adapter;

/**
 * Constants for view names to avoid hardcoded strings.
 * Follows Single Responsibility Principle - centralized view name management.
 * Prevents violations of Clean Architecture by decoupling layers from specific view names.
 */
public class ViewNames {
    private ViewNames() {
        throw new AssertionError("Cannot instantiate ViewNames class");
    }

    public static final String SIGNUP = "sign up";
    public static final String LOGIN = "log in";
    public static final String LOGGED_IN = "logged in";
    public static final String DISPLAY_LOCAL_EVENTS = "display local events";
    public static final String CALENDAR = "calendar view";
    public static final String EVENT_LIST_BY_DATE = "event list by date";
    public static final String EVENT_DESCRIPTION = "event description";
    public static final String EVENT_SEARCH = "event search";
    public static final String SAVE_EVENTS = "save events";

}
