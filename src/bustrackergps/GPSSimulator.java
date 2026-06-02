/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

/**
 *
 * @author User
 */
public class GPSSimulator {
    private String busId;
    private double currentLatitude;
    private double currentLongitude;
    private UpcomingStops route;

    public GPSSimulator(String busId, double startLat, double startLon, UpcomingStops route) {
        
        this.busId = busId;
        this.currentLatitude = startLat;
        this.currentLongitude = startLon;
        this.route = route;
    }

    public void startJourney() {

        System.out.println("Starting GPS Simulation for bus " + busId);

        while (route.hasStops()) {

            BusStopGPS nextStop = route.getNextStop();

            System.out.println("\nTravelling to: " + nextStop.getStopName());

            moveBus(nextStop);

            System.out.println("Arrived at: " + nextStop.getStopName());

            route.removeCurrentStop();

            if (route.hasStops()) {
                System.out.println("Next Stop: " + route.getNextStop().getStopName());
            }
            else {
                System.out.println("No more stops remaining");
            }
        }

        System.out.println("\n" + busId + "has succesfully completed the entire route");
    }

    private void moveBus(BusStopGPS destination) {

        double destinationLat = destination.getLatitude();
        double destinationLon = destination.getLongitude();

        int updates = 5;

        double latStep = (destinationLat - currentLatitude) / updates;

        double lonStep = (destinationLon - currentLongitude) / updates;
            
        for (int i = 1; i <= updates; i++) { 
            if (i == updates) {
        currentLatitude = destinationLat;
        currentLongitude = destinationLon; 
            } else { 
                currentLatitude += latStep;
                currentLongitude += lonStep; 
            }
        
            System.out.printf("GPS Update %d -> Lat: %.6f | Lon: %.6f%n", i, currentLatitude, currentLongitude);

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } 
}

