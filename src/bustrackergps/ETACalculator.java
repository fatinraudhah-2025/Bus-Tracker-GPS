/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

/**
 *
 * @author fatin raudhah
 */
public class ETACalculator {
    
    private static final double BUS_SPEED_MPS = 8.33; //average bus speed in campus 8.33 m/s
    
    //calculate distance between 2 coordinate points using Haversine Formula (in meter)
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        final int R = 6371; // earth radius in KM
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance /2) * Math.sin(latDistance /2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance /2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distanceInKm = R * c;
        
        return distanceInKm * 1000; //change to meter
    }
    
    //calculate ETA in minutes
    public static int calculateETA(double distanceMetres){
        if (distanceMetres <= 0) return 0;
        double timeInSeconds = distanceMetres / BUS_SPEED_MPS;
        return (int) Math.ceil(timeInSeconds / 60);  //round to the nearest minutes
    }

    
}
