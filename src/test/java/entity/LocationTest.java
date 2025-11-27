package entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create location with latitude and longitude only")
        void testConstructorWithCoordinatesOnly() {
            Location location = new Location(43.6629, -79.3957);

            assertEquals(43.6629, location.getLatitude(), 0.0001);
            assertEquals(-79.3957, location.getLongitude(), 0.0001);
            assertEquals("", location.getAddress());
        }

        @Test
        @DisplayName("Should create location with address and coordinates")
        void testConstructorWithAddressAndCoordinates() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals("123 Main St", location.getAddress());
            assertEquals(43.6629, location.getLatitude(), 0.0001);
            assertEquals(-79.3957, location.getLongitude(), 0.0001);
        }

        @Test
        @DisplayName("Should trim whitespace from address")
        void testConstructorTrimsAddress() {
            Location location = new Location("  123 Main St  ", 43.6629, -79.3957);

            assertEquals("123 Main St", location.getAddress());
        }

        @Test
        @DisplayName("Should handle null address by setting empty string")
        void testConstructorWithNullAddress() {
            Location location = new Location(null, 43.6629, -79.3957);

            assertEquals("", location.getAddress());
            assertNotNull(location.getAddress());
        }

        @Test
        @DisplayName("Should throw exception for latitude above 90")
        void testConstructorWithLatitudeAbove90() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Location(91.0, -79.3957);
            });

            assertTrue(exception.getMessage().contains("Latitude"));
            assertTrue(exception.getMessage().contains("90"));
        }

        @Test
        @DisplayName("Should throw exception for latitude below -90")
        void testConstructorWithLatitudeBelow90() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Location(-91.0, -79.3957);
            });

            assertTrue(exception.getMessage().contains("Latitude"));
            assertTrue(exception.getMessage().contains("-90"));
        }

        @Test
        @DisplayName("Should throw exception for longitude above 180")
        void testConstructorWithLongitudeAbove180() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Location(43.6629, 181.0);
            });

            assertTrue(exception.getMessage().contains("Longitude"));
            assertTrue(exception.getMessage().contains("180"));
        }

        @Test
        @DisplayName("Should throw exception for longitude below -180")
        void testConstructorWithLongitudeBelow180() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Location(43.6629, -181.0);
            });

            assertTrue(exception.getMessage().contains("Longitude"));
            assertTrue(exception.getMessage().contains("-180"));
        }

        @Test
        @DisplayName("Should accept boundary latitude values")
        void testConstructorWithBoundaryLatitudes() {
            Location location1 = new Location(90.0, 0.0);
            Location location2 = new Location(-90.0, 0.0);

            assertEquals(90.0, location1.getLatitude(), 0.0001);
            assertEquals(-90.0, location2.getLatitude(), 0.0001);
        }

        @Test
        @DisplayName("Should accept boundary longitude values")
        void testConstructorWithBoundaryLongitudes() {
            Location location1 = new Location(0.0, 180.0);
            Location location2 = new Location(0.0, -180.0);

            assertEquals(180.0, location1.getLongitude(), 0.0001);
            assertEquals(-180.0, location2.getLongitude(), 0.0001);
        }
    }

    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceCalculationTests {

        @Test
        @DisplayName("Should calculate distance between two locations")
        void testCalculateDistanceBetweenTwoLocations() {
            // Toronto to Mississauga (approximate)
            Location toronto = new Location(43.6532, -79.3832);
            Location mississauga = new Location(43.5890, -79.6441);

            double distance = toronto.calculateDistance(mississauga);

            // Distance should be approximately 23-24 km
            assertTrue(distance > 22.0 && distance < 25.0,
                    "Distance should be approximately 23-24 km, was: " + distance);
        }

        @Test
        @DisplayName("Should calculate zero distance for same location")
        void testCalculateDistanceSameLocation() {
            Location location = new Location(43.6532, -79.3832);

            double distance = location.calculateDistance(location);

            assertEquals(0.0, distance, 0.001);
        }

        @Test
        @DisplayName("Should calculate distance with very close coordinates")
        void testCalculateDistanceVeryClose() {
            Location loc1 = new Location(43.6532, -79.3832);
            Location loc2 = new Location(43.6533, -79.3833);

            double distance = loc1.calculateDistance(loc2);

            // Should be very small (less than 0.2 km)
            assertTrue(distance < 0.2,
                    "Distance should be very small, was: " + distance);
        }

        @Test
        @DisplayName("Should return MAX_VALUE when calculating distance to null location")
        void testCalculateDistanceToNull() {
            Location location = new Location(43.6532, -79.3832);

            double distance = location.calculateDistance(null);

            assertEquals(Double.MAX_VALUE, distance);
        }

        @Test
        @DisplayName("Should calculate long distance correctly (Toronto to Vancouver)")
        void testCalculateLongDistance() {
            Location toronto = new Location(43.6532, -79.3832);
            Location vancouver = new Location(49.2827, -123.1207);

            double distance = toronto.calculateDistance(vancouver);

            // Distance should be approximately 3350-3400 km
            assertTrue(distance > 3300 && distance < 3450,
                    "Distance Toronto-Vancouver should be ~3350-3400 km, was: " + distance);
        }

        @Test
        @DisplayName("Should calculate distance across the equator")
        void testCalculateDistanceAcrossEquator() {
            Location northernHemisphere = new Location(10.0, 0.0);
            Location southernHemisphere = new Location(-10.0, 0.0);

            double distance = northernHemisphere.calculateDistance(southernHemisphere);

            // Distance should be approximately 2220 km (20 degrees of latitude)
            assertTrue(distance > 2200 && distance < 2250,
                    "Distance should be approximately 2220 km, was: " + distance);
        }

        @Test
        @DisplayName("Should calculate distance at the poles")
        void testCalculateDistanceAtPoles() {
            Location northPole = new Location(90.0, 0.0);
            Location southPole = new Location(-90.0, 0.0);

            double distance = northPole.calculateDistance(southPole);

            // Distance should be approximately half Earth's circumference (~20,000 km)
            assertTrue(distance > 19900 && distance < 20100,
                    "Distance pole to pole should be ~20,000 km, was: " + distance);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should be equal to itself")
        void testEqualsItself() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals(location, location);
        }

        @Test
        @DisplayName("Should be equal to location with same values")
        void testEqualsSameValues() {
            Location location1 = new Location("123 Main St", 43.6629, -79.3957);
            Location location2 = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals(location1, location2);
            assertEquals(location2, location1);
        }

        @Test
        @DisplayName("Should not be equal to location with different address")
        void testNotEqualsDifferentAddress() {
            Location location1 = new Location("123 Main St", 43.6629, -79.3957);
            Location location2 = new Location("456 Oak Ave", 43.6629, -79.3957);

            assertNotEquals(location1, location2);
        }

        @Test
        @DisplayName("Should not be equal to location with different latitude")
        void testNotEqualsDifferentLatitude() {
            Location location1 = new Location("123 Main St", 43.6629, -79.3957);
            Location location2 = new Location("123 Main St", 43.7629, -79.3957);

            assertNotEquals(location1, location2);
        }

        @Test
        @DisplayName("Should not be equal to location with different longitude")
        void testNotEqualsDifferentLongitude() {
            Location location1 = new Location("123 Main St", 43.6629, -79.3957);
            Location location2 = new Location("123 Main St", 43.6629, -79.4957);

            assertNotEquals(location1, location2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void testNotEqualsNull() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertNotEquals(null, location);
        }

        @Test
        @DisplayName("Should not be equal to different class object")
        void testNotEqualsDifferentClass() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);
            String notALocation = "Not a location";

            assertNotEquals(location, notALocation);
        }

        @Test
        @DisplayName("Should have same hashCode for equal locations")
        void testHashCodeSameForEqualLocations() {
            Location location1 = new Location("123 Main St", 43.6629, -79.3957);
            Location location2 = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals(location1.hashCode(), location2.hashCode());
        }

        @Test
        @DisplayName("Should have consistent hashCode")
        void testHashCodeConsistent() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            int hashCode1 = location.hashCode();
            int hashCode2 = location.hashCode();

            assertEquals(hashCode1, hashCode2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain address in toString")
        void testToStringContainsAddress() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            String result = location.toString();

            assertTrue(result.contains("123 Main St"));
        }

        @Test
        @DisplayName("Should contain latitude in toString")
        void testToStringContainsLatitude() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            String result = location.toString();

            assertTrue(result.contains("43.6"));
        }

        @Test
        @DisplayName("Should contain longitude in toString")
        void testToStringContainsLongitude() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            String result = location.toString();

            assertTrue(result.contains("-79.3"));
        }

        @Test
        @DisplayName("Should have proper format")
        void testToStringFormat() {
            Location location = new Location("Test Address", 43.6629, -79.3957);

            String result = location.toString();

            assertTrue(result.startsWith("Location{"));
            assertTrue(result.contains("address="));
            assertTrue(result.contains("latitude="));
            assertTrue(result.contains("longitude="));
            assertTrue(result.endsWith("}"));
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should return correct address")
        void testGetAddress() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals("123 Main St", location.getAddress());
        }

        @Test
        @DisplayName("Should return correct latitude")
        void testGetLatitude() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals(43.6629, location.getLatitude(), 0.0001);
        }

        @Test
        @DisplayName("Should return correct longitude")
        void testGetLongitude() {
            Location location = new Location("123 Main St", 43.6629, -79.3957);

            assertEquals(-79.3957, location.getLongitude(), 0.0001);
        }
    }
}