package data_access;

import entity.Event;
import entity.EventRepository;
import entity.Location;

import java.util.List;
import java.util.Optional;

public class TicketmasterEventRepositoryAdapter implements EventRepository {
    private final EventDataAccessObject dao;
    private final Location defaultCenter;
    private final double defaultRadiusKm;

    public TicketmasterEventRepositoryAdapter(EventDataAccessObject dao,
                                              Location defaultCenter,
                                              double defaultRadiusKm) {
        this.dao = dao;
        this.defaultCenter = defaultCenter;
        this.defaultRadiusKm = defaultRadiusKm;
    }

    public List<Event> findEvents(Location center, double radiusKm) {
        return dao.getEventsByLocation(center, radiusKm);
    }

    @Override
    public List<Event> findAllEvents() {
        return dao.getEventsByLocation(defaultCenter, defaultRadiusKm);
    }

    @Override
    public Optional<Event> findById(String id) {
        return Optional.ofNullable(dao.getEventById(id));
    }

    @Override
    public List<Event> findByName(String query) {
        return dao.searchEventsByName(query, defaultCenter, defaultRadiusKm);
    }
}