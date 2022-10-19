package org.java.personal.project.domain;

import lombok.Builder;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "location")
public class Location {
    private String type;

    @GeoSpatialIndexed
    private double[] coordinates;

    public Location() {
    }

    public Location(String type, double longitude, double latitude) {
        this.type = type;
        this.coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
