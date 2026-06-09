/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

import java.util.function.Consumer;

/**
 *
 * GPSSimulator can output updates either to System.out (existing behavior)
 * or to a provided Consumer<String> for UI display.
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

    /**
     * Backwards-compatible: prints to System.out
     */
    public void startJourney() {
        startJourney(System.out::println);
    }

    /**
     * Start the journey and emit textual updates via the provided logger.
     */
    public void startJourney(Consumer<String> logger) {
        logger.accept("Starting GPS Simulation for bus " + busId);

        while (route.hasStops()) {
            BusStopGPS nextStop = route.getNextStop();
            logger.accept("\nTravelling to: " + nextStop.getStopName());

            moveBus(nextStop, logger);

            logger.accept("Arrived at: " + nextStop.getStopName());

            route.removeCurrentStop();

            if (route.hasStops()) {
                logger.accept("Next Stop: " + route.getNextStop().getStopName());
            } else {
                logger.accept("No more stops remaining");
            }
        }

        logger.accept("\n" + busId + " has successfully completed the entire route");
    }

    private void moveBus(BusStopGPS destination, Consumer<String> logger) {
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

            logger.accept(String.format("GPS Update %d -> Lat: %.6f | Lon: %.6f", i, currentLatitude, currentLongitude));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.accept("Simulation interrupted: " + e.toString());
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

