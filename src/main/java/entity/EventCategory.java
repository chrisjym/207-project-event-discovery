package entity;

public enum EventCategory {
    MUSIC("Music"),

    SPORTS("Sports"),

    ARTS_THEATRE("Arts & Theatre"),

    FILM("Film"),

    MISCELLANEOUS("Miscellaneous");

    private final String displayName;
    EventCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public static EventCategory fromString(String eventCategoryName) {
        if (eventCategoryName == null) {
            return MISCELLANEOUS;
        }
        for (EventCategory eventCategory : EventCategory.values()) {
            if (eventCategory.displayName.equalsIgnoreCase(eventCategoryName)||
                    eventCategory.name().equalsIgnoreCase(eventCategoryName)) {
                return eventCategory;
            }
        }
        return MISCELLANEOUS;
    }
        @Override
        public String toString() {
        return displayName;
        }

}
