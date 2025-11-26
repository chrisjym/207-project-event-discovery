package use_case.search;

import entity.Location;

public class SearchInputData {
    private final String query;
    private final String searchType;
    private final Location location;

    public SearchInputData(String query, String searchType, Location location) {
        this.query = query;
        this.searchType = searchType; // This is for implementation into search by location vs event
        this.location = location;
    }

    public String getQuery() { return query; }
    public String getSearchType() { return searchType; }

    public Location getLocation() {
        return location;
    }
}
