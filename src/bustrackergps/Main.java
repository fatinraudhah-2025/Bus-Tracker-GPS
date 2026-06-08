/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bustrackergps;

import java.util.Scanner;
/**
 * Main.java
 * ─────────────────────────────────────────────────────────────────────────────
 * Step 1 – Bus Route and Schedule Tracker
 * Demonstrates: Graph (RouteGraph) + Linked List (BusRoute)
 *
 * Campus scenario  (MMU Cyberjaya-style layout):
 *   Three shuttle routes share a common network of 10 stops.
 *
 *   Route T1  – Outer Loop   (6 stops, clockwise ring)
 *   Route T2  – Inner Shuttle (4 stops, core academic zone)
 *   Route T3  – Express Link  (3 stops, library ↔ hostel express)
 * ─────────────────────────────────────────────────────────────────────────────
 */
public class Main {

    public static void main(String[] args) {

        //System.out.println("==============================================");
        //System.out.println("  STEP 1 : Registering Bus Stops (Nodes)");
        //System.out.println("==============================================");

        RouteGraph graph = new RouteGraph();

        BusStop mainGate      = new BusStop("S01", "Main Gate",             "Entrance / Exit", 3.120000, 101.650000);
        BusStop adminBlock    = new BusStop("S02", "Admin Block",            "Administrative Hub", 3.121000, 101.651000);
        BusStop library       = new BusStop("S03", "Central Library",        "Block A, Level 1", 3.122500, 101.652100);
        BusStop cafeteria     = new BusStop("S04", "Main Cafeteria",         "Central Campus", 3.123000, 101.653500);
        BusStop engFaculty    = new BusStop("S05", "Engineering Faculty",    "Block FKE", 3.124500, 101.651200);
        BusStop itFaculty     = new BusStop("S06", "IT Faculty",             "Block FCM", 3.121800, 101.654000);
        BusStop stadium       = new BusStop("S07", "Sports Stadium",         "West Campus", 3.126000, 101.650500);
        BusStop hostelA       = new BusStop("S08", "Hostel A",               "North Residential", 3.127500, 101.652800);
        BusStop hostelB       = new BusStop("S09", "Hostel B",               "North Residential", 3.127000, 101.654200);
        BusStop medicalCentre = new BusStop("S10", "Medical Centre",         "Health Building", 3.124000, 101.656000);

        graph.addStop(mainGate);
        graph.addStop(adminBlock);
        graph.addStop(library);
        graph.addStop(cafeteria);
        graph.addStop(engFaculty);
        graph.addStop(itFaculty);
        graph.addStop(stadium);
        graph.addStop(hostelA);
        graph.addStop(hostelB);
        graph.addStop(medicalCentre);

        //System.out.println("\n==============================================");
        //System.out.println("  STEP 2 : Mapping Connections (Edges)");
        //System.out.println("==============================================");

        graph.addBidirectionalConnection("S01", "S02",  350);
        graph.addBidirectionalConnection("S02", "S03",  400);
        graph.addBidirectionalConnection("S03", "S05",  300);
        graph.addBidirectionalConnection("S05", "S07",  500);
        graph.addBidirectionalConnection("S07", "S08",  450);
        graph.addBidirectionalConnection("S08", "S09",  150);
        graph.addBidirectionalConnection("S09", "S01",  600);
        graph.addBidirectionalConnection("S03", "S04",  200);
        graph.addBidirectionalConnection("S04", "S06",  250);
        graph.addBidirectionalConnection("S06", "S02",  300);
        graph.addBidirectionalConnection("S04", "S10",  350);
        graph.addBidirectionalConnection("S10", "S01",  400);
        graph.addConnection("S03", "S09", 700);

        graph.printGraph();

        //System.out.println("==============================================");
        //System.out.println("  STEP 3 : Building Bus Routes (Linked Lists)");
        //System.out.println("==============================================");

        BusRoute routeT1 = new BusRoute("T1", "Outer Loop (Clockwise)", graph);
        //System.out.println("\nBuilding Route T1...");
        routeT1.addStop("S01");
        routeT1.addStop("S02");
        routeT1.addStop("S03");
        routeT1.addStop("S05");
        routeT1.addStop("S07");
        routeT1.addStop("S08");
        routeT1.addStop("S09");

        BusRoute routeT2 = new BusRoute("T2", "Inner Shuttle", graph);
        //System.out.println("\nBuilding Route T2...");
        routeT2.addStop("S02");
        routeT2.addStop("S03");
        routeT2.addStop("S04");
        routeT2.addStop("S06");

        BusRoute routeT3 = new BusRoute("T3", "Library-Hostel Express", graph);
        //System.out.println("\nBuilding Route T3...");
        routeT3.addStop("S03");
        routeT3.addStop("S09");
        routeT3.addStop("S08");

        //System.out.println("\n==============================================");
        //System.out.println("  STEP 4 : Full Route Paths");
        //System.out.println("==============================================");
        //routeT1.printRoute();
        //routeT2.printRoute();
       // routeT3.printRoute();

        //System.out.println("\n==============================================");
        //System.out.println("  STEP 5 : Stop Retrieval Demos");
        //System.out.println("==============================================");

        //System.out.println("\n[T1] Stop at index 3 : " + routeT1.getStopAt(3));

        int libIndex = routeT2.indexOf("S03");
        //System.out.println("[T2] 'Central Library' is stop #" + (libIndex + 1)
                           //+ " (index " + libIndex + ")");

        BusStop found = graph.getStop("S10");
        //System.out.println("[Graph] Direct lookup S10 : " + found);

        //System.out.println("\n[Graph] Neighbours of Central Library (S03):");
        for (RouteGraph.Edge edge : graph.getNeighbours("S03")) {
            //System.out.println("         --> " + edge);
        }

        //System.out.println("\n==============================================");
        //System.out.println("  Step 1 Complete. Graph & Routes ready.");
        //System.out.println("==============================================");
        
        ScheduleMap tracker = new ScheduleMap(routeT1, routeT2, routeT3);
        Scanner input = new Scanner(System.in);
        
        boolean running = true;
        
        while(running){
        System.out.println("\n===BUS SCHEDULE===");
        System.out.println("1. Search Bus Schedule by Bus ID (e.g. T1, T2, T3)" );
        System.out.println("2. Search Bus Schedule by Route Name (e.g. Inner Shuttle) ");
        System.out.println("3. View Live GPS Tracking & Arrival Ranking");
        System.out.println("4. Exit");
        System.out.print("Choose option : ");
        
        int choice = input.nextInt();
        input.nextLine();
        
        System.out.println();
        
        if (choice == 1){
            System.out.print("Enter Bus ID: ");
            String searchID = input.nextLine();
            BusSchedule result = tracker.getScheduleByID(searchID);
            
            System.out.println("\n--- Search Result ---");
            if (result != null){
                result.DisplayInfo();
                result.route.printRoute();
            } else {
                System.out.println("Bus ID ' " + searchID + "' not found...");
            }
            
        }else if (choice == 2){
            System.out.print("Enter Route Name: ");
            String searchRoute = input.nextLine();
            BusSchedule result = tracker.getScheduleByStop(searchRoute);
            
            System.out.println("\n--- Search Result ---");
            if (result != null){
                result.DisplayInfo();
                result.route.printRoute();
            }else {
                System.out.println("Route '" + searchRoute + "' not found...");
            }
            
        }else if (choice == 3){
            runLiveTrackingMenu(input, tracker, library);
            
        }else if(choice == 4){
            System.out.println("Thankyou for using this system. Till we meet again");
            running = false;
            
        }else {
            System.out.println("Invalid option.");
        }
        
        
    }
  
        
        
        //eta & priority queue ranking
        ArrivalTracker trackerQueue = new ArrivalTracker();
        
        trackerQueue.registerBusArrival("Bus T1", library, 3.1150, 101.6400);
        trackerQueue.registerBusArrival("Bus T2", library, 3.1210, 101.6510);
        trackerQueue.registerBusArrival("Bus T3", library, 3.1180, 101.6450);
        trackerQueue.displayNearestArrivals("Central Library");
}

    public static void runLiveTrackingMenu(Scanner input, ScheduleMap tracker, BusStop libraryTarget) {
        System.out.println("=== LIVE GPS TRACKING MODULE ===");
        System.out.print("Enter Bus ID to track (T1 / T2 / T3): ");
        String busChoice = input.nextLine().toUpperCase();

        BusSchedule selectedSchedule = tracker.getScheduleByID(busChoice);

        if (selectedSchedule == null) {
            System.out.println("Bus ID '" + busChoice + "' is not active or not found.");
            return;
        }

        System.out.println("\nInitializing simulation for " + selectedSchedule.busID + " (" + selectedSchedule.route.getRouteName() + ")...");
        
        UpcomingStops routeQueue = new UpcomingStops();
        for (BusStop stop : selectedSchedule.route.getOrderedStops()) {
            routeQueue.addStop(new BusStopGPS(stop.getName(), stop.getLatitude(), stop.getLongitude()));
        }

        // take bus initial location
        double startLat = selectedSchedule.route.getStopAt(0).getLatitude() - 0.001;
        double startLon = selectedSchedule.route.getStopAt(0).getLongitude() - 0.001;
        
        GPSSimulator gps = new GPSSimulator(selectedSchedule.busID, startLat, startLon, routeQueue);
        gps.startJourney();


        System.out.println("\n--- Calculating Live Arrival Priority Queue Ranking ---");
        ArrivalTracker arrivalTracker = new ArrivalTracker();
        
        arrivalTracker.registerBusArrival(selectedSchedule.busID, libraryTarget, startLat + 0.001, startLon + 0.001);
        
        String[] allBuses = {"T1", "T2", "T3"};
        for (String id : allBuses) {
            if (!id.equals(selectedSchedule.busID)) { 
                BusSchedule otherBus = tracker.getScheduleByID(id);
                if (otherBus != null) {
                    double randomOffsetLat = (Math.random() - 0.5) * 0.01;
                    double randomOffsetLon = (Math.random() - 0.5) * 0.01;
                    double liveLat = libraryTarget.getLatitude() + randomOffsetLat;
                    double liveLon = libraryTarget.getLongitude() + randomOffsetLon;
                    
                    arrivalTracker.registerBusArrival(otherBus.busID, libraryTarget, liveLat, liveLon);
                }
            }
        }
        
        arrivalTracker.displayNearestArrivals(libraryTarget.getName());
    }
}