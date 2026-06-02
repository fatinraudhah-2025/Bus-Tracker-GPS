/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

/**
 *
 * @author User
 */
public class BusStopGPS {
    private String stopName;
    private double latitude;
    private double longitude;

    public BusStopGPS(String stopName, double latitude, double longitude) {
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getStopName() {
        return stopName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return stopName;
    }
    
}
