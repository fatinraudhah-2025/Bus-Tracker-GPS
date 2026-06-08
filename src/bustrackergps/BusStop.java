/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

/**
 * BusStop.java
 * Represents a single bus stop node in the route graph.
 * Each stop has a unique ID, a name, and a location description.
 */
public class BusStop {
    private String stopId;
    private String name;
    private String location; // e.g., "Near Main Gate", "Block A"
    private double latitude;
    private double longitude;

    public BusStop(String stopId, String name, String location, double latitude, double longitude) {
        this.stopId = stopId;
        this.name = name;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getStopId()    { return stopId; }
    public String getName()      { return name; }
    public String getLocation()  { return location; }
    public double getLatitude()  { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public String toString() {
        return "[" + stopId + "] " + name + " (" + location + ")";
    }
}