package use_case.calendarFlow;

import java.time.LocalDate;

import entity.Location;

/**
 * Input Data for the Calendar Flow Use Case.
 */
public class CalendarFlowInputData {
    private final LocalDate selectedDate;
    private final Location userLocation;
    private final double radiusKm;

    public CalendarFlowInputData(LocalDate selectedDate, Location userLocation, double radiusKm) {
        this.selectedDate = selectedDate;
        this.userLocation = userLocation;
        this.radiusKm = radiusKm;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public double getRadiusKm() {
        return radiusKm;
    }

}
