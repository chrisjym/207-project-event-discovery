package entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Location defaultLocation;
    private LocalDateTime defaultDateTime;

    @BeforeEach
    void setUp() {
        defaultLocation = new Location(43.6629, -79.3957);
        defaultDateTime = LocalDateTime.of(2025, 11, 20, 19, 0);
    }

    @Nested
    @DisplayName("Main Constructor Tests")
    class MainConstructorTests {

        @Test
        @DisplayName("Should create event with all valid parameters")
        void testConstructorWithValidParameters() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Test Description",
                    "123 Main St",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            assertEquals("1", event.getId());
            assertEquals("Test Event", event.getName());
            assertEquals("Test Description", event.getDescription());
            assertEquals("123 Main St", event.getAddress());
            assertEquals(EventCategory.MUSIC, event.getCategory());
            assertEquals(defaultLocation, event.getLocation());
            assertEquals(defaultDateTime, event.getStartTime());
            assertEquals("https://example.com/image.jpg", event.getImageUrl());
        }

        @Test
        @DisplayName("Should set empty description for null description")
        void testConstructorWithNullDescription() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    null,
                    "123 Main St",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            assertEquals("", event.getDescription());
        }

        @Test
        @DisplayName("Should set MISCELLANEOUS category for null category")
        void testConstructorWithNullCategory() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    null,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            assertEquals(EventCategory.MISCELLANEOUS, event.getCategory());
        }

        @Test
        @DisplayName("Should set empty image URL for null image URL")
        void testConstructorWithNullImageUrl() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    null
            );

            // Should return placeholder
            assertTrue(event.getImageUrl().contains("placeholder"));
        }

        @Test
        @DisplayName("Should set empty image URL for empty string image URL")
        void testConstructorWithEmptyImageUrl() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    ""
            );

            // Should return placeholder
            assertTrue(event.getImageUrl().contains("placeholder"));
        }

        @Test
        @DisplayName("Should throw exception for null ID")
        void testConstructorWithNullId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        null,
                        "Test Event",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event ID"));
        }

        @Test
        @DisplayName("Should throw exception for empty ID")
        void testConstructorWithEmptyId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "",
                        "Test Event",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event ID"));
        }

        @Test
        @DisplayName("Should throw exception for whitespace-only ID")
        void testConstructorWithWhitespaceId() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "   ",
                        "Test Event",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event ID"));
        }

        @Test
        @DisplayName("Should throw exception for null name")
        void testConstructorWithNullName() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        null,
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event name"));
        }

        @Test
        @DisplayName("Should throw exception for empty name")
        void testConstructorWithEmptyName() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        "",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event name"));
        }

        @Test
        @DisplayName("Should throw exception for null address")
        void testConstructorWithNullAddress() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        "Test Event",
                        "Description",
                        null,
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event address"));
        }

        @Test
        @DisplayName("Should throw exception for empty address")
        void testConstructorWithEmptyAddress() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        "Test Event",
                        "Description",
                        "",
                        EventCategory.MUSIC,
                        defaultLocation,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Event address"));
        }

        @Test
        @DisplayName("Should throw exception for null location")
        void testConstructorWithNullLocation() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        "Test Event",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        null,
                        defaultDateTime,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Location"));
        }

        @Test
        @DisplayName("Should throw exception for null start time")
        void testConstructorWithNullStartTime() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Event(
                        "1",
                        "Test Event",
                        "Description",
                        "123 Main St",
                        EventCategory.MUSIC,
                        defaultLocation,
                        null,
                        "https://example.com/image.jpg"
                );
            });

            assertTrue(exception.getMessage().contains("Start time"));
        }
    }

    @Nested
    @DisplayName("Convenience Constructor Tests")
    class ConvenienceConstructorTests {

        @Test
        @DisplayName("Should create event with string category and datetime")
        void testConvenienceConstructor() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "Music",
                    "2025-11-20T19:00"
            );

            assertEquals("1", event.getId());
            assertEquals("Test Event", event.getName());
            assertEquals(EventCategory.MUSIC, event.getCategory());
            assertEquals(2025, event.getStartTime().getYear());
            assertEquals(11, event.getStartTime().getMonthValue());
            assertEquals(20, event.getStartTime().getDayOfMonth());
            assertEquals(19, event.getStartTime().getHour());
            assertEquals(0, event.getStartTime().getMinute());
        }

        @Test
        @DisplayName("Should parse category case-insensitively")
        void testConvenienceConstructorCaseInsensitiveCategory() {
            Event event1 = new Event("1", "Test", "Desc", "Addr", defaultLocation, "music", "2025-11-20T19:00");
            Event event2 = new Event("2", "Test", "Desc", "Addr", defaultLocation, "MUSIC", "2025-11-20T19:00");
            Event event3 = new Event("3", "Test", "Desc", "Addr", defaultLocation, "MuSiC", "2025-11-20T19:00");

            assertEquals(EventCategory.MUSIC, event1.getCategory());
            assertEquals(EventCategory.MUSIC, event2.getCategory());
            assertEquals(EventCategory.MUSIC, event3.getCategory());
        }

        @Test
        @DisplayName("Should default to MISCELLANEOUS for invalid category")
        void testConvenienceConstructorInvalidCategory() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "InvalidCategory",
                    "2025-11-20T19:00"
            );

            assertEquals(EventCategory.MISCELLANEOUS, event.getCategory());
        }

        @Test
        @DisplayName("Should default to MISCELLANEOUS for null category")
        void testConvenienceConstructorNullCategory() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    null,
                    "2025-11-20T19:00"
            );

            assertEquals(EventCategory.MISCELLANEOUS, event.getCategory());
        }

        @Test
        @DisplayName("Should default to MISCELLANEOUS for empty category")
        void testConvenienceConstructorEmptyCategory() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "",
                    "2025-11-20T19:00"
            );

            assertEquals(EventCategory.MISCELLANEOUS, event.getCategory());
        }

        @Test
        @DisplayName("Should default to current time for null datetime")
        void testConvenienceConstructorNullDateTime() {
            LocalDateTime before = LocalDateTime.now();

            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "Music",
                    null
            );

            LocalDateTime after = LocalDateTime.now();

            assertFalse(event.getStartTime().isBefore(before));
            assertFalse(event.getStartTime().isAfter(after));
        }

        @Test
        @DisplayName("Should default to current time for empty datetime")
        void testConvenienceConstructorEmptyDateTime() {
            LocalDateTime before = LocalDateTime.now();

            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "Music",
                    ""
            );

            LocalDateTime after = LocalDateTime.now();

            assertFalse(event.getStartTime().isBefore(before));
            assertFalse(event.getStartTime().isAfter(after));
        }

        @Test
        @DisplayName("Should default to current time for invalid datetime format")
        void testConvenienceConstructorInvalidDateTime() {
            LocalDateTime before = LocalDateTime.now();

            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "Music",
                    "invalid-date-format"
            );

            LocalDateTime after = LocalDateTime.now();

            assertFalse(event.getStartTime().isBefore(before));
            assertFalse(event.getStartTime().isAfter(after));
        }

        @Test
        @DisplayName("Should have empty image URL with convenience constructor")
        void testConvenienceConstructorImageUrl() {
            Event event = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    defaultLocation,
                    "Music",
                    "2025-11-20T19:00"
            );

            // Should return placeholder since no image URL was provided
            assertTrue(event.getImageUrl().contains("placeholder"));
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        private Event testEvent;

        @BeforeEach
        void setUp() {
            testEvent = new Event(
                    "test-id",
                    "Test Event Name",
                    "Test Description",
                    "456 Test Street",
                    EventCategory.SPORTS,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/test.jpg"
            );
        }

        @Test
        @DisplayName("Should return correct ID")
        void testGetId() {
            assertEquals("test-id", testEvent.getId());
        }

        @Test
        @DisplayName("Should return correct name")
        void testGetName() {
            assertEquals("Test Event Name", testEvent.getName());
        }

        @Test
        @DisplayName("Should return correct description")
        void testGetDescription() {
            assertEquals("Test Description", testEvent.getDescription());
        }

        @Test
        @DisplayName("Should return correct address")
        void testGetAddress() {
            assertEquals("456 Test Street", testEvent.getAddress());
        }

        @Test
        @DisplayName("Should return correct category")
        void testGetCategory() {
            assertEquals(EventCategory.SPORTS, testEvent.getCategory());
        }

        @Test
        @DisplayName("Should return correct location")
        void testGetLocation() {
            assertEquals(defaultLocation, testEvent.getLocation());
        }

        @Test
        @DisplayName("Should return correct start time")
        void testGetStartTime() {
            assertEquals(defaultDateTime, testEvent.getStartTime());
        }

        @Test
        @DisplayName("Should return correct image URL when provided")
        void testGetImageUrlWhenProvided() {
            assertEquals("https://example.com/test.jpg", testEvent.getImageUrl());
        }

        @Test
        @DisplayName("Should return placeholder when image URL is empty")
        void testGetImageUrlPlaceholder() {
            Event eventWithoutImage = new Event(
                    "1",
                    "Test",
                    "Desc",
                    "Addr",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    ""
            );

            String imageUrl = eventWithoutImage.getImageUrl();

            assertTrue(imageUrl.contains("placeholder"));
            assertTrue(imageUrl.contains("No+Event+Image"));
        }
    }

    @Nested
    @DisplayName("Business Method Tests")
    class BusinessMethodTests {

        private Event testEvent;
        private Location eventLocation;

        @BeforeEach
        void setUp() {
            eventLocation = new Location(43.6532, -79.3832);  // Toronto
            testEvent = new Event(
                    "1",
                    "Test Event",
                    "Description",
                    "123 Main St",
                    EventCategory.MUSIC,
                    eventLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );
        }

        @Test
        @DisplayName("Should return true when location is within radius")
        void testIsWithinRadiusTrue() {
            Location userLocation = new Location(43.6629, -79.3957);  // ~1.5 km away

            assertTrue(testEvent.isWithinRadius(userLocation, 2.0));
        }

        @Test
        @DisplayName("Should return false when location is outside radius")
        void testIsWithinRadiusFalse() {
            Location userLocation = new Location(43.6629, -79.3957);  // ~1.5 km away

            assertFalse(testEvent.isWithinRadius(userLocation, 1.0));
        }

        @Test
        @DisplayName("Should return false for null user location")
        void testIsWithinRadiusNullLocation() {
            assertFalse(testEvent.isWithinRadius(null, 5.0));
        }

        @Test
        @DisplayName("Should return false for negative radius")
        void testIsWithinRadiusNegativeRadius() {
            Location userLocation = new Location(43.6629, -79.3957);

            assertFalse(testEvent.isWithinRadius(userLocation, -1.0));
        }

        @Test
        @DisplayName("Should return true when exactly at radius boundary")
        void testIsWithinRadiusExactBoundary() {
            Location userLocation = new Location(43.6629, -79.3957);
            double exactDistance = testEvent.calculateDistanceTo(userLocation);

            assertTrue(testEvent.isWithinRadius(userLocation, exactDistance));
        }

        @Test
        @DisplayName("Should calculate correct distance to user location")
        void testCalculateDistanceTo() {
            Location userLocation = new Location(43.6629, -79.3957);

            double distance = testEvent.calculateDistanceTo(userLocation);

            assertTrue(distance > 0);
            assertTrue(distance < 3.0);  // Should be less than 3 km
        }

        @Test
        @DisplayName("Should return MAX_VALUE for null user location in distance calculation")
        void testCalculateDistanceToNull() {
            double distance = testEvent.calculateDistanceTo(null);

            assertEquals(Double.MAX_VALUE, distance);
        }

        @Test
        @DisplayName("Should return true for matching category")
        void testIsInCategoryTrue() {
            assertTrue(testEvent.isInCategory(EventCategory.MUSIC));
        }

        @Test
        @DisplayName("Should return false for non-matching category")
        void testIsInCategoryFalse() {
            assertFalse(testEvent.isInCategory(EventCategory.SPORTS));
        }

        @Test
        @DisplayName("Should handle null category comparison")
        void testIsInCategoryNull() {
            assertFalse(testEvent.isInCategory(null));
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        private Event event1;
        private Event event2;

        @BeforeEach
        void setUp() {
            event1 = new Event(
                    "same-id",
                    "Event Name",
                    "Description",
                    "Address",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            event2 = new Event(
                    "same-id",
                    "Different Name",
                    "Different Description",
                    "Different Address",
                    EventCategory.SPORTS,
                    new Location(0, 0),
                    LocalDateTime.now(),
                    "https://different.com/image.jpg"
            );
        }

        @Test
        @DisplayName("Should be equal to itself")
        void testEqualsItself() {
            assertEquals(event1, event1);
        }

        @Test
        @DisplayName("Should be equal to event with same ID")
        void testEqualsSameId() {
            assertEquals(event1, event2);
        }

        @Test
        @DisplayName("Should not be equal to event with different ID")
        void testNotEqualsDifferentId() {
            Event differentEvent = new Event(
                    "different-id",
                    "Event Name",
                    "Description",
                    "Address",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            assertNotEquals(event1, differentEvent);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void testNotEqualsNull() {
            assertNotEquals(null, event1);
        }

        @Test
        @DisplayName("Should not be equal to different class object")
        void testNotEqualsDifferentClass() {
            assertNotEquals(event1, "Not an Event");
        }

        @Test
        @DisplayName("Should have same hashCode for events with same ID")
        void testHashCodeSameForSameId() {
            assertEquals(event1.hashCode(), event2.hashCode());
        }

        @Test
        @DisplayName("Should have different hashCode for events with different ID")
        void testHashCodeDifferentForDifferentId() {
            Event differentEvent = new Event(
                    "different-id",
                    "Event Name",
                    "Description",
                    "Address",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );

            assertNotEquals(event1.hashCode(), differentEvent.hashCode());
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void testHashCodeConsistent() {
            int hash1 = event1.hashCode();
            int hash2 = event1.hashCode();

            assertEquals(hash1, hash2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        private Event testEvent;

        @BeforeEach
        void setUp() {
            testEvent = new Event(
                    "test-id",
                    "Test Event",
                    "Description",
                    "Address",
                    EventCategory.MUSIC,
                    defaultLocation,
                    defaultDateTime,
                    "https://example.com/image.jpg"
            );
        }

        @Test
        @DisplayName("Should contain ID in toString")
        void testToStringContainsId() {
            String result = testEvent.toString();

            assertTrue(result.contains("test-id"));
        }

        @Test
        @DisplayName("Should contain name in toString")
        void testToStringContainsName() {
            String result = testEvent.toString();

            assertTrue(result.contains("Test Event"));
        }

        @Test
        @DisplayName("Should contain category in toString")
        void testToStringContainsCategory() {
            String result = testEvent.toString();

            assertTrue(result.contains("MUSIC") || result.contains("Music"));
        }

        @Test
        @DisplayName("Should contain image URL in toString")
        void testToStringContainsImageUrl() {
            String result = testEvent.toString();

            assertTrue(result.contains("example.com"));
        }

        @Test
        @DisplayName("Should have proper format")
        void testToStringFormat() {
            String result = testEvent.toString();

            assertTrue(result.startsWith("Event{"));
            assertTrue(result.contains("id="));
            assertTrue(result.contains("name="));
            assertTrue(result.contains("category="));
            assertTrue(result.contains("image="));
            assertTrue(result.endsWith("}"));
        }
    }
}