/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 *
 * @author fatin raudhah
 */
public class ArrivalTracker {
    
    //inner class to keep bus record and eta info
    public static class BusArrival{
        String busId;
        String nextStopName;
        double distance;
        int etaMinutes;
        
        public BusArrival(String busId, String nextStopName, double distance, int etaMinutes){
            this.busId = busId;
            this.nextStopName = nextStopName;
            this.distance = distance;
            this.etaMinutes = etaMinutes;
        }
        
        @Override
        public String toString() {
            return String.format("Bus %-7s | Next Stop: %-20s | Distance: %.1fm | ETA: %d mins", busId, nextStopName, distance, etaMinutes);
        }
    }
    
    private PriorityQueue<BusArrival> arrivalQueue;
    
    public ArrivalTracker(){
        this.arrivalQueue = new PriorityQueue<>(Comparator.comparingInt(b -> b.etaMinutes));
    }
    
    public void registerBusArrival(String busId, BusStop nextStop, double currentLat, double currentLon) {
        
        //calculate distance using calculator
        double distance = ETACalculator.calculateDistance(currentLat, currentLon, nextStop.getLatitude(), nextStop.getLongitude());
        
        //calculate minute ETA
        int eta = ETACalculator.calculateETA(distance);
        
        BusArrival arrival = new BusArrival(busId, nextStop.getName(), distance, eta);
        arrivalQueue.offer(arrival);
    }
    
    //Display the bus ranking from the fastest to the slowest
    public void displayNearestArrivals(String targetStopName){
        System.out.println("\n--------------------------------------------");
        System.out.println(" LIVE ARRIVAL RANKING FOR STATION: " + targetStopName.toUpperCase());
        System.out.println("--------------------------------------------");
        
        if(arrivalQueue.isEmpty()) {
            System.out.println("No buses currently running towards this area.");
            return;
        }
        
        int rank = 1;
        
        while(!arrivalQueue.isEmpty()) {
            BusArrival bus = arrivalQueue.poll();
            System.out.println(rank + ". " + bus);
            rank++;
        }
        System.out.println("--------------------------------------------");
    }
   
}
