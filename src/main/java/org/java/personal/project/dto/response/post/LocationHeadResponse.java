package org.java.personal.project.dto.response.post;

public class LocationHeadResponse {
    private String locationName;
    private LocationResponse location;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }
}
